import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
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
                System.out.println("Received message: " + msg_from_client + " from client " + assigned_socket.getRemoteSocketAddress());
                Scanner message_scanner = new Scanner(msg_from_client);
                String command = message_scanner.next();

                if (command.equals("LOGIN"))
                {
                    String username = message_scanner.next();
                    String password = message_scanner.next();

                    User tmp = new User(username, password);
                    User user_login = archive.findUser(tmp);

                    if (user_login == null)
                    {
                        to_client.println("INVALID_LOGIN");
                        to_client.flush();
                    }
                    else
                    {
                        to_client.println("LOGGED_IN");
                        to_client.flush();

                        //Here will be implements the passage of all information to client
                    }
                }
                else if (command.equals("SIGNUP"))
                {
                    String username = message_scanner.next();
                    String password = message_scanner.next();

                    //Checking if username is already used
                    User user_signup = new User(username, password);
                    User tmp = archive.findUser(user_signup);

                    if(tmp == null)
                    {
                        String name = message_scanner.next();
                        String surname = message_scanner.next();
                        String email = message_scanner.next();
                        Person p_signed = new Person(name, surname, email);
                        user_signup.setInformation(p_signed);
                        archive.add(user_signup);

                        to_client.println("SIGNED_UP");
                        to_client.flush();
                    }
                    else
                    {
                        to_client.println("USER_ALREADY_EXISTS");
                        to_client.flush();
                    }

                }
                else if (command.equals("QUIT"))
                {
                    done = true;
                    System.out.println("Client " + assigned_socket.getRemoteSocketAddress() + " quitted");
                    assigned_socket.close();
                }
                else
                {
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
