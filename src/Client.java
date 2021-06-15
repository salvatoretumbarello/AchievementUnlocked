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
                                menu();
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

        public void menu()
        {
            //Here will be implemented another menu
        }

        public String menuLogin()
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

        public String menuSignUp()
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
