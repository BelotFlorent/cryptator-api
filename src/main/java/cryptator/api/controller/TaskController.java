/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.controller;

import cryptator.api.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for managing long-running tasks
 */
@RestController
@RequestMapping("/api/v1/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    @Autowired
    private TaskManager taskManager;
    
    /**
     * Cancel a running task
     * POST /api/v1/tasks/{taskId}/cancel
     */
    @PostMapping("/{taskId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelTask(@PathVariable String taskId) {
        Map<String, Object> response = new HashMap<>();
        
        boolean cancelled = taskManager.cancelTask(taskId);
        
        if (cancelled) {
            response.put("success", true);
            response.put("message", "Task cancelled successfully");
            response.put("taskId", taskId);
        } else {
            response.put("success", false);
            response.put("message", "Task not found or already completed");
            response.put("taskId", taskId);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get status of a task
     * GET /api/v1/tasks/{taskId}/status
     */
    @GetMapping("/{taskId}/status")
    public ResponseEntity<Map<String, Object>> getTaskStatus(@PathVariable String taskId) {
        Map<String, Object> response = new HashMap<>();
        
        TaskManager.TaskInfo info = taskManager.getTaskInfo(taskId);
        
        if (info != null) {
            response.put("found", true);
            response.put("taskId", taskId);
            response.put("type", info.getType());
            response.put("elapsedTimeMs", info.getElapsedTime());
            response.put("cancelled", info.isCancelled());
            response.put("status", info.isCancelled() ? "CANCELLED" : "RUNNING");
        } else {
            response.put("found", false);
            response.put("taskId", taskId);
            response.put("status", "NOT_FOUND");
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get statistics about running tasks
     * GET /api/v1/tasks/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getTaskStats() {
        Map<String, Object> response = new HashMap<>();
        response.put("runningTasks", taskManager.getRunningTasksCount());
        return ResponseEntity.ok(response);
    }
}
