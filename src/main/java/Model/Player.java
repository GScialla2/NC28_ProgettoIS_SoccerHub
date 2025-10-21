package Model;

public class Player extends User
{
    private String city;
    private String role;
    private String careerDescription;

    public String getCity()
    {
        return city;
    }

    public String getRole()
    {
        return role;
    }

    public String getCareerDescription()
    {
        return careerDescription;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public void setCareerDescription(String careerDescription)
    {
        this.careerDescription = careerDescription;
    }
}
