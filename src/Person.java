public class Person
{
    private String name;
    private String surname;
    private String email;

    public Person(String name, String surname, String email)
    {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        StringBuilder msg = new StringBuilder();
        msg.append("\nINFORMATION");
        msg.append("\nName: " + name);
        msg.append("\nSurname: " + surname);
        msg.append("\nemail: " + email);
        return msg.toString();
    }
}
