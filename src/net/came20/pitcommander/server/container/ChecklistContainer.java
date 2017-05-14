package net.came20.pitcommander.server.container;

import net.came20.pitcommander.server.util.ChecklistPopulator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by cameronearle on 4/30/17.
 */
public class ChecklistContainer extends Container {
    public class Checkbox {
        private transient boolean searchWildcard = false;
        private String name;
        private boolean value;

        public Checkbox(String name, boolean value) {
            this.name = name;
            this.value = value;
        }

        public Checkbox(String name) {
            this.name = name;
            searchWildcard = true;
        }

        public String getName() {
            return name;
        }

        public boolean getValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Checkbox checkbox = (Checkbox) o;

            if (!searchWildcard) {
                if (value != checkbox.value) return false;
            }
            return name != null ? name.equals(checkbox.name) : checkbox.name == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (value ? 1 : 0);
            return result;
        }
    }

    private static transient ChecklistContainer ourInstance = new ChecklistContainer();
    public static ChecklistContainer getInstance() {
        return ourInstance;
    }

    private Vector<Checkbox> boxes = new Vector<>();
    private boolean allChecked = true;
    private boolean blueSwitchTask = false;
    private boolean redSwitchTask = false;

    private void checkAll() {
        boolean all = true;
        for (Checkbox c : boxes) {
            if (!c.value) {
                all = false;
            }
        }
        allChecked = all;
        blueSwitchTask = boxes.contains(new Checkbox("Switch Bumpers: BLUE", false));
        redSwitchTask = boxes.contains(new Checkbox("Switch Bumpers: RED", false));
    }

    public void addCheckbox(String name, boolean value, boolean doUpdate) {
        Checkbox c = new Checkbox(name, value);
        synchronized (lock) {
            if (!boxes.contains(c)) {
                boxes.add(c);
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
            if (boxes.contains(new Checkbox(name))) {
                Checkbox c = new Checkbox(name, value);
                boxes.set(boxes.indexOf(new Checkbox(name)), c);
                checkAll();
                fireUpdate();
            }
        }
    }

    public void reset() { //Resets the checklist and repopulates default items, then tells the populator to fill in
        synchronized (lock) {
            boxes.clear();
            ChecklistPopulator.populate(this);
            checkAll();
            fireUpdate(); //Send the new list to the clients
        }
    }

    public boolean getChecked(String name) {
        synchronized (lock) {
            if (boxes.contains(new Checkbox(name))) {
                return boxes.get(boxes.indexOf(new Checkbox(name))).getValue();
            } else {
                return false;
            }
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

    public Vector<Checkbox> getList() {
        return boxes;
    }

    private ChecklistContainer() {
    }

    @Override
    public String getName() {
        return "Checklist";
    }
}
