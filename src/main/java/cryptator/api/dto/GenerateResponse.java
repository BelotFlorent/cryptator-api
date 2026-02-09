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
 * Response DTO for cryptarithm generation
 */
public class GenerateResponse {
    
    private boolean success;
    private List<GeneratedCryptarithm> cryptarithms;
    private String error;
    private long executionTimeMs;
    private String taskId;

    public static class GeneratedCryptarithm {
        private String cryptarithm;
        private String solution;

        public GeneratedCryptarithm() {}

        public GeneratedCryptarithm(String cryptarithm, String solution) {
            this.cryptarithm = cryptarithm;
            this.solution = solution;
        }

        public String getCryptarithm() {
            return cryptarithm;
        }

        public void setCryptarithm(String cryptarithm) {
            this.cryptarithm = cryptarithm;
        }

        public String getSolution() {
            return solution;
        }

        public void setSolution(String solution) {
            this.solution = solution;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<GeneratedCryptarithm> getCryptarithms() {
        return cryptarithms;
    }

    public void setCryptarithms(List<GeneratedCryptarithm> cryptarithms) {
        this.cryptarithms = cryptarithms;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
