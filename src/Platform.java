import java.util.ArrayList;
import java.util.Objects;

public class Platform
{
    private String name;
    private String company;
    private int releaseYear;

    public Platform(String name, String company, int releaseYear)
    {
        this.name = name;
        this.company = company;
        this.releaseYear = releaseYear;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public int getReleaseYear()
    {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Platform platform = (Platform) o;
        return Objects.equals(name, platform.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }

    @Override
    public String toString()
    {
        StringBuilder msg = new StringBuilder();
        msg.append("\n\nPLATFORM: "+name);
        msg.append("\nCompany: "+company);
        msg.append("\nRelease year: "+releaseYear);
        return msg.toString();
    }
}
