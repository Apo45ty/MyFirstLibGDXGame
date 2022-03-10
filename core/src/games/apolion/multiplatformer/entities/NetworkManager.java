package games.apolion.multiplatformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import games.apolion.multiplatformer.Main;
import games.apolion.multiplatformer.screens.GameScreen;
import games.apolion.multiplatformer.udpnet.entitties.*;
import games.apolion.multiplatformer.utils.HttpRequestSender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkManager {

    private static final float MIN_FRAME_LENGTH = 1f/20f;
    private static final float MIN_FRAME_LENGTH_PLAYER_REFRESH = 3;
    private float timeSinceLastRender;
    private String  TAG = NetworkManager.class.getName();
    private final SocketListener socketListener;
    private final Thread socketThread;
    private Queue<MessageObject> globalQueue = new ConcurrentLinkedQueue<MessageObject>();
    private boolean joinedServer = false;
    private final GameScreen gameScreen;
    public static int basePort=12000,bufSize= 1024;
    private DatagramSocket socket;
    private List<NetworkPlayer> networkPlayerList= new ArrayList<NetworkPlayer>();
    private final Main main;
    private boolean registeredPort = false;

    public NetworkManager(GameScreen gameScreen, Main main){
        this.main=main;
        int count = 0;
        boolean foundAPort = true;
        do
        {
            try
            {
                //LOG.info("base port "+basePort);
                socket = new DatagramSocket(basePort+ count);
                foundAPort = true;
            }
            catch (Exception e)
            {
                count++;
                foundAPort = false;
            }
        } while (!foundAPort);

        Gdx.app.log(TAG,"PortNumber "+basePort+ count);
        this.gameScreen=gameScreen;

        socketListener = new SocketListener(socket,globalQueue,new TempMessageFactory());
        socketThread = new Thread(socketListener);
        socketThread.start();

    }
    public float getAxis(String axis, NetworkPlayer networkPlayer) {
        return 0f;
    }
    public void sendUDPPacket(String message){
        byte[] buf = new byte[bufSize];
        buf = message.getBytes();
        DatagramPacket packet = null;
        try {
            Gdx.app.log(TAG, "IP:" + InetAddress.getLocalHost()+" Port:"+HttpRequestSender.me.gameDescriptor.port);
            packet = new DatagramPacket(buf, buf.length,  InetAddress.getLocalHost(), HttpRequestSender.me.gameDescriptor.port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        if(!joinedServer)
            HttpRequestSender.me.JoinAnyGame();
    }

    public void update(float delta) {
        timeSinceLastRender += delta;
        if (timeSinceLastRender >= MIN_FRAME_LENGTH) {
            // Do the actual rendering, pass timeSinceLastRender as delta time.
            timeSinceLastRender = 0f;
            while(globalQueue.size()>0) {
                //POLL MESSAGES
                MessageObject mObj = globalQueue.poll();
                if (mObj != null) {
                    //Gdx.app.log(TAG,HttpRequestSender.me.token.token);
                    ///Gdx.app.log(TAG,mObj.get("token"));
                    if(mObj.get("token")== null)
                        continue;
                    if (HttpRequestSender.me.tokenDTO.token.trim().equals(mObj.get("token").trim())) {
                        ///Gdx.app.log(TAG,mObj.getMessage());
                        if (mObj.getType() == ServerCommands.portHasBeenRegister) {
                            registeredPort = true;
                            for(String user:HttpRequestSender.me.gameDescriptor.users){
                                Gdx.app.log(TAG,"User:"+user);
                                if(user.equals(HttpRequestSender.me.tokenDTO.username)){
                                    continue;
                                }
                                NetworkPlayer newP = new NetworkPlayer(10, -10, 30, 30, gameScreen.getTextureAtlas(), gameScreen.getPlayer(), gameScreen.getCamera(), user);
                                this.networkPlayerList.add(newP);
                                gameScreen.addGameObject(newP);
                            }
                        } else if (mObj.getType() == ServerCommands.chat) {
                            String temp1 = mObj.getMessage().split(ServerCommands.chat.commandStr+":")[1];
                            String temp2 = temp1.split("user:")[1];
                            int index = temp2.indexOf(" ");
                            String user = temp2.substring(0,index);
                            String message = temp2.substring(index);
                            gameScreen.chatTextArea.appendText(user+":"+message+"\n");
                            gameScreen.resizeScrollview();
                        } else if (mObj.getType() == ServerCommands.instantiateNewPlayer) {
                            globalQueue.clear();
                            String username = mObj.getMessage().split("user:")[1].split(" ")[0];
                            boolean userAlreadyInGame = false;
                            for (NetworkPlayer np : networkPlayerList) {
                                if (np.getUsername().equals(username)) {
                                    userAlreadyInGame = true;
                                    break;
                                }
                            }
                            if (!userAlreadyInGame) {
                                Gdx.app.log(TAG, "User joined: " + username);
                                NetworkPlayer newP = new NetworkPlayer(10, -10, 30, 30, gameScreen.getTextureAtlas(), gameScreen.getPlayer(), gameScreen.getCamera(), username);
                                this.networkPlayerList.add(newP);
                                gameScreen.addGameObject(newP);
                            }
                        } else if (mObj.getType() == ServerCommands.setPosRot) {
                            String username = mObj.getMessage().split("user:")[1].split(" ")[0];
                            for (NetworkPlayer np : networkPlayerList) {
                                if (np.getUsername().equals(username)) {
                                    int x = Integer.parseInt(mObj.getMessage().split("x:")[1].split(" ")[0]);
                                    int y = Integer.parseInt(mObj.getMessage().split("y:")[1].split(" ")[0]);
                                    float xVel = Float.parseFloat(mObj.getMessage().split("xVel:")[1].split(" ")[0]);
                                    float yVel = Float.parseFloat(mObj.getMessage().split("yVel:")[1].split(" ")[0]);
                                    np.setxPos(x);
                                    np.setyPos(y);
                                    np.setVelocity(new Vector2(xVel, yVel));
                                    break;
                                }
                            }
                        }

                        Gdx.app.log(TAG, mObj.getMessage());
                    }
                }
            }
            //Check Status
            if(HttpRequestSender.me.gameDescriptor!=null){
                joinedServer=true;
                if(!registeredPort){
                    Gdx.app.log(TAG,"Registering Port");
                    sendUDPPacket(ServerCommands.registerPort.commandStr+":"+" token:"+HttpRequestSender.me.tokenDTO.token);
                } else {
                    sendUDPPacket(ServerCommands.setPosRot.commandStr+": "+" x:"+gameScreen.getPlayer().getX()+" y:"+gameScreen.getPlayer().getY()
                            +" xVel:"+gameScreen.getPlayer().getVelocity().x+" yVel:"+gameScreen.getPlayer().getVelocity().y
                            +" token:"+HttpRequestSender.me.tokenDTO.token);
                }
            } else {
                Gdx.app.log(TAG,"Joining game");
            }

            for(NetworkPlayer np:networkPlayerList){
                np.readInput(this,delta);
            }
        }


    }

    public void dispose() {
        socketListener.running =false;
        try {
            socketThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
