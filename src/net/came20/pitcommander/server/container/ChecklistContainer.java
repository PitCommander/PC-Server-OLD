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

    private void checkAll() {
        boolean all = true;
        for (boolean b : boxes.values()) {
            if (!b) {
                all = false;
            }
        }
        allChecked = all;
    }

    public void addCheckbox(String name, boolean value) {
        synchronized (lock) {
            boxes.putIfAbsent(name, value);
            checkAll();
            fireUpdate();
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
