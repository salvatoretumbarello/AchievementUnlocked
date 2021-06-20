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
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", information=" + information +
                ", library=" + library +
                '}';
    }

    public String printPerson()
    {
        return information.getName() + "\n" +
                information.getSurname() +"\n" +
                information.getEmail();
    }

    public void addVideogame(Videogame videogame)
    {
        library.add(videogame);
    }
}
