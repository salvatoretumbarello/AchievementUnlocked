import java.io.*;

public class StatusSaver implements Runnable
{
    private UserArchive userArchive;
    private StatsArchive statsArchive;

    public StatusSaver(UserArchive userArchive, StatsArchive statsArchive)
    {
        this.userArchive = userArchive;
        this.statsArchive = statsArchive;
    }

    @Override
    public void run()
    {
        while (true) {
            try {
                var fos_ua = new FileOutputStream("user_archive.dat");
                var os_ua = new ObjectOutputStream(fos_ua);

                var fos_sa = new FileOutputStream("stats_archive.dat");
                var os_sa = new ObjectOutputStream(fos_sa);

                os_ua.writeObject(userArchive);
                os_sa.writeObject(statsArchive);

                os_ua.close();
                os_sa.close();

                System.out.println("-- StatusSaver: data saved !!");
                Thread.sleep(20000);

            } catch (FileNotFoundException e) {
                System.out.println("-- StatusSaver: Error on saving data !!");
                e.printStackTrace();
                System.exit(-1);
            } catch (IOException e) {
                System.out.println("-- StatusSaver: Error on saving data !!");
                e.printStackTrace();
                System.exit(-1);
            } catch (InterruptedException e) {
                System.out.println("-- StatusSaver: Error on saving data !!");
                e.printStackTrace();
                System.exit(-1);
            }
        }

    }
}