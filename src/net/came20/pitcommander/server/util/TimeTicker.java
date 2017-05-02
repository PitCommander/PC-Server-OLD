package net.came20.pitcommander.server.util;

import net.came20.pitcommander.server.announcement.TimeTickAnnouncement;
import net.came20.pitcommander.server.socket.AnnounceSock;

import java.time.Instant;

/**
 * Created by cameronearle on 4/30/17.
 */
public class TimeTicker implements Runnable {
    private static transient TimeTicker ourInstance = new TimeTicker();
    public static TimeTicker getInstance() {
        return ourInstance;
    }

    private int interval;
    private AnnounceSock sock = AnnounceSock.getInstance();
    private long currentTime;
    private Object lock = new Object();

    public void setup(int interval) {
        this.interval = interval;
    }

    private TimeTicker() {
    }

    public long getCurrentTime() {
        synchronized (lock) {
            return currentTime;
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            synchronized (lock) {
                currentTime = Instant.now().toEpochMilli();
                sock.announce(new TimeTickAnnouncement(currentTime)); //Send the EPOCH time
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
