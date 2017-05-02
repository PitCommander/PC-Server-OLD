package net.came20.pitcommander.server.announcement;

import net.came20.pitcommander.server.container.Container;

/**
 * Created by cameronearle on 4/30/17.
 */
public class ContainerUpdateAnnouncement implements Announcement {
    private Container container;

    public ContainerUpdateAnnouncement(Container container) {
        this.container = container;
    }

    @Override
    public String getName() {
        return container.getName() + "ContainerUpdate";
    }
}
