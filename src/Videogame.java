import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Videogame extends Game
{
    private String softwareHouse;
    private Date releaseDate;
    private ArrayList<Platform> platformList = new ArrayList<>();
    private HashSet<Achievement> achievements = new HashSet<>();

    public String getSoftwareHouse()
    {
        return softwareHouse;
    }

    public void setSoftwareHouse(String softwareHouse)
    {
        this.softwareHouse = softwareHouse;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

}
