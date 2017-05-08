package net.came20.pitcommander.server.container;

import net.came20.pitcommander.server.util.ChecklistPopulator;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by cameronearle on 4/30/17.
 */
public class ChecklistContainer extends Container {
    private static transient ChecklistContainer ourInstance = new ChecklistContainer();
    public static ChecklistContainer getInstance() {
        return ourInstance;
    }

    private Map<String, Boolean> boxes = new LinkedHashMap<>();
    private boolean allChecked = true;
    private boolean blueSwitchTask = false;
    private boolean redSwitchTask = false;

    private void checkAll() {
        boolean all = true;
        for (boolean b : boxes.values()) {
            if (!b) {
                all = false;
            }
        }
        allChecked = all;
        blueSwitchTask = boxes.containsKey("Switch Bumpers: BLUE") && !boxes.getOrDefault("Switch Bumpers: BLUE", true);
        redSwitchTask = boxes.containsKey("Switch Bumpers: RED") && !boxes.getOrDefault("Switch Bumpers: RED", true);
    }

    public void addCheckbox(String name, boolean value, boolean doUpdate) {
        synchronized (lock) {
            if (!boxes.containsKey(name)) {
                boxes.put(name, value);
                checkAll();
                if (doUpdate)
                    fireUpdate();
            }
        }
    }

    public void addCheckbox(String name, boolean value) {
        this.addCheckbox(name, value, true);
    }

    public void removeCheckbox(String name) {
        synchronized (lock) {
            boxes.remove(name);
            checkAll();
            fireUpdate();
        }
    }

    public void setChecked(String name, boolean value) {
        synchronized (lock) {
            boxes.replace(name, value);
            checkAll();
            fireUpdate();
        }
    }

    public void reset() { //Resets the checklist and repopulates default items, then tells the populator to fill in
        synchronized (lock) {
            boxes.clear();
            ChecklistPopulator.populate(this);
            fireUpdate(); //Send the new list to the clients
        }
    }

    public boolean getChecked(String name) {
        synchronized (lock) {
            return boxes.get(name);
        }
    }

    public boolean getAllChecked() {
        synchronized (lock) {
            return allChecked;
        }
    }

    public boolean getRedTask() {
        synchronized (lock) {
            return redSwitchTask;
        }
    }

    public boolean getBlueTask() {
        synchronized (lock) {
            return blueSwitchTask;
        }
    }

    public Map<String, Boolean> getMap() {
        return boxes;
    }

    private ChecklistContainer() {
    }

    @Override
    public String getName() {
        return "Checklist";
    }
}
