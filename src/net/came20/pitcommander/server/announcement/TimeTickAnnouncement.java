package net.came20.pitcommander.server.announcement;

import net.came20.pitcommander.server.container.MatchContainer;

/**
 * Created by cameronearle on 4/30/17.
 */
public class TimeTickAnnouncement implements Announcement {
    private long newTime;
    private long timeToZero;

    public TimeTickAnnouncement(long newTime) {
        this.newTime = newTime;
        timeToZero = MatchContainer.getInstance().getTimeToZero();
    }

    public long getNewTime() {
        return newTime;
    }

    @Override
    public String getName() {
        return "TimeTick";
    }
}
