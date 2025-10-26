package Model;

import java.util.Date;

public class Tournament
{
    private int id;
    private Integer createdBy; // user id (coach) who created the tournament, nullable
    private String name;
    private String type;
    private String trophy;
    private int teamCount;
    private int matchCount;
    private Date startDate;
    private Date endDate;
    private String location;
    private String description;
    private String category;
    private String status;

    public int getId()
    {
        return id;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public String getTrophy()
    {
        return trophy;
    }

    public int getTeamCount()
    {
        return teamCount;
    }

    public int getMatchCount()
    {
        return matchCount;
    }
    
    public Date getStartDate()
    {
        return startDate;
    }
    
    public Date getEndDate()
    {
        return endDate;
    }
    
    public String getLocation()
    {
        return location;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public String getCategory()
    {
        return category;
    }
    
    public String getStatus()
    {
        return status;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setTrophy(String trophy)
    {
        this.trophy = trophy;
    }

    public void setTeamCount(int teamCount)
    {
        this.teamCount = teamCount;
    }

    public void setMatchCount(int matchCount)
    {
        this.matchCount = matchCount;
    }
    
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }
    
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }
    
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public void setCategory(String category)
    {
        this.category = category;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
}
