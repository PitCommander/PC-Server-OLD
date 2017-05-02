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
import net.came20.pitcommander.server.socket.RoutableAnnouncement;
import net.came20.pitcommander.server.util.EventSorter;
import net.came20.pitcommander.server.util.MatchSorter;
import net.came20.pitcommander.server.util.TimeTicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by cameronearle on 4/30/17.
 */
public class Server {
    public static void main(String[] args) {
        AnnounceSock.getInstance().setup("*", 5800);
        CommandSock.getInstance().setup("*", 5801);

        Thread announceThread = new Thread(AnnounceSock.getInstance());
        announceThread.start();
        Thread commandThread = new Thread(CommandSock.getInstance());
        commandThread.start();
        TimeTicker.getInstance().setup(1000);
        Thread timeTickerThread = new Thread(TimeTicker.getInstance());
        timeTickerThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GeneralContainer.getInstance().setTeamNumber(401);
        GeneralContainer.getInstance().setEventCode("MEME_CHEEZY_CHAMPS_MEME");


    }
}
