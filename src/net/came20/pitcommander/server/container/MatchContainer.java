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
        TaskExecutor.getInstance().scheduleAtFixedRate(this::updateEventTime, 0, 1, TimeUnit.SECONDS);
    }

    /// VARS
    private Match lastMatch = new Match();
    private Match currentMatch = new Match();
    private Match nextMatch = new Match();
    private Match[] matches = {new Match()};
    private long timeToNext = 0;

    private boolean recalcEvents() {
        boolean changed = false;
        long currentTime = TimeTicker.getInstance().getCurrentTime(); //Grab the current time
        int lastMatchIndex = -1;
        int currentMatchIndex = -1;
        int nextMatchIndex = -1;
        for (int i = 0; i < matches.length; i++) { //Iterate the matches
            if (matches[i].time >= currentTime) { //If this match occurs after or on the current time, it is the first current match
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
                changed = true;
            }
        } else {
            lastMatch = new Match();
        }

        if (currentMatchIndex >= 0) {
            if (!currentMatch.equals(matches[currentMatchIndex])) {
                currentMatch = matches[currentMatchIndex];
                changed = true;
            }
        } else {
            currentMatch = new Match();
        }

        if (nextMatchIndex >= 0) {
            if (!nextMatch.equals(matches[nextMatchIndex])) {
                nextMatch = matches[nextMatchIndex];
                changed = true;
                //Since this is the next match, and it has changed, we need to reset the checklist
                ChecklistContainer.getInstance().reset(); //Reset the checklist
                switch (MatchTools.getTeamAlliance(nextMatch, Server.TEAM_NUMBER)) {
                    case RED:
                        ChecklistContainer.getInstance().addCheckbox("Switch Bumpers: RED", false);
                        break;
                    case BLUE:
                        ChecklistContainer.getInstance().addCheckbox("Switch Bumpers: BLUE", false);
                        break;
                }
            }
        } else {
            nextMatch = new Match();
        }

        return changed;
    }

    private void updateEventTime() {
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
    }

    @Override
    public String getName() {
        return "Match";
    }
}
