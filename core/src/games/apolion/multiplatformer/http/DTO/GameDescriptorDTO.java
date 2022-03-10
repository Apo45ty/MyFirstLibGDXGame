package games.apolion.multiplatformer.http.DTO;

public class GameDescriptorDTO {
    public String serverName;
    public int port;
    public String ip;
    public String[] users;
    public String state;
    public String errorMessage;
    public GameDescriptorDTO() {
    }
    public GameDescriptorDTO(String serverName, int port, String ip, String[] users, String host) {
        super();
        this.serverName = serverName;
        this.port = port;
        this.ip = ip;
        this.users = users;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String[] getUsers() {
        return users;
    }
    public void setUsers(String[] users) {
        this.users = users;
    }
}

