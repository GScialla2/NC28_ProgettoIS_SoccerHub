package Model;

public class Player extends User
{
    private String city;
    private String role;
    private String careerDescription;
    private String position;
    private double height;
    private double weight;
    private String preferredFoot;

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
    
    public String getPosition()
    {
        return position;
    }
    
    public double getHeight()
    {
        return height;
    }
    
    public double getWeight()
    {
        return weight;
    }
    
    public String getPreferredFoot()
    {
        return preferredFoot;
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
    
    public void setPosition(String position)
    {
        this.position = position;
    }
    
    public void setHeight(double height)
    {
        this.height = height;
    }
    
    public void setWeight(double weight)
    {
        this.weight = weight;
    }
    
    public void setPreferredFoot(String preferredFoot)
    {
        this.preferredFoot = preferredFoot;
    }
}
