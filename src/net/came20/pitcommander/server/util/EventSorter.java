package net.came20.pitcommander.server.util;

import com.cpjd.models.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by cameronearle on 4/30/17.
 */
public class EventSorter {
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); //Date format TBA uses
    public static Event[] sort(Event[] events) {
        List<EventDatePair> pairsList = new ArrayList<>(); //List to hold the pair links between events and dates
        for (Event event : events) {
            try {
                Date eventDate = df.parse(event.start_date); //Try to extract a date from the event
                pairsList.add(new EventDatePair(event, eventDate)); //Add this event to the pairs list
            } catch (ParseException e) {
            }
        }
        Collections.sort(pairsList, new Comparator<EventDatePair>() {
            @Override
            public int compare(EventDatePair o1, EventDatePair o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        }); //Sort the events in ascending order by date
        Event[] toReturn = new Event[pairsList.size()]; //Create an array with the correct number of events
        for (int i = 0; i < pairsList.size(); i++) {
            toReturn[i] = pairsList.get(i).getEvent(); //Add the event to the array
        }
        return toReturn;
    }
}
