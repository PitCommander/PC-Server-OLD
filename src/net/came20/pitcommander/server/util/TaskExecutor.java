package net.came20.pitcommander.server.util;

import net.came20.pitcommander.server.container.ChecklistContainer;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * Created by cameronearle on 5/3/17.
 */
public class TaskExecutor extends ScheduledThreadPoolExecutor {
    private static transient TaskExecutor ourInstance = new TaskExecutor(4);
    public static TaskExecutor getInstance() {
        return ourInstance;
    }

    private TaskExecutor(int corePoolSize) {
        super(corePoolSize);
    }
}
