/**
 * @author Bukowski Dawid S22310
 */

package zad1;


public class ChatServer {

    private final String host;
    private final int port;
    private Selector selector;

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startServer() {
        try {
            selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {

    }

    private String getServerLog() {

    }


}
