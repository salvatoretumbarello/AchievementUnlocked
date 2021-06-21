import java.util.Objects;

public class Game
{
    protected String title;
    protected String description;
    protected int rating; //The minimum age suggested for the play

    public Game(String title, String description, int rating)
    {
        this.title = title;
        this.description = description;
        this.rating = rating;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getRating()
    {
        return rating;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(title, game.title);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(title);
    }
}
