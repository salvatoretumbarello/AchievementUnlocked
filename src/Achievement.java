import java.util.Date;

public class Achievement
{
    private String name;
    private boolean unlocked;
    private Date unlockDate;
    private String description;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isUnlocked()
    {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked)
    {
        this.unlocked = unlocked;
    }

}
