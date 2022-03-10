package games.apolion.multiplatformer.udpnet.entitties;

import java.net.InetAddress;

public abstract class MessageFactory {
	
	public abstract MessageObject parse(String message, InetAddress address, int port);
}
