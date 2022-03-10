package games.apolion.multiplatformer.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import games.apolion.multiplatformer.http.DTO.GameDescriptorDTO;
import games.apolion.multiplatformer.http.DTO.TokenDTO;

public class HttpRequestSender {

    private String  TAG = HttpRequestSender.class.getName();
    static {
        me = new HttpRequestSender();
    }
    public static HttpRequestSender me ;
    public final Json json = new Json();
    public TokenDTO tokenDTO;
    private boolean waitingForResponse=false;
    public GameDescriptorDTO gameDescriptor;

    public HttpRequestSender(){
        if(me==null){
            me=this;
        }
    }
    public void JoinAnyGame(){

        if(tokenDTO ==null)
            return;

        Net.HttpRequest request = new Net.HttpRequest("POST");
        final String url = Constants.BASEURL+Constants.JOINANYGAMEENPOINT;
        request.setUrl(url);

        request.setHeader("accesstoken", tokenDTO.token);
        request.setHeader("g-recaptcha-response","");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                //String s = new String(httpResponse.getResult(), StandardCharsets.UTF_8);
                if(statusCode != HttpStatus.SC_OK) {
                    System.out.println("Request Failed");
                    return;
                }

                String responseJson = httpResponse.getResultAsString();
                try {
                    gameDescriptor = json.fromJson(GameDescriptorDTO.class,responseJson);
                }
                catch(Exception exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error(TAG,t.getMessage());
            }

            @Override
            public void cancelled() {
                Gdx.app.error(TAG,"Sending got cancelled");
            }
        });
    }
    public void sendLoginRequest(Object requestObject) {

        json.setOutputType(JsonWriter.OutputType.json);
        String requestJson = json.toJson(requestObject); // this is just an example

        Net.HttpRequest request = new Net.HttpRequest("POST");
        final String url = Constants.BASEURL+Constants.LOGINENPOINT;
        request.setUrl(url);

        request.setContent(requestJson);
        request.setHeader("g-recaptcha-response","");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                //String s = new String(httpResponse.getResult(), StandardCharsets.UTF_8);
                if(statusCode != HttpStatus.SC_OK) {
                    Gdx.app.log(TAG,"Status code "+statusCode);
                    return;
                }

                String responseJson = httpResponse.getResultAsString();
                try {
                    if(responseJson.contains("Invalid Credentials")){
                        tokenDTO = null;
                    } else
                        tokenDTO = json.fromJson(TokenDTO.class,responseJson);

                    //DO some stuff with the response string

                }
                catch(Exception exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log(TAG,"Request Failed"+t.getMessage());
            }

            @Override
            public void cancelled() {
                Gdx.app.log(TAG,"Request Cancelled");
            }
        });
    }

}
