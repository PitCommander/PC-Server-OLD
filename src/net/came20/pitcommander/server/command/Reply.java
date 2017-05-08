package net.came20.pitcommander.server.command;

import java.util.HashMap;
import java.util.List;

/**
 * Created by cameronearle on 4/30/17.
 */
public class Reply {
    private Replies reply;
    private HashMap<String, Object> payload;

    public Reply(Replies reply, HashMap<String, Object> payload) {
        this.reply = reply;
        this.payload = payload;
    }

    public Replies getReply() {
        return reply;
    }

    public HashMap<String, Object> getPayload() {
        return payload;
    }
}
