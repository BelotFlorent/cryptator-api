/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.service;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Service to manage long-running tasks and allow cancellation
 */
@Service
public class TaskManager {
    
    private final ConcurrentHashMap<String, TaskInfo> runningTasks = new ConcurrentHashMap<>();
    
    /**
     * Register a new task and return its ID
     */
    public String registerTask(String type) {
        String taskId = UUID.randomUUID().toString();
        TaskInfo info = new TaskInfo(taskId, type);
        runningTasks.put(taskId, info);
        System.out.println("Task registered: " + taskId + " (" + type + ")");
        return taskId;
    }
    
    /**
     * Register a task with a specific ID (provided by client)
     */
    public void registerTask(String type, String taskId) {
        TaskInfo info = new TaskInfo(taskId, type);
        runningTasks.put(taskId, info);
        System.out.println("Task registered with client ID: " + taskId + " (" + type + ")");
    }
    
    /**
     * Check if a task has been cancelled
     */
    public boolean isCancelled(String taskId) {
        TaskInfo info = runningTasks.get(taskId);
        return info != null && info.isCancelled();
    }
    
    /**
     * Cancel a task
     */
    public boolean cancelTask(String taskId) {
        TaskInfo info = runningTasks.get(taskId);
        if (info != null) {
            info.cancel();
            System.out.println("Task cancelled: " + taskId);
            return true;
        }
        return false;
    }
    
    /**
     * Unregister a task when it's done
     */
    public void unregisterTask(String taskId) {
        runningTasks.remove(taskId);
        System.out.println("Task unregistered: " + taskId);
    }
    
    /**
     * Get info about a task
     */
    public TaskInfo getTaskInfo(String taskId) {
        return runningTasks.get(taskId);
    }
    
    /**
     * Get the number of running tasks
     */
    public int getRunningTasksCount() {
        return runningTasks.size();
    }
    
    /**
     * Information about a running task
     */
    public static class TaskInfo {
        private final String taskId;
        private final String type;
        private final long startTime;
        private final AtomicBoolean cancelled;
        
        public TaskInfo(String taskId, String type) {
            this.taskId = taskId;
            this.type = type;
            this.startTime = System.currentTimeMillis();
            this.cancelled = new AtomicBoolean(false);
        }
        
        public void cancel() {
            cancelled.set(true);
        }
        
        public boolean isCancelled() {
            return cancelled.get();
        }
        
        public String getTaskId() {
            return taskId;
        }
        
        public String getType() {
            return type;
        }
        
        public long getStartTime() {
            return startTime;
        }
        
        public long getElapsedTime() {
            return System.currentTimeMillis() - startTime;
        }
    }
}
