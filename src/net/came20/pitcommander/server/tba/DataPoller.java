package net.came20.pitcommander.server.tba;

import com.cpjd.main.TBA;
import com.cpjd.models.Event;
import com.cpjd.models.Match;
import net.came20.pitcommander.server.container.GeneralContainer;
import net.came20.pitcommander.server.container.MatchContainer;
import net.came20.pitcommander.server.util.EventSorter;
import net.came20.pitcommander.server.util.MatchSorter;
import net.came20.pitcommander.server.util.TimeTicker;

import java.time.Instant;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cameronearle on 5/1/17.
 */
public class DataPoller implements Runnable {
    private static transient DataPoller ourInstance = new DataPoller();
    public static DataPoller getInstance() {
        return ourInstance;
    }

    private Object lock = new Object();

    private TBA tba = new TBA();
    private int interval;
    private int teamNumber;

    public void setup(int teamNumber, int interval) {
        this.teamNumber = teamNumber;
        this.interval = interval;
        TBA.setID(Integer.toString(teamNumber), "PitCommander", "v1");
    }
    private DataPoller() {}

    private Event[] events;
    private Event currentEvent;
    private Match[] matches;

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            //Grab latest matches
            synchronized (lock) {
                events = EventSorter.sort(tba.getTeamEvents(teamNumber, Year.now().getValue())); //Grab the events
                if (events.length > 0) {
                    currentEvent = events[0];
                } else {
                    currentEvent = new Event();
                }

                GeneralContainer.getInstance().setEventCode(currentEvent.event_code);

                matches = tba.getTeamEventMatches(Year.now().getValue(), currentEvent.key, teamNumber);
                MatchContainer.getInstance().updateMatchList(matches);
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
