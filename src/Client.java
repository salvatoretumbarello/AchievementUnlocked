import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

    public class Client
    {
        String address_ip;
        int port;
        Socket client_socket;

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
                            //To be completed
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

                                User current_user = loadUserFromServer();

                                System.out.println("Loaded: "+current_user);
                                menuStart(current_user);

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
                                System.out.println("Now you can login");
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
            }
            catch (UnknownHostException e)
            {
                System.out.println("Connection error!!\n\n");
                e.printStackTrace();
            }
            catch (IOException e)
            {
                System.out.println("Input/Output error!!\n\n");
                e.printStackTrace();
            }
        }

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
                i_info = from_server.nextInt(); //rating
                info[2] = from_server.nextLine(); //softwareHouse

                v = new Videogame(info[0], info[1], i_info, info[2]);
                u.addVideogame(v);

                while (true)
                {
                    info[0] = from_server.nextLine(); //name or message that ends the stream
                    if (info[0].equals("END_PLATFORMS")) break;
                    info[1] = from_server.nextLine(); //company
                    i_info = from_server.nextInt(); //releaseYear

                    pf = new Platform(info[0], info[1], i_info);
                    v.addPlatform(pf);

                }

                while (true)
                {
                    info[0] = from_server.nextLine(); //name or message that ends the stream
                    if (info[0].equals("END ACHIEVEMENTS")) break;
                    info[1] = from_server.nextLine(); //name of the videogame
                    b_info = from_server.hasNextBoolean(); //unlocked
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

        private void menuStart(User user)
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
                    System.out.println("_____ACHIEVEMENT_UNLOCKED_____");
                    System.out.println("\nWelcome " + user.getUsername() + "!\n");
                    System.out.println("1) Add a new videogame to library");
                    System.out.println("2) unlock achievements");
                    System.out.println("0) log out");
                    System.out.println(">>>");
                    choice = from_user.nextInt();
                    from_user.nextLine(); //Enter

                    switch (choice) {
                        case 1:
                            System.out.println("Insert title: ");
                            msg_to_send = from_user.nextLine();
                            to_server.println("SEARCH_VIDEOGAME \n"+msg_to_send);
                            to_server.flush();
                            msg_received = from_server.nextLine();
                            if (msg_received.equals("VIDEOGAME_FOUND"))
                            {
                                    Videogame vg = loadVideogameFromServer();
                                    user.addVideogame(vg);
                                    System.out.println("Added videogame:\n"+vg);
                            }
                            else if (msg_received.equals("VIDEOGAME_NOT_FOUND"))
                            {
                                System.out.println("Videogame not found on our server..");
                                System.out.println("Please help us to grow by adding all informations about it");
                                System.out.println();
                                Videogame vg = menuAddVideogame(user);
                                user.addVideogame(vg);
                                System.out.println("Added videogame:\n"+vg);
                            }
                            else
                            {
                                System.out.println("An error occurred...\nTry again!");
                            }
                            break;

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
            }
            catch (UnknownHostException e)
            {
                System.out.println("Connection error!!\n\n");
                e.printStackTrace();
            }
            catch (IOException e)
            {
                System.out.println("Input/Output error!!\n\n");
                e.printStackTrace();
            }

        }

        private Videogame loadVideogameFromServer() throws IOException
        {
            Scanner from_server = new Scanner(client_socket.getInputStream());

            String[] info = new String[3]; int i_info;
            Videogame v; Achievement a; Platform pf;

            info[0] = from_server.nextLine(); //title or message that ends the stream
            info[1] = from_server.nextLine(); //description
            i_info = from_server.nextInt(); //rating
            info[2] = from_server.nextLine(); //softwareHouse

            v = new Videogame(info[0], info[1], i_info, info[2]);


            while (true)
            {
                info[0] = from_server.nextLine(); //name or message that ends the stream
                if (info[0].equals("END_PLATFORMS")) break;
                info[1] = from_server.nextLine(); //company
                i_info = from_server.nextInt(); //releaseYear

                pf = new Platform(info[0], info[1], i_info);
                v.addPlatform(pf);

            }

            while (true)
            {
                info[0] = from_server.nextLine(); //name or message that ends the stream
                if (info[0].equals("END ACHIEVEMENTS")) break;
                info[1] = from_server.nextLine(); //name of the videogame
                info[2] = from_server.nextLine(); //description

                a = new Achievement(info[0], info[1], false, info[2]);
                v.addAchievement(a);

            }
            return v;
        }

        private Videogame menuAddVideogame(User user)
        {
            String info[] = new String[3];
            int i_info; boolean b_info;
            Videogame vg;
            Scanner from_user = new Scanner(System.in);

            System.out.println("Insert title: ");
            info[0] = from_user.nextLine();
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
                System.out.println("Insert '1' to unlock (other commands left it locked): ");
                i_info = from_user.nextInt();
                from_user.nextLine(); //Enter
                if (i_info == 1) b_info = true;
                else b_info = false;

                vg.addAchievement(new Achievement(info[0], vg.getTitle(), b_info, info[1]));
            }
            return vg;
        }
    }
