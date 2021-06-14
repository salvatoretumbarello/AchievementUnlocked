import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    ServerSocket server_socket;
    Socket client_socket;
    private int port;

    private UserArchive archive;

    public Server(int starting_port)
    {
        //For now I create a new empty archive...
        archive = new UserArchive();

        this.port = starting_port;
        System.out.println("Created server on port "+port);
    }

    public void start()
    {
        try
        {
            server_socket = new ServerSocket(port);

            while (true)
            {
                System.out.println("Listening on port "+port);
                client_socket = server_socket.accept();
                System.out.println("Accepted connection from client " + client_socket.getRemoteSocketAddress());

                ClientManager cm = new ClientManager(client_socket, archive);
                Thread t = new Thread(cm);
                t.start();
            }
        }
        catch (IOException e)
        {
            System.out.println("Cannot start the server!!");
            e.printStackTrace();
        }
    }
}
