import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientManager implements Runnable
{
    Socket assigned_socket;
    UserArchive archive;

    public ClientManager(Socket client_socket, UserArchive archive)
    {
        this.assigned_socket = client_socket;
        this.archive = archive;
    }

    @Override
    public void run()
    {
        try
        {
            Scanner from_client = new Scanner(assigned_socket.getInputStream());
            var to_client = new PrintWriter(assigned_socket.getOutputStream());

            boolean done = false;
            String msg_from_client;

            while (!done)
            {
                msg_from_client = from_client.nextLine();
                System.out.println("Received message: " + msg_from_client + "from client " + assigned_socket.getRemoteSocketAddress());
                Scanner message_scanner = new Scanner(msg_from_client);
                String command = message_scanner.next();

                if (command.equals("LOGIN")) {
                    String username = message_scanner.next();
                    String password = message_scanner.next();

                    //Here will be implemented the login

                    to_client.println("LOGGED_IN");
                    to_client.flush();
                } else if (command.equals("QUIT")) {
                    done = true;
                    System.out.println("Client " + assigned_socket.getRemoteSocketAddress() + " quitted");
                    assigned_socket.close();
                } else {
                    System.out.println("Not valid command received " + command + "");
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Input/Output error!!\n\n");
            e.printStackTrace();
        }
    }
}
