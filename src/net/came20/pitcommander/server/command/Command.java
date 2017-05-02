package net.came20.pitcommander.server.command;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by cameronearle on 4/30/17.
 */
public class Command {
    private Commands command;
    private List<HashMap<String, Object>> payload;

    public Command(Commands command, List<HashMap<String, Object>> payload) {
        this.command = command;
        this.payload = payload;
    }

    public Commands getCommand() {
        return command;
    }

    public List<HashMap<String, Object>> getPayload() {
        return payload;
    }
}
