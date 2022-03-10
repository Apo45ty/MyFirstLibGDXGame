package games.apolion.multiplatformer.udpnet.entitties;

import java.net.InetAddress;

public class TempMessageFactory extends MessageFactory {


    public static String destinationTokenDelimiter = "Dtoken:";

    @Override
    public MessageObject parse(final String message, final InetAddress address, final int port) {
        if (message.indexOf(ServerCommands.chat.commandStr + ":") == 0)
            return new MessageObjectTempImp(message, address, port, ServerCommands.chat);
        else if (message.contains(ServerCommands.setPosRot.commandStr + ":"))
            return new MessageObjectTempImp(message, address, port, ServerCommands.setPosRot);
        else if (message.contains(ServerCommands.portHasBeenRegister.commandStr))
            return new MessageObjectTempImp(message, address, port, ServerCommands.portHasBeenRegister);
        else if (message.contains(ServerCommands.instantiateNewPlayer.commandStr))
            return new MessageObjectTempImp(message, address, port, ServerCommands.instantiateNewPlayer);

        return null;
    }

}
