package net.came20.pitcommander.server.socket;

import com.google.gson.Gson;
import net.came20.pitcommander.server.announcement.Announcement;
import org.omg.PortableServer.THREAD_POLICY_ID;
import org.zeromq.ZMQ;

import java.util.Vector;

/**
 * Created by cameronearle on 4/30/17.
 */
public class AnnounceSock implements Runnable {
    private transient static AnnounceSock ourInstance = new AnnounceSock();
    public static AnnounceSock getInstance() {
        return ourInstance;
    }

    private String address;
    private int port;
    private Vector<Announcement> announcementQueue = new Vector<>();
    private Gson gson = new Gson();

    private AnnounceSock() {}

    public void setup(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void announce(Announcement announcement) {
        announcementQueue.add(announcement);
    }

    @Override
    public void run() {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.PUB);
        socket.bind("tcp://" + address + ":" + port);

        while (!Thread.interrupted()) {
            while (announcementQueue.size() > 0) {
                String toSend = gson.toJson(new RoutableAnnouncement(announcementQueue.remove(0)));
                socket.send(toSend);
                System.out.println(toSend);
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
