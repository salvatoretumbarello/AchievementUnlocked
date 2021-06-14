import java.util.HashSet;

public class User
{
    private String username;
    private String password;
    private Person information;
    private HashSet<Videogame> library = new HashSet<>();

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

}
