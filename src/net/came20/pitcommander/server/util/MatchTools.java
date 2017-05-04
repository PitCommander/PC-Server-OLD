package net.came20.pitcommander.server.util;

import com.cpjd.models.Match;

/**
 * Created by cameronearle on 5/3/17.
 */
public class MatchTools {
    public enum AllianceColor {
        RED,
        BLUE,
        NONE
    }

    public static AllianceColor getTeamAlliance(Match m, int team) {
        for(String s : m.redTeams) if(Integer.parseInt(s.replace("frc", "")) == team) return AllianceColor.RED;
        for(String s : m.blueTeams) if(Integer.parseInt(s.replace("frc", "")) == team) return AllianceColor.BLUE;
        return AllianceColor.NONE;
    }
}
