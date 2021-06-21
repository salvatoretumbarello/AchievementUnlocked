import java.util.ArrayList;
import java.util.HashSet;

public class UserArchive
{
    ArrayList<User> users = new ArrayList<>();

    public void add(User user)
    {
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
        Videogame vg = null;
        for(User user : users)
        {
            vg = user.findVideogame(title);
            if (vg != null) break;
        }
        return vg;
    }
}
