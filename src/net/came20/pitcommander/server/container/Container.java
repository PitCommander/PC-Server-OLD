package net.came20.pitcommander.server.container;

import net.came20.pitcommander.server.announcement.ContainerUpdateAnnouncement;
import net.came20.pitcommander.server.socket.AnnounceSock;

/**
 * Created by cameronearle on 4/30/17.
 */
public abstract class Container {
    public abstract String getName();
    final transient Object lock = new Object();
    public void fireUpdate() {
        AnnounceSock.getInstance().announce(new ContainerUpdateAnnouncement(this));
    }
}
