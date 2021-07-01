import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client
{
    String address_ip;
    int port;
    Socket client_socket;
    User current_user;

    public Client(String ip, int port)
    {
        this.address_ip = ip;
        this.port = port;
        System.out.println("Creating Client to connection "+address_ip+" "+port);
    }

    public void start()
    {
        try
        {
            client_socket = new Socket(address_ip, port);
            Scanner from_server = new Scanner(client_socket.getInputStream());
            var to_server = new PrintWriter(client_socket.getOutputStream());

            Scanner from_user = new Scanner(System.in);
            boolean done = false;
            String msg_to_send;
            String msg_received;

            while (!done)
            {
                int choice;
                System.out.println();
                System.out.println("_____ACHIEVEMENT_UNLOCKED_____");
                System.out.println("1) log in");
                System.out.println("2) sign up");
                System.out.println("0) quit");
                System.out.println(">>>");
                choice = from_user.nextInt();

                switch (choice)
                {
                    //LOGIN code
                    case 1:
                        msg_to_send = menuLogin();
                        to_server.println(msg_to_send);
                        to_server.flush();
                        System.out.println("\n\n");
                        msg_received = from_server.nextLine();
                        if (msg_received.equals("INVALID_LOGIN"))
                        {
                            System.out.println("Username and password not found!!");
                        }
                        else if (msg_received.equals("LOGGED_IN"))
                        {
                            System.out.println("Logged in");
                            System.out.println("Recovering your information");

                            current_user = loadUserFromServer();

                            System.out.println("Loaded: "+current_user);
                            menuStart();

                        }
                        else
                        {
                            System.out.println("An error occurred...\nTry again!");
                        }
                        System.out.println("\n\n");
                        break;

                    //SIGNUP code
                    case 2:
                        msg_to_send = menuSignUp();
                        to_server.println(msg_to_send);
                        to_server.flush();
                        System.out.println("\n\n");
                        msg_received = from_server.nextLine();

                        if (msg_received.equals("SIGNED_UP"))
                        {
                            System.out.println("Signed Up!");
                            System.out.println("You can now login");
                        }
                        else if (msg_received.equals("USER_ALREADY_EXISTS"))
                        {
                            System.out.println("This username is not available");
                            System.out.println("Please choose another username");
                        }
                        else
                        {
                            System.out.println("An error occurred...\nTry again!");
                        }
                        System.out.println("\n\n");
                        break;

                    //QUIT code
                    case 0:
                        done = true;
                        to_server.println("QUIT");
                        to_server.flush();
                        client_socket.close();
                        System.out.println("Quitting...\n");
                        break;

                    default:
                        System.out.println("Invalid choice!!\n\n");
                }


            }
        }
        catch (InputMismatchException e)
        {
            System.out.println("Invalid choice!!\n\n");
            e.printStackTrace();
            System.exit(1);
        }
        catch (UnknownHostException e)
        {
            System.out.println("Connection error!!\n\n");
            e.printStackTrace();
            System.exit(1);
        }
        catch (IOException e)
        {
            System.out.println("Input/Output error!!\n\n");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void menuStart()
    {
        try
        {
            Scanner from_server = new Scanner(client_socket.getInputStream());
            var to_server = new PrintWriter(client_socket.getOutputStream());
            Scanner from_user = new Scanner(System.in);
            boolean done = false;
            String msg_to_send;
            String msg_received;

            while (!done)
            {
                int choice;
                System.out.println();
                for (int i = 0; i < 3; i++)
                {
                    Thread.sleep(1000);
                    System.out.print(" . ");
                }
                System.out.println("\n");
                System.out.println("_____ACHIEVEMENT_UNLOCKED_____");
                System.out.println("\nWelcome " + current_user.getUsername() + "!!\n");
                System.out.println("1) Add a new videogame to your library");
                System.out.println("2) Remove a videogame from your library");
                System.out.println("3) Unlock achievements");
                System.out.println("4) Show your library");
                System.out.println("5) Show your stats");
                System.out.println("6) Show community stats");
                System.out.println("7) Save changes");
                System.out.println("0) Log out");
                System.out.println(">>>");
                choice = from_user.nextInt();
                from_user.nextLine(); //Enter

                switch (choice)
                {
                    //ADD VIDEOGAME TO LIBRARY
                    case 1:
                        System.out.println("Insert title: ");
                        msg_to_send = from_user.nextLine();
                        String title = msg_to_send;
                        to_server.println("SEARCH_VIDEOGAME \n"+msg_to_send);
                        to_server.flush();
                        msg_received = from_server.nextLine();
                        if (msg_received.equals("VIDEOGAME_FOUND"))
                        {
                            System.out.println("Videogame found on our server, loading into your library...");
                            Videogame vg = loadVideogameFromServer();
                            current_user.addVideogame(vg);
                            for (int i = 0; i < 3; i++)
                            {
                                Thread.sleep(1000);
                                System.out.print(" . ");
                            }
                            System.out.println("Added videogame:\n"+vg);
                        }
                        else if (msg_received.equals("VIDEOGAME_NOT_FOUND"))
                        {
                            System.out.println("Videogame not found on server...");
                            System.out.println("Please add all information about it");
                            System.out.println();
                            for (int i = 0; i < 3; i++)
                            {
                                Thread.sleep(1000);
                                System.out.print(" . ");
                            }
                            Videogame vg = menuAddVideogame(current_user, title);
                            current_user.addVideogame(vg);
                            System.out.println("\n\nAdded videogame:"+vg);
                        }
                        else
                        {
                            System.out.println("An error occurred...\nTry again!");
                        }
                        break;

                    //DELETE VIDEOGAME FROM LIBRARY
                    case 2:
                        System.out.println();
                        System.out.println("Your game: ");
                        printVideogames();
                        System.out.println();
                        System.out.println("Insert the game to remove: ");
                        String name = from_user.nextLine();
                        Videogame v = current_user.findVideogame(name);
                        if (v == null)
                        {
                            System.out.println("\nGame not found in your library!\n");
                        }
                        else
                        {
                            current_user.removeVideogame(v);
                            System.out.println("\n"+name+" removed!\n");
                        }
                        break;

                    //UNLOCK ACHIEVEMENTS
                    case 3:
                        System.out.println();
                        System.out.println("Videogame on your library: ");
                        printVideogames();
                        System.out.println();
                        System.out.println("Insert Videogame title: ");
                        title = from_user.nextLine();
                        Videogame vg = current_user.findVideogame(title);
                        if(vg == null)
                        {
                            System.out.println("Videogame not found in your library...");
                            break;
                        }
                        else
                        {
                            while(true)
                            {
                                System.out.println();
                                System.out.println("Achiements for " + vg.getTitle() + ": ");
                                printAchievements(vg);
                                System.out.println();
                                System.out.println("Insert name of achievement to unlock or \"0\" to quit: ");
                                String a_name = from_user.nextLine();
                                if (a_name.equals("0")) break;
                                Achievement ac = current_user.findAchievement(vg, a_name);
                                if (ac == null)
                                {
                                    System.out.println("\nAchievement not found..");
                                }
                                else
                                {
                                    if (ac.isUnlocked())
                                        System.out.println("\nAchievement already unlocked");
                                    else
                                    {
                                        ac.setUnlocked(true);
                                        current_user.updateAchievement(ac, vg);
                                    }
                                }
                            }
                        }
                        break;

                    //SHOW LIBRARY
                    case 4:
                        System.out.println();
                        System.out.println(current_user.printLibrary());
                        System.out.println();
                        break;

                    //SHOW CURRENT USER STATS
                    case 5:
                        System.out.println();
                        menuPrintLocalStats();
                        System.out.println();
                        break;

                    //SHOW COMMUNITY STATS
                    case 6:
                        System.out.println();
                        to_server.println("GET_STATS");
                        to_server.flush();
                        StatsArchive user_stats = loadStatsFromServer();
                        printAchievementsStats(user_stats);
                        System.out.println();
                        break;

                    //SAVE CHANGES
                    case 7:
                        to_server.println("UPDATE_INFORMATION");
                        to_server.flush();
                        updateUserInfo();
                        System.out.println();
                        System.out.println("Changes saved.\n");
                        break;

                    //LOG OUT
                    case 0:
                        done = true;
                        break;

                    default:
                        System.out.println("Invalid choice!!\n\n");
                        break;

                }
            }
        }
        catch (InputMismatchException e)
        {
            System.out.println("Invalid choice!!\n\n");
            e.printStackTrace();
            System.exit(1);
        }
        catch (UnknownHostException e)
        {
            System.out.println("Connection error!!\n\n");
            e.printStackTrace();
            System.exit(1);
        }
        catch (IOException e)
        {
            System.out.println("Input/Output error!!\n\n");
            e.printStackTrace();
            System.exit(1);
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Scanner error!!\n\n");
            e.printStackTrace();
            System.exit(1);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }

    //Load all information of the user that is logged in
    private User loadUserFromServer() throws IOException
    {
        Scanner from_server = new Scanner(client_socket.getInputStream());

        String[] info = new String[5]; int i_info; boolean b_info;
        User u; Person p; Videogame v; Achievement a; Platform pf;

        info[0] = from_server.nextLine(); //username
        info[1] = from_server.nextLine(); //password
        info[2] = from_server.nextLine(); //name
        info[3] = from_server.nextLine(); //surname
        info[4] = from_server.nextLine(); //email

        u = new User(info[0], info[1]);
        p = new Person(info[2], info[3], info[4]);
        u.setInformation(p);

        while (true)
        {
            info[0] = from_server.nextLine(); //title or message that ends the stream
            if (info[0].equals("END_VIDEOGAMES")) break;
            info[1] = from_server.nextLine(); //description
            i_info = Integer.parseInt(from_server.nextLine()); //rating
            info[2] = from_server.nextLine(); //softwareHouse

            v = new Videogame(info[0], info[1], i_info, info[2]);
            u.addVideogame(v);

            while (true)
            {
                info[0] = from_server.nextLine(); //name or message that ends the stream
                if (info[0].equals("END_PLATFORMS")) break;
                info[1] = from_server.nextLine(); //company
                i_info = Integer.parseInt(from_server.nextLine()); //releaseYear

                pf = new Platform(info[0], info[1], i_info);
                v.addPlatform(pf);

            }

            while (true)
            {
                info[0] = from_server.nextLine(); //name or message that ends the stream
                if (info[0].equals("END_ACHIEVEMENTS")) break;
                info[1] = from_server.nextLine(); //name of the videogame
                b_info = Boolean.parseBoolean(from_server.nextLine()); //unlocked
                info[2] = from_server.nextLine(); //description

                a = new Achievement(info[0], info[1], b_info, info[2]);
                v.addAchievement(a);

            }
        }
        return u;
    }

    private String menuLogin()
    {
        StringBuilder msg = new StringBuilder();
        msg.append("LOGIN ");
        Scanner in = new Scanner(System.in);
        System.out.println("Insert username: ");
        msg.append(in.nextLine()+" ");
        System.out.println("Insert password: ");
        msg.append(in.nextLine());

        return msg.toString();
    }

    private String menuSignUp()
    {
        StringBuilder msg = new StringBuilder();
        msg.append("SIGNUP ");
        Scanner in = new Scanner(System.in);
        System.out.println("Insert username: ");
        msg.append(in.nextLine()+" ");
        System.out.println("Insert password: ");
        msg.append(in.nextLine()+" ");
        System.out.println("Insert name: ");
        msg.append(in.nextLine()+" ");
        System.out.println("Insert surname: ");
        msg.append(in.nextLine()+" ");
        System.out.println("Insert email: ");
        msg.append(in.nextLine());

        return msg.toString();
    }

    //Send the user information from the client after a command on the menuStart() of the Client
    private void updateUserInfo() throws IOException
    {
        var to_server = new PrintWriter(client_socket.getOutputStream());

        to_server.println(current_user.getUsername());
        to_server.println(current_user.getPassword());
        to_server.println(current_user.printPerson());

        for(Videogame vg : current_user.getLibrary())
        {
            to_server.println(vg.getTitle());
            to_server.println(vg.getDescription());
            to_server.println(vg.getRating());
            to_server.println(vg.getSoftwareHouse());

            for(Platform pf : vg.getPlatformList())
            {
                to_server.println(pf.getName());
                to_server.println(pf.getCompany());
                to_server.println(pf.getReleaseYear());
            }
            to_server.println("END_PLATFORMS");

            for(Achievement a : vg.getAchievements())
            {
                to_server.println(a.getName());
                to_server.println(a.getVideogame_name());
                to_server.println(a.isUnlocked());
                to_server.println(a.getDescription());
            }
            to_server.println("END_ACHIEVEMENTS");
        }
        to_server.println("END_VIDEOGAMES");
        to_server.flush();


    }

    //load a game from server with all achievement locked
    private Videogame loadVideogameFromServer() throws IOException
    {
        Scanner from_server = new Scanner(client_socket.getInputStream());

        String[] info = new String[3]; int i_info;
        Videogame v; Achievement a; Platform pf;

        info[0] = from_server.nextLine(); //title or message that ends the stream
        info[1] = from_server.nextLine(); //description
        i_info = Integer.parseInt(from_server.nextLine()); //rating
        info[2] = from_server.nextLine(); //softwareHouse

        v = new Videogame(info[0], info[1], i_info, info[2]);


        while (true)
        {
            info[0] = from_server.nextLine(); //name or message that ends the stream
            if (info[0].equals("END_PLATFORMS")) break;
            info[1] = from_server.nextLine(); //company
            i_info = Integer.parseInt(from_server.nextLine()); //releaseYear

            pf = new Platform(info[0], info[1], i_info);
            v.addPlatform(pf);

        }

        while (true)
        {
            info[0] = from_server.nextLine(); //name or message that ends the stream
            if (info[0].equals("END_ACHIEVEMENTS")) break;
            info[1] = from_server.nextLine(); //name of the videogame
            info[2] = from_server.nextLine(); //description

            a = new Achievement(info[0], info[1], false, info[2]);
            v.addAchievement(a);

        }
        return v;
    }

    private Videogame menuAddVideogame(User user, String title)
    {
        String info[] = new String[3];
        int i_info; boolean b_info;
        Videogame vg;
        Scanner from_user = new Scanner(System.in);

        info[0] = title;
        System.out.println("Insert description: ");
        info[1] = from_user.nextLine();
        System.out.println("Insert rating: ");
        i_info = from_user.nextInt();
        from_user.nextLine(); //Enter
        System.out.println("Insert software house");
        info[2] = from_user.nextLine();

        vg = new Videogame(info[0], info[1], i_info, info[2]);

        while (true)
        {
            System.out.println("Insert platform name (0 to quit): ");
            info[0] = from_user.nextLine();
            if (info[0].equals("0")) break;
            System.out.println("Insert company: ");
            info[1] = from_user.nextLine();
            System.out.println("Insert the year of release: ");
            i_info = from_user.nextInt();
            from_user.nextLine(); //Enter

            vg.addPlatform(new Platform(info[0], info[1], i_info));
        }

        while (true)
        {
            System.out.println("Insert achievement name (0 to quit): ");
            info[0] = from_user.nextLine();
            if (info[0].equals("0")) break;
            System.out.println("Insert achievement description: ");
            info[1] = from_user.nextLine();
            System.out.println("Insert '1' to unlock ('0' or others left it locked): ");
            i_info = from_user.nextInt();
            from_user.nextLine(); //Enter
            if (i_info == 1) b_info = true;
            else b_info = false;

            vg.addAchievement(new Achievement(info[0], vg.getTitle(), b_info, info[1]));
        }
        return vg;
    }

    private void printVideogames()
    {
        for(Videogame vg :current_user.getLibrary())
            System.out.println(vg.getTitle());
    }

    private void printAchievements(Videogame vg)
    {
        for(Achievement ac : vg.getAchievements())
        {
            if (ac.isUnlocked())
                System.out.println("\n[Unlocked] " + ac.getName());
            else
                System.out.println("\n[Locked] " + ac.getName());

        }

    }

    //load statistic data from the server
    private StatsArchive loadStatsFromServer() throws IOException
    {
        Scanner from_server = new Scanner(client_socket.getInputStream());

        String[] info = new String[3]; boolean b_info;
        int[] i_info = new int[2]; double d_info;
        Achievement ac; AchievementStats as;
        StatsArchive archive = new StatsArchive();

        while (true)
        {
            info[0] = from_server.nextLine(); //name or message that ends the stream
            if (info[0].equals("END_ACHIEVEMENTS")) break;
            info[1] = from_server.nextLine(); //name of the videogame
            info[2] = from_server.nextLine(); //description
            i_info[0] = Integer.parseInt(from_server.nextLine()); //n_unlocked
            i_info[1] = Integer.parseInt(from_server.nextLine()); //n_total
            d_info = Double.parseDouble(from_server.nextLine()); //unlock_percentage

            ac = new Achievement(info[0], info[1],false, info[2]);
            as = new AchievementStats(ac, i_info[0], i_info[1], d_info);
            archive.add(as);
        }
        return archive;
    }

    //Print statistic data about the current user
    public void menuPrintLocalStats()
    {
        for(Videogame vg : current_user.getLibrary())
        {
            int ac_count = 0; int ac_unlocked_count = 0;
            double percentage;

            for (Achievement ac : vg.getAchievements())
            {
                ac_count++;
                if (ac.isUnlocked())
                    ac_unlocked_count++;
            }
            percentage = 100 * ((double) ac_unlocked_count) / ((double) ac_count);
            System.out.println("VIDEOGAME: "+vg.getTitle());
            System.out.println("Unlocked achievements: "+ac_unlocked_count);
            System.out.println("Total achievements: "+ac_count);
            System.out.println(percentage+" % completed");
            System.out.println();
        }
    }

    private void printAchievementsStats(StatsArchive statsArchive)
    {
        for (AchievementStats as : statsArchive.getArchive())
        {
            System.out.println("ACHIEVEMENT: "+as.getAchievement().getName());
            System.out.println("VIDEOGAME: "+as.getAchievement().getVideogame_name());
            System.out.println("Unlocked by: "+as.getN_unlocked()+" users");
            System.out.println("Videogame own by: "+as.getN_total()+" users");
            System.out.println("Unlock percentage: "+as.getUnlock_percentage()*100+" %");
            System.out.println(as.getDifficulty());
            System.out.println();
        }
    }
}