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
                String username, password;
                User user_tmp; Videogame vg_tmp;
                msg_from_client = from_client.nextLine();
                System.out.println("Received message: " + msg_from_client + " from client " + assigned_socket.getRemoteSocketAddress());
                Scanner message_scanner = new Scanner(msg_from_client);
                String command = message_scanner.next();

                switch (command)
                {
                    case "LOGIN":
                        username = message_scanner.next();
                        password = message_scanner.next();

                        user_tmp = new User(username, password);
                        User user_login = archive.findUser(user_tmp);

                        if (user_login == null) {
                            to_client.println("INVALID_LOGIN");
                            to_client.flush();
                        } else {
                            to_client.println("LOGGED_IN");
                            to_client.flush();
                            System.out.println("Sending user informations: " + user_login);

                            sendUserToClient(user_login);

                            System.out.println("Sending complete");
                        }
                        break;

                    case "SIGNUP":
                        username = message_scanner.next();
                        password = message_scanner.next();

                        User user_signup = new User(username, password);
                        user_tmp = archive.findUser(user_signup);

                        if (user_tmp == null)
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

                        break;

                    case "SEARCH_VIDEOGAME":
                        String title = from_client.nextLine();
                        vg_tmp = archive.findVideogame(title);
                        if (vg_tmp == null)
                        {
                            to_client.println("VIDEOGAME_NOT_FOUND");
                            to_client.flush();
                            System.out.println(title+" not found.");

                        }
                        else
                        {
                            to_client.println("VIDEOGAME_FOUND");
                            to_client.flush();
                            sendVideogameToClient(vg_tmp);
                            System.out.println(title+" found.");
                        }
                        break;

                    case "QUIT":
                        done = true;
                        System.out.println("Client " + assigned_socket.getRemoteSocketAddress() + " quitted");
                        assigned_socket.close();
                        break;

                        default:
                        System.out.println("Not valid command received " + command + "");
                        break;
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Input/Output error!!\n\n");
            e.printStackTrace();
        }
    }

    private void sendUserToClient(User user_login) throws IOException
    {
        var to_client = new PrintWriter(assigned_socket.getOutputStream());

        to_client.println(user_login.getUsername());
        to_client.println(user_login.getPassword());
        to_client.println(user_login.printPerson());

        for(Videogame vg : user_login.getLibrary())
        {
            to_client.println(vg.getTitle());
            to_client.println(vg.getDescription());
            to_client.println(vg.getRating());
            to_client.println(vg.getSoftwareHouse());

            for(Platform pf : vg.getPlatformList())
            {
                if (vg.getPlatformList().isEmpty()) break;
                to_client.println(pf.getName());
                to_client.println(pf.getCompany());
                to_client.println(pf.getReleaseYear());
            }
            to_client.println("END_PLATFORMS");

            for(Achievement a : vg.getAchievements())
            {
                if (vg.getAchievements().isEmpty()) break;
                to_client.println(a.getName());
                to_client.println(a.getVideogame_name());
                to_client.println(a.isUnlocked());
                to_client.println(a.getDescription());
            }
            to_client.println("END_ACHIEVEMENTS");
        }
        to_client.println("END_VIDEOGAMES");
        to_client.flush();
    }

    private void sendVideogameToClient(Videogame videogame) throws IOException
    {
        var to_client = new PrintWriter(assigned_socket.getOutputStream());

            to_client.println(videogame.getTitle());
            to_client.println(videogame.getDescription());
            to_client.println(videogame.getRating());
            to_client.println(videogame.getSoftwareHouse());

            for(Platform pf : videogame.getPlatformList())
            {
                if (videogame.getPlatformList().isEmpty()) break;
                to_client.println(pf.getName());
                to_client.println(pf.getCompany());
                to_client.println(pf.getReleaseYear());
            }
            to_client.println("END_PLATFORMS");

            for(Achievement a : videogame.getAchievements())
            {
                if (videogame.getAchievements().isEmpty()) break;
                to_client.println(a.getName());
                to_client.println(a.getVideogame_name());
                to_client.println(a.getDescription());
            }
            to_client.println("END_ACHIEVEMENTS");

        to_client.flush();
    }
}
