import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientManager implements Runnable
{
    Socket assigned_socket;
    UserArchive archive;
    User current_user;
    StatsArchive archive_stats;

    public ClientManager(Socket client_socket, UserArchive archive, StatsArchive archive_stats)
    {
        this.assigned_socket = client_socket;
        this.archive = archive;
        this.archive_stats = archive_stats;
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
                        current_user = archive.findUser(user_tmp);

                        if (current_user == null)
                        {
                            to_client.println("INVALID_LOGIN");
                            to_client.flush();
                        }
                        else
                        {
                            to_client.println("LOGGED_IN");
                            to_client.flush();
                            sendUserToClient(current_user);
                            System.out.println(current_user.getUsername()+"'s user information sent to client " + assigned_socket.getRemoteSocketAddress());
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
                            System.out.println("New user: "+user_signup.getUsername()+" registered from client " + assigned_socket.getRemoteSocketAddress());
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
                        }
                        else
                        {
                            to_client.println("VIDEOGAME_FOUND");
                            to_client.flush();
                            sendVideogameToClient(vg_tmp);
                            System.out.println(title+" found. "+vg_tmp.getTitle()+" sent to client " + assigned_socket.getRemoteSocketAddress());
                        }
                        break;

                    case "GET_STATS":
                        sendStatsArchiveToClient();
                        System.out.println("Stats about "+current_user.getUsername()+" sent to client "+ assigned_socket.getRemoteSocketAddress());
                        break;

                    case "UPDATE_INFORMATION":
                        current_user = updateUserInfo();
                        archive.updateUser(current_user);
                        System.out.println("Updated information about "+current_user.getUsername()+" from client "+ assigned_socket.getRemoteSocketAddress());
                        break;

                    case "QUIT":
                        done = true;
                        System.out.println("Client " + assigned_socket.getRemoteSocketAddress() + " quitted");
                        assigned_socket.close();
                        break;

                    default:
                        System.out.println("Not valid command received " + command +" from client "+ assigned_socket.getRemoteSocketAddress());
                        break;
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Input/Output error!!\n\n");
            e.printStackTrace();
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Scanner error!!\n\n");
            e.printStackTrace();
        }
    }

    //Send all user information to client which is logged
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
                to_client.println(pf.getName());
                to_client.println(pf.getCompany());
                to_client.println(pf.getReleaseYear());
            }
            to_client.println("END_PLATFORMS");

            for(Achievement a : vg.getAchievements())
            {
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


    //Receive the user information from the client after a command on the menuStart() of the Client
    private User updateUserInfo() throws IOException
    {
        Scanner from_client = new Scanner(assigned_socket.getInputStream());

        String[] info = new String[5]; int i_info; boolean b_info;
        User u; Person p; Videogame v; Achievement a; Platform pf;
        AchievementStats as;

        info[0] = from_client.nextLine(); //username
        info[1] = from_client.nextLine(); //password
        info[2] = from_client.nextLine(); //name
        info[3] = from_client.nextLine(); //surname
        info[4] = from_client.nextLine(); //email

        u = new User(info[0], info[1]);
        p = new Person(info[2], info[3], info[4]);
        u.setInformation(p);

        while (true)
        {
            info[0] = from_client.nextLine(); //title or message that ends the stream
            if (info[0].equals("END_VIDEOGAMES")) break;
            info[1] = from_client.nextLine(); //description
            i_info = Integer.parseInt(from_client.nextLine()); //rating
            info[2] = from_client.nextLine(); //softwareHouse

            v = new Videogame(info[0], info[1], i_info, info[2]);
            u.addVideogame(v);

            while (true)
            {
                info[0] = from_client.nextLine(); //name or message that ends the stream
                if (info[0].equals("END_PLATFORMS")) break;
                info[1] = from_client.nextLine(); //company
                i_info = Integer.parseInt(from_client.nextLine()); //releaseYear

                pf = new Platform(info[0], info[1], i_info);
                v.addPlatform(pf);

            }

            while (true)
            {
                info[0] = from_client.nextLine(); //name or message that ends the stream
                if (info[0].equals("END_ACHIEVEMENTS")) break;
                info[1] = from_client.nextLine(); //name of the videogame
                b_info = Boolean.parseBoolean(from_client.nextLine()); //unlocked
                info[2] = from_client.nextLine(); //description

                a = new Achievement(info[0], info[1], b_info, info[2]);
                v.addAchievement(a);
                as = new AchievementStats(a);
                archive_stats.updateStats(as);
            }
        }
        return u;
    }

    //Send a videogame found in the server to a user for add it on library (achievement locked)
    private void sendVideogameToClient(Videogame videogame) throws IOException
    {
        var to_client = new PrintWriter(assigned_socket.getOutputStream());

        to_client.println(videogame.getTitle());
        to_client.println(videogame.getDescription());
        to_client.println(videogame.getRating());
        to_client.println(videogame.getSoftwareHouse());

        for(Platform pf : videogame.getPlatformList())
        {
            to_client.println(pf.getName());
            to_client.println(pf.getCompany());
            to_client.println(pf.getReleaseYear());
        }
        to_client.println("END_PLATFORMS");

        for(Achievement a : videogame.getAchievements())
        {
            to_client.println(a.getName());
            to_client.println(a.getVideogame_name());
            to_client.println(a.getDescription());
        }
        to_client.println("END_ACHIEVEMENTS");
        to_client.flush();
    }

    //Send a StatsArchive to the client, this archive contains statistic data of the games in the library of the user
    private void sendStatsArchiveToClient() throws IOException
    {
        var to_client = new PrintWriter(assigned_socket.getOutputStream());

        Achievement ac;

        for (AchievementStats as : archive_stats.getArchive())
        {
            for (Videogame vg : current_user.getLibrary())
            {
                ac = as.getAchievement();
                if (vg.getTitle().equalsIgnoreCase(ac.getVideogame_name()))
                {
                    to_client.println(ac.getName());
                    to_client.println(ac.getVideogame_name());
                    to_client.println(ac.getDescription());
                    to_client.println(as.getN_unlocked());
                    to_client.println(as.getN_total());
                    to_client.println(as.getUnlock_percentage());
                }
            }
        }
        to_client.println("END_ACHIEVEMENTS");
        to_client.flush();
    }
}
