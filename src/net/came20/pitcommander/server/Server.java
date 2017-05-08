package net.came20.pitcommander.server;

import com.cpjd.main.TBA;
import com.cpjd.models.Event;
import com.cpjd.models.Match;
import com.google.gson.Gson;
import net.came20.pitcommander.server.announcement.TimeTickAnnouncement;
import net.came20.pitcommander.server.command.Command;
import net.came20.pitcommander.server.container.GeneralContainer;
import net.came20.pitcommander.server.socket.AnnounceSock;
import net.came20.pitcommander.server.socket.CommandSock;
import net.came20.pitcommander.server.tba.DataPoller;
import net.came20.pitcommander.server.util.EventSorter;
import net.came20.pitcommander.server.util.MatchSorter;
import net.came20.pitcommander.server.util.TimeTicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by cameronearle on 4/30/17.
 */
public class Server {
    public static final int TEAM_NUMBER = 401;
    public static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(4);

    public static void main(String[] args) {
        AnnounceSock.getInstance().setup("*", 5800);
        CommandSock.getInstance().setup("*", 5801);

        Thread announceThread = new Thread(AnnounceSock.getInstance()); //Start the announcer
        announceThread.start();

        Thread commandThread = new Thread(CommandSock.getInstance()); //Start the request engine
        commandThread.start();

        TimeTicker.getInstance().setup(1000);
        Thread timeTickerThread = new Thread(TimeTicker.getInstance()); //Start the ticker
        timeTickerThread.start();

        DataPoller.getInstance().setup(TEAM_NUMBER, 30000); //Poll every 30 seconds
        Thread dataPollerThread = new Thread(DataPoller.getInstance());
        dataPollerThread.start(); //Start the data poller

        try {
            commandThread.join(); //This thread may get the power to shutdown in the future, so it is a good choice to join
        } catch (InterruptedException e) {} //Ignore this, it just means the user stopped the program
        announceThread.interrupt();
        try {
            announceThread.join();
        } catch (InterruptedException e) { //Close down all threads and wait for their exit
            e.printStackTrace();
        }
        timeTickerThread.interrupt();
        try {
            timeTickerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dataPollerThread.interrupt();
        try {
            dataPollerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
