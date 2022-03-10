package games.apolion.multiplatformer.udpnet.entitties;

import games.apolion.multiplatformer.entities.NetworkManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Queue;

public class SocketListener implements  Runnable{
    private final DatagramSocket socket;
    private final Queue<MessageObject> globalQueue;
    public boolean running;
    private int bufSize;
    private byte[] buf;
    private MessageFactory messageFactory;

    public SocketListener(DatagramSocket socket, Queue<MessageObject> globalQueue,MessageFactory messageFactory) {
        this.socket = socket;
        this.globalQueue = globalQueue;
        this.bufSize = NetworkManager.bufSize;
        this.messageFactory=messageFactory;
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            buf = new byte[bufSize];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received = new String(packet.getData(), 0, packet.getLength());

            MessageObject mObj = messageFactory.parse(received,address,port);
            if( mObj != null )
                globalQueue.add(mObj);
        }
    }
}
