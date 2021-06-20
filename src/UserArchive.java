import java.util.ArrayList;

public class UserArchive
{
    ArrayList<User> users = new ArrayList<>();

    public User findUser(User someuser)
    {
        for (User user : users)
        {
            if (user.equals(someuser))
                return user;
        }
        return null;
    }

    public void add(User user)
    {
        users.add(user);
    }
}
