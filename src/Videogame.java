import java.util.ArrayList;
import java.util.HashSet;

public class Videogame extends Game
{
    private String softwareHouse;
    private ArrayList<Platform> platformList = new ArrayList<>();
    private HashSet<Achievement> achievements = new HashSet<>();

    public Videogame(String title, String description, int rating, String softwareHouse)
    {
        super(title, description, rating);
        this.softwareHouse = softwareHouse;
    }

    public ArrayList<Platform> getPlatformList()
    {
        return platformList;
    }

    public HashSet<Achievement> getAchievements()
    {
        return achievements;
    }

    public String getSoftwareHouse()
    {
        return softwareHouse;
    }

    public void setSoftwareHouse(String softwareHouse)
    {
        this.softwareHouse = softwareHouse;
    }

    @Override
    public String toString() {
        return "Videogame{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                ", softwareHouse='" + softwareHouse + '\'' +
                ", platformList=" + platformList +
                ", achievements=" + achievements +
                '}';
    }

    public void addPlatform(Platform platform)
    {
        platformList.add(platform);
    }

    public void addAchievement(Achievement achievement)
    {
        achievements.add(achievement);
    }
}
