package net.came20.pitcommander.server.util;

import com.cpjd.models.Event;

import java.util.Date;

/**
 * Created by cameronearle on 4/30/17.
 */
class EventDatePair {
    private Event event;
    private Date date;

    public EventDatePair(Event event, Date date) {
        this.event = event;
        this.date = date;
    }

    public Event getEvent() {
        return event;
    }

    public Date getDate() {
        return date;
    }
}
