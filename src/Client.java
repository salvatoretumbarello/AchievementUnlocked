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

        private void menuStart(User user)
        {
            //Here will be implemented another menu
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
}
