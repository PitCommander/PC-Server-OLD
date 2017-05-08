package net.came20.pitcommander.server.util;

import net.came20.pitcommander.server.Server;
import net.came20.pitcommander.server.container.ChecklistContainer;
import net.came20.pitcommander.server.container.MatchContainer;

/**
 * Created by cameronearle on 5/3/17.
 */
public class ChecklistPopulator {
    /**
     * Pulls data from various sources and repopulates the checklist
     * @param container
     */
    public static void populate(ChecklistContainer container) {
        //BUMPER SWITCH
        switch (MatchTools.getTeamAlliance(MatchContainer.getInstance().getCurrentMatch(), Server.TEAM_NUMBER)) {
            case RED:
                if (!MatchTools.getTeamAlliance(MatchContainer.getInstance().getLastMatch(), Server.TEAM_NUMBER).equals(MatchTools.AllianceColor.RED)) {
                    //If the previous match's color wasn't red
                    container.addCheckbox("Switch Bumpers: RED", false, false);
                }
                break;
            case BLUE:
                if (!MatchTools.getTeamAlliance(MatchContainer.getInstance().getLastMatch(), Server.TEAM_NUMBER).equals(MatchTools.AllianceColor.BLUE)) {
                    //If the previous match's color wasn't blue
                    container.addCheckbox("Switch Bumpers: BLUE", false, false);
                }
                break;
        }

        //Basic tasks
        container.addCheckbox("Switch Battery", false, false);
        container.addCheckbox("Charge Air", false, false);
        container.addCheckbox("Systems Test", false, false);
    }
}
