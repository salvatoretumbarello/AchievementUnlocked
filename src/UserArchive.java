import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class UserArchive implements Serializable
{
    private ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> getUsers()
    {
        return users;
    }

    public void add(User user)
    {
        users.add(user);
    }

    public void updateUser(User user)
    {
        users.remove(user);
        users.add(user);
    }

    public User findUser(User someuser)
    {
        for (User user : users)
        {
            if (user.equals(someuser))
                return user;
        }
        return null;
    }

    public Videogame findVideogame(String title)
    {
        for(User user : users)
        {
            for(Videogame tmp : user.getLibrary())
            {
                if (tmp.getTitle().equalsIgnoreCase(title))
                    return tmp;
            }
        }
        return null;
    }
}
