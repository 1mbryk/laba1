package entity;

public class ChatUser
{
    private String name;
    private long last_interaction_time;
    private String session_id;

    public ChatUser(String name, long last_interaction_time, String session_id)
    {
        super();
        this.name = name;
        this.last_interaction_time = last_interaction_time;
        this.session_id = session_id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getLastInteractionTime()
    {
        return last_interaction_time;
    }

    public void setLastInteractionTime(long last_interaction_time)
    {
        this.last_interaction_time = last_interaction_time;
    }

    public String getSessionId()
    {
        return session_id;
    }

    public void setSessionId(String session_id)
    {
        this.session_id = session_id;
    }
}
