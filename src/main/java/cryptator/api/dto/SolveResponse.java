/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.dto;

import java.util.List;

/**
 * Response DTO for cryptarithm solving
 */
public class SolveResponse {
    
    private boolean success;
    private String cryptarithm;
    private List<Solution> solutions;
    private String error;
    private long executionTimeMs;
    private int solutionCount;
    private String taskId;

    public static class Solution {
        private String assignment;
        private String evaluation;
        private boolean valid;

        public Solution() {}

        public Solution(String assignment, String evaluation, boolean valid) {
            this.assignment = assignment;
            this.evaluation = evaluation;
            this.valid = valid;
        }

        public String getAssignment() {
            return assignment;
        }

        public void setAssignment(String assignment) {
            this.assignment = assignment;
        }

        public String getEvaluation() {
            return evaluation;
        }

        public void setEvaluation(String evaluation) {
            this.evaluation = evaluation;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCryptarithm() {
        return cryptarithm;
    }

    public void setCryptarithm(String cryptarithm) {
        this.cryptarithm = cryptarithm;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public int getSolutionCount() {
        return solutionCount;
    }

    public void setSolutionCount(int solutionCount) {
        this.solutionCount = solutionCount;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
