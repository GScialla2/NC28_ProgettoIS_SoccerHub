package Model;

import java.util.Date;

public class Match
{
    private int id;
    private String opponent;
    private Date matchDate;
    private String matchTime;
    private String type;
    private String stadium;

    public int getId()
    {
        return id;
    }

    public String getOpponent()
    {
        return opponent;
    }

    public Date getMatchDate()
    {
        return matchDate;
    }

    public String getMatchTime()
    {
        return matchTime;
    }

    public String getType()
    {
        return type;
    }

    public String getStadium()
    {
        return stadium;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setOpponent(String opponent)
    {
        this.opponent = opponent;
    }

    public void setMatchDate(Date matchDate)
    {
        this.matchDate = matchDate;
    }

    public void setMatchTime(String matchTime)
    {
        this.matchTime = matchTime;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setStadium(String stadium)
    {
        this.stadium = stadium;
    }
}
