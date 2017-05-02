package net.came20.pitcommander.server.container;

/**
 * Created by cameronearle on 4/30/17.
 */
public class GeneralContainer extends Container {
    private transient static GeneralContainer ourInstance = new GeneralContainer();
    public static GeneralContainer getInstance() {
        return ourInstance;
    }

    //VALUES
    private int teamNumber = 0;
    private String eventCode = "?";

    //Accessors
    public int getTeamNumber() {
        synchronized (lock) {
            return teamNumber;
        }
    }

    public void setTeamNumber(int teamNumber) {
        synchronized (lock) {
            this.teamNumber = teamNumber;
            fireUpdate();
        }
    }

    public String getEventCode() {
        synchronized (lock) {
            return eventCode;
        }
    }

    public void setEventCode(String eventCode) {
        synchronized (lock) {
            this.eventCode = eventCode;
            fireUpdate();
        }
    }

    private GeneralContainer() {
    }

    @Override
    public String getName() {
        return "General";
    }
}
