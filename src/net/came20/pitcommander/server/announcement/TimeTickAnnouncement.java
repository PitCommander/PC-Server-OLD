package net.came20.pitcommander.server.announcement;

/**
 * Created by cameronearle on 4/30/17.
 */
public class TimeTickAnnouncement implements Announcement {
    private long newTime;

    public TimeTickAnnouncement(long newTime) {
        this.newTime = newTime;
    }

    public long getNewTime() {
        return newTime;
    }

    @Override
    public String getName() {
        return "TimeTick";
    }
}
