package Model;

public class Coach extends User
{
    private String city;
    private String phone;
    private String careerDescription;
    private String licenseNumber;
    private int experienceYears;
    private String specialization;

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
    
    public String getLicenseNumber()
    {
        return licenseNumber;
    }
    
    public int getExperienceYears()
    {
        return experienceYears;
    }
    
    public String getSpecialization()
    {
        return specialization;
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
    
    public void setLicenseNumber(String licenseNumber)
    {
        this.licenseNumber = licenseNumber;
    }
    
    public void setExperienceYears(int experienceYears)
    {
        this.experienceYears = experienceYears;
    }
    
    public void setSpecialization(String specialization)
    {
        this.specialization = specialization;
    }
}
