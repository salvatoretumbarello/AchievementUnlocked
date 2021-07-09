import java.io.*;

public class TestServer {
    public static void main(String args[]) {
        try {
            if (args.length == 1) {
                System.out.println("Starting server on port " + args[0]);
                Server my_server = new Server(Integer.parseInt(args[0]));
                my_server.start();
            } else if (args.length == 3) {
                UserArchive ua = loadUserArchiveFromFile(args[1]);
                StatsArchive sa = loadStatsArchiveFromFile(args[2]);
                System.out.println("Starting server on port " + args[0]);
                Server my_server = new Server(Integer.parseInt(args[0]), ua, sa);
                my_server.start();
            } else {
                System.out.println("Error: usage is java TestServer <port>");
                System.out.println("For reload data: java TestServer <port> <archive.dat>");
                System.exit(-1);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cannot load archive of users");
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Cannot load archive of users");
            e.printStackTrace();
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot load archive of users");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static UserArchive loadUserArchiveFromFile(String filename) throws IOException, ClassNotFoundException {
        var fin = new FileInputStream(filename);
        var ois = new ObjectInputStream(fin);

        UserArchive archive = (UserArchive) ois.readObject();

        ois.close();
        System.out.println("Users loaded succesfully");

        return archive;
    }

    public static StatsArchive loadStatsArchiveFromFile(String filename) throws IOException, ClassNotFoundException {
        var fin = new FileInputStream(filename);
        var ois = new ObjectInputStream(fin);

        StatsArchive archive = (StatsArchive) ois.readObject();

        ois.close();
        System.out.println("Stats loaded succesfully");
        return archive;
    }
}

