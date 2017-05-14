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

    public enum Outcome {
        WIN,
        LOSS,
        TIE,
        NONE
    }

    public static AllianceColor getTeamAlliance(Match m, int team) {
        if (m.redTeams != null && m.blueTeams != null) {
            for (String s : m.redTeams) if (Integer.parseInt(s.replace("frc", "")) == team) return AllianceColor.RED;
            for (String s : m.blueTeams) if (Integer.parseInt(s.replace("frc", "")) == team) return AllianceColor.BLUE;
        }
        return AllianceColor.NONE;
    }

    public static Outcome getOutcome(Match m, int team) {
        if (m.redScore == 0 && m.blueScore == 0) {
            return Outcome.NONE;
        }

        if (m.redScore == m.blueScore) {
            return Outcome.TIE;
        }

        switch (getTeamAlliance(m, team)) {
            case RED:
                if (m.redScore > m.blueScore) {
                    return Outcome.WIN;
                }
                break;
            case BLUE:
                if (m.blueScore > m.redScore) {
                    return Outcome.WIN;
                }
                break;
        }
        return Outcome.LOSS;
    }
}
