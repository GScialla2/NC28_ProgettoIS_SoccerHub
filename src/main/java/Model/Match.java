package Model;

import java.util.Date;

public class Match
{
    private int id;
    private int tournamentId;
    private Integer createdBy; // user id (coach) who created the match, nullable
    private String homeTeam;
    private String awayTeam;
    private Date matchDate;
    private String location;
    private String category;
    private String type;
    private String status;
    private int homeScore;
    private int awayScore;

    public int getId()
    {
        return id;
    }

    public int getTournamentId()
    {
        return tournamentId;
    }

    public Integer getCreatedBy()
    {
        return createdBy;
    }

    public String getHomeTeam()
    {
        return homeTeam;
    }

    public String getAwayTeam()
    {
        return awayTeam;
    }

    public Date getMatchDate()
    {
        return matchDate;
    }

    public String getLocation()
    {
        return location;
    }

    public String getCategory()
    {
        return category;
    }

    public String getType()
    {
        return type;
    }

    public String getStatus()
    {
        return status;
    }

    public int getHomeScore()
    {
        return homeScore;
    }

    public int getAwayScore()
    {
        return awayScore;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setTournamentId(int tournamentId)
    {
        this.tournamentId = tournamentId;
    }

    public void setCreatedBy(Integer createdBy)
    {
        this.createdBy = createdBy;
    }

    public void setHomeTeam(String homeTeam)
    {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam)
    {
        this.awayTeam = awayTeam;
    }

    public void setMatchDate(Date matchDate)
    {
        this.matchDate = matchDate;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setHomeScore(int homeScore)
    {
        this.homeScore = homeScore;
    }

    public void setAwayScore(int awayScore)
    {
        this.awayScore = awayScore;
    }
}
