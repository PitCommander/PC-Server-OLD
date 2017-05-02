package net.came20.pitcommander.server.socket;

import com.google.gson.Gson;
import net.came20.pitcommander.server.command.Command;
import net.came20.pitcommander.server.command.CommandRouter;
import net.came20.pitcommander.server.command.Reply;
import org.zeromq.ZMQ;

/**
 * Created by cameronearle on 4/30/17.
 */
public class CommandSock implements Runnable {
    private static CommandSock ourInstance = new CommandSock();

    public static CommandSock getInstance() {
        return ourInstance;
    }

    private CommandSock() {
    }

    private String address;
    private int port;

    public void setup(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.REP);
        socket.bind("tcp://" + address + ":" + port);
        socket.setReceiveTimeOut(1000); //Allow up to a es

        while (!Thread.interrupted()) {
            String command = socket.recvStr();
            if (command != null) {
                Command parsedCommand = gson.fromJson(command, Command.class);
                System.out.println("GOT COMMAND: " + command);
                Reply reply = CommandRouter.route(parsedCommand);
                String encodedReply = gson.toJson(reply);
                socket.send(encodedReply);
                System.out.println("REPLYING: " + encodedReply);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
