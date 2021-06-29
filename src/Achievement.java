import java.util.Date;
import java.util.Objects;

public class Achievement
{
    private String name;
    private String videogame_name;
    private String description;
    private boolean unlocked;


    public Achievement(String name, String videogame_name, boolean unlocked, String description)
    {
        this.name = name;
        this.videogame_name = videogame_name;
        this.unlocked = unlocked;
        this.description = description;
    }

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

    public String getVideogame_name() {
        return videogame_name;
    }

    public void setVideogame_name(String videogame_name) {
        this.videogame_name = videogame_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Achievement that = (Achievement) o;
        return Objects.equals(name, that.name) && Objects.equals(videogame_name, that.videogame_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, videogame_name);
    }

    @Override
    public String toString()
    {
        StringBuilder msg = new StringBuilder();
        msg.append("\n\nACHIEVEMENT: "+name);
        msg.append("\nVideogame title: "+videogame_name);
        msg.append("\nDescription: "+description);
        if (isUnlocked())
            msg.append("\n[Unlocked] ");
        else
            msg.append("\n[Locked] ");
        return msg.toString();
    }
}
