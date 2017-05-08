package net.came20.pitcommander.server.socket;

import net.came20.pitcommander.server.announcement.Announcement;

/**
 * Created by cameronearle on 4/30/17.
 */
class RoutableAnnouncement {
    private String id;
    private Announcement payload;

    RoutableAnnouncement(Announcement announcement) {
        payload = announcement;
        id = announcement.getName();
    }
}
