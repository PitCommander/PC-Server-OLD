package net.came20.pitcommander.server.socket;

import net.came20.pitcommander.server.announcement.Announcement;

/**
 * Created by cameronearle on 4/30/17.
 */
public class RoutableAnnouncement {
    private String id;
    private Announcement payload;

    public RoutableAnnouncement(Announcement announcement) {
        payload = announcement;
        id = announcement.getName();
    }
}
