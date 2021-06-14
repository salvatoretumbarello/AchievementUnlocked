public class TestServer
{
    public static void main(String args[])
    {
        if (args.length != 1)
        {
            System.out.println("Error: usage is java TestServer <port>");
            System.exit(-1);
        }

        System.out.println("Starting server on port "+args[0]);

        Server my_server = new Server(Integer.parseInt(args[0]));
        my_server.start();

    }
}