package net.came20.pitcommander.server.container;

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
        blueSwitchTask = boxes.containsKey("Switch Bumpers: BLUE");
        redSwitchTask = boxes.containsKey("Switch Bumpers: RED");
    }

    public void addCheckbox(String name, boolean value) {
        synchronized (lock) {
            if (!boxes.containsKey(name)) {
                boxes.put(name, value);
                checkAll();
                fireUpdate();
            }
        }
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
            fireUpdate(); //Send the cleared list to the clients

        }
    }

    public boolean getChecked(String name) {
        synchronized (lock) {
            return boxes.get(name);
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
