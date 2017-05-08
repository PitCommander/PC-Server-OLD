package net.came20.pitcommander.server.container;

import com.cpjd.models.Match;
import net.came20.pitcommander.server.Server;
import net.came20.pitcommander.server.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by cameronearle on 5/2/17.
 */
public class MatchContainer extends Container {
    private transient static MatchContainer ourInstance = new MatchContainer();

    public static MatchContainer getInstance() {
        return ourInstance;
    }

    private MatchContainer() {
        Server.EXECUTOR.scheduleAtFixedRate(this::updateEventTime, 0, 1, TimeUnit.SECONDS);
    }

    /// VARS
    private Match lastMatch = new Match();
    private Match currentMatch = new Match();
    private Match nextMatch = new Match();
    private Match currentlyPlaying = new Match();
    private transient Match[] matches = {};
    private long timeToZero = 0;

    private boolean recalcEvents() {
        boolean changed = false;
        long currentTime = TimeTicker.getInstance().getCurrentTime(); //Grab the current time
        int lastMatchIndex = -1;
        int currentMatchIndex = -1;
        int nextMatchIndex = -1;
        for (int i = 0; i < matches.length; i++) { //Iterate the matches
            if (matches[i].time >= currentTime && currentMatchIndex == -1) { //If this match occurs after or on the current time (and it's not already set), it is the first current match
                currentMatchIndex = i;
                if (i >= 1) { //If there is data in the array before this index
                    lastMatchIndex = i - 1;
                }
                if (matches.length > i + 1) { //If there is more data after this index
                    nextMatchIndex = i + 1;
                }
            }
        }

        if (lastMatchIndex >= 0) {
            if (!lastMatch.equals(matches[lastMatchIndex])) { //If this is different than the data that previously was here
                lastMatch = matches[lastMatchIndex];
                currentlyPlaying = lastMatch;
                changed = true;
            }
        } else {
            lastMatch = new Match();
            currentlyPlaying = new Match();
        }

        if (currentMatchIndex >= 0) {
            if (!currentMatch.equals(matches[currentMatchIndex])) {
                currentMatch = matches[currentMatchIndex];
                changed = true;
                //Since the next match is here, and it has changed, we need to reset the checklist
                ChecklistContainer.getInstance().reset(); //Reset the checklist
            }
        } else {
            currentMatch = new Match();
        }

        if (nextMatchIndex >= 0) {
            if (!nextMatch.equals(matches[nextMatchIndex])) {
                nextMatch = matches[nextMatchIndex];
                changed = true;
            }
        } else {
            nextMatch = new Match();
        }

        timeToZero = currentMatch.time - currentTime;
        System.out.println("TTZ: " + timeToZero);

        return changed;
    }

    private void updateEventTime() {
        System.out.println("UPDATE");
        synchronized (lock) {
            if (recalcEvents()) {
                fireUpdate();
            }
        }
    }

    public void updateMatchList(Match[] newList) {
        synchronized (lock) {
            matches = MatchSorter.sort(newList);
            recalcEvents();
            fireUpdate();
        }
        for (Match m : matches) {
            System.out.println("MATCH: " + m.match_number + " AT: " + m.time);
        }
        System.out.println("LAST: " + lastMatch.match_number);
        System.out.println("CURRENT: " + currentMatch.match_number);
        System.out.println("NEXT: " + nextMatch.match_number);
    }

    public Match getLastMatch() {
        synchronized (lock) {
            return lastMatch;
        }
    }

    public Match getCurrentMatch() {
        synchronized (lock) {
            return currentMatch;
        }
    }

    public Match getNextMatch() {
        synchronized (lock) {
            return nextMatch;
        }
    }

    public long getTimeToZero() {
        synchronized (lock) {
            return timeToZero;
        }
    }

    @Override
    public String getName() {
        return "Match";
    }
}
