import java.util.HashSet;
import java.util.Objects;

public class User
{
    private String username;
    private String password;
    private Person information;
    private HashSet<Videogame> library = new HashSet<>();

    public User(String u, String p)
    {
        this.username = u;
        this.password = p;
    }

    public Person getInformation()
    {
        return information;
    }

    public void setInformation(Person information)
    {
        this.information = information;
    }

    public HashSet<Videogame> getLibrary()
    {
        return library;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public boolean equals(Object o)
    {
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("\n\nUSER");
        msg.append("\nUsername: "+username);
        msg.append("\nPassword: "+password);
        msg.append("\n");
        msg.append(information.toString());
        return msg.toString();
    }

    public void addVideogame(Videogame videogame)
    {
        library.add(videogame);
    }

    public String printPerson()
    {
        return information.getName() + "\n" +
                information.getSurname() +"\n" +
                information.getEmail();
    }

    public String printLibrary()
    {
        StringBuilder str = new StringBuilder();
        for (Videogame tmp : library) str.append(tmp);
        return str.toString();
    }

    public Videogame findVideogame(String title)
    {
        for(Videogame vg : library)
        {
            if (vg.getTitle().equalsIgnoreCase(title))
                return vg;
        }
        return null;
    }

    public Achievement findAchievement(Videogame vg, String a_name)
    {
        for (Videogame tmp : library)
        {
            if (tmp.equals(vg))
                for (Achievement ac : tmp.getAchievements())
                    if (ac.getName().equalsIgnoreCase(a_name)) return ac;
        }
        return null;
    }

    public void updateAchievement(Achievement ac, Videogame vg)
    {
        for(Videogame tmp : library)
            if(tmp.equals(vg))
            {
                tmp.getAchievements().remove(ac);
                tmp.getAchievements().add(ac);
            }
    }
}
