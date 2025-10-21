package Model;

public class Fan extends User
{
    private String birthCity;
    private String residenceCity;
    private String province;
    private String street;
    private String favoriteTeam;
    private String phone;
    private String membershipLevel;

    public String getBirthCity()
    {
        return birthCity;
    }

    public String getResidenceCity()
    {
        return residenceCity;
    }

    public String getProvince()
    {
        return province;
    }

    public String getStreet()
    {
        return street;
    }

    public String getFavoriteTeam()
    {
        return favoriteTeam;
    }

    public String getPhone()
    {
        return phone;
    }
    
    public String getMembershipLevel()
    {
        return membershipLevel;
    }

    public void setBirthCity(String birthCity)
    {
        this.birthCity = birthCity;
    }

    public void setResidenceCity(String residenceCity)
    {
        this.residenceCity = residenceCity;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public void setFavoriteTeam(String favoriteTeam)
    {
        this.favoriteTeam = favoriteTeam;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public void setMembershipLevel(String membershipLevel)
    {
        this.membershipLevel = membershipLevel;
    }
}
