package Model;

public class Coach extends User
{
    private String city;
    private String phone;
    private String careerDescription;

    public String getCity()
    {
        return city;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getCareerDescription()
    {
        return careerDescription;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public void setCareerDescription(String careerDescription)
    {
        this.careerDescription = careerDescription;
    }
}
