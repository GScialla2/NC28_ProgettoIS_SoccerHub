package Model;

public class Tournament
{
    private int id;
    private String name;
    private String type;
    private String trophy;
    private int teamCount;
    private int matchCount;

    public int getId()
    {
        return id;
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

    public void setId(int id)
    {
        this.id = id;
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
}
