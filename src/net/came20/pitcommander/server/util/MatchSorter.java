package net.came20.pitcommander.server.util;

import com.cpjd.models.Event;
import com.cpjd.models.Match;

import java.text.ParseException;
import java.util.*;

/**
 * Created by cameronearle on 5/1/17.
 */
public class MatchSorter {
    public static Match[] sort(Match[] matches) {
        List<Match> matchList = new ArrayList<>(); //List to hold the pair links between events and dates
        for (Match m : matches) {
            matchList.add(m);
        }
        Collections.sort(matchList, new Comparator<Match>() {
            @Override
            public int compare(Match o1, Match o2) {
                return new Long(o1.time).compareTo(o2.time);
            }
        });
        Match[] toReturn = new Match[matchList.size()]; //Create an array with the correct number of events
        for (int i = 0; i < matchList.size(); i++) {
            toReturn[i] = matchList.get(i); //Add the event to the array
        }
        return toReturn;
    }
}
