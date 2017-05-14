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

    public class ScheduleElement {
        private long number;
        private long time;
        private MatchTools.AllianceColor alliance;
        private MatchTools.Outcome outcome;

        public ScheduleElement(long number, long time, MatchTools.AllianceColor alliance, MatchTools.Outcome outcome) {
            this.number = number;
            this.time = time;
            this.alliance = alliance;
            this.outcome = outcome;
        }

        public ScheduleElement(Match m) {
            this(m.match_number, m.time, MatchTools.getTeamAlliance(m, Server.TEAM_NUMBER), MatchTools.getOutcome(m, Server.TEAM_NUMBER));
        }
    }

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
    private transient Match[] allMatches = {};
    private ArrayList<ScheduleElement> schedule = new ArrayList<>();
    private int wins = 0;
    private int losses = 0;
    private int ties = 0;
    private long timeToZero = 0;

    private boolean recalcEvents() {
        boolean changed = false;
        long currentTime = TimeTicker.getInstance().getCurrentTime(); //Grab the current time
        int lastMatchIndex = -1;
        int currentMatchIndex = -1;
        int nextMatchIndex = -1;
        int currentlyPlayingIndex = -1;
        for (int i = 0; i < matches.length; i++) { //Iterate the matches
            if (matches[i].time >= currentTime && currentMatchIndex == -1) { //If this match occurs after or on the current time (and it's not already set), it is the first current match
                currentMatchIndex = i;
                if (i >= 1) { //If there is data in the array before this index
                    lastMatchIndex = i - 1;
                }
                if (matches.length > i + 1) { //If there is more data after this index
                    nextMatchIndex = i + 1;
                }
                break;
            }
        }
        for (int i = 0; i < allMatches.length; i++) {
            if (allMatches[i].time >= currentTime && currentlyPlayingIndex == -1) {
                if (allMatches.length > 1) {
                    currentlyPlayingIndex = i - 1;
                } else {
                    if (allMatches.length > 0) {
                        currentlyPlayingIndex = 0;
                    }
                }
                break;
            }
        }

        if (lastMatchIndex >= 0) {
            if (!lastMatch.equals(matches[lastMatchIndex])) { //If this is different than the data that previously was here
                lastMatch = matches[lastMatchIndex];
                changed = true;
            }
        } else {
            lastMatch = new Match();
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

        if (currentlyPlayingIndex >= 0) {
            if (!currentlyPlaying.equals(allMatches[currentlyPlayingIndex])) {
                currentlyPlaying = allMatches[currentlyPlayingIndex];
                changed = true;
            }
        } else {
            currentlyPlaying = new Match();
        }

        timeToZero = currentMatch.time - currentTime;
        if (timeToZero < 0) {
            timeToZero = 0;
        }
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

    public void updateMatchList(Match[] newList, Match[] newAllList) {
        synchronized (lock) {
            matches = MatchSorter.sort(newList);
            allMatches = MatchSorter.sort(newAllList);
            wins = 0;
            losses = 0;
            ties = 0;
            for (Match m : matches) {
                schedule.add(new ScheduleElement(m));
                switch (MatchTools.getOutcome(m, Server.TEAM_NUMBER)) {
                    case WIN:
                        wins++;
                        break;
                    case LOSS:
                        losses++;
                        break;
                    case TIE:
                        ties++;
                        break;
                }
            }
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
