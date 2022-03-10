package games.apolion.multiplatformer.udpnet.entitties;

public interface MessageObject {
	ServerCommands getType();
	String get(String s);
	String getMessage();
}
