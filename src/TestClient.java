public class TestClient
{
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("Error, usage: java TestClient <IP> <port>");
            System.exit(-1);
        }

        String ip = args[0];
        int port = Integer.parseInt(args[1]);

        Client my_client = new Client(ip, port);
        my_client.start();
    }
}