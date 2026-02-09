/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.dto;

import javax.validation.constraints.NotBlank;

/**
 * Request DTO for solving a cryptarithm
 */
public class SolveRequest {
    
    @NotBlank(message = "Cryptarithm cannot be empty")
    private String cryptarithm;
    
    /**
     * Optional task ID provided by the client for cancellation support
     */
    private String taskId;
    
    private String solverType = "SCALAR";
    
    private Integer solutionLimit = 0;
    
    private Integer timeLimit = 0;
    
    private Integer arithmeticBase = 10;
    
    private Boolean checkSolution = false;
    
    private Boolean exportGraphviz = false;
    
    /**
     * Allow leading zeros in words
     */
    private Boolean allowLeadingZeros = false;
    
    /**
     * Use Horner evaluation scheme
     */
    private Boolean hornerScheme = false;
    
    /**
     * Manual assignments: map of symbol to digit (e.g., {"s": 9, "e": 5})
     */
    private java.util.Map<String, Integer> assignments;

    public String getCryptarithm() {
        return cryptarithm;
    }

    public void setCryptarithm(String cryptarithm) {
        this.cryptarithm = cryptarithm;
    }

    public String getSolverType() {
        return solverType;
    }

    public void setSolverType(String solverType) {
        this.solverType = solverType;
    }

    public Integer getSolutionLimit() {
        return solutionLimit;
    }

    public void setSolutionLimit(Integer solutionLimit) {
        this.solutionLimit = solutionLimit;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getArithmeticBase() {
        return arithmeticBase;
    }

    public void setArithmeticBase(Integer arithmeticBase) {
        this.arithmeticBase = arithmeticBase;
    }

    public Boolean getCheckSolution() {
        return checkSolution;
    }

    public void setCheckSolution(Boolean checkSolution) {
        this.checkSolution = checkSolution;
    }

    public Boolean getExportGraphviz() {
        return exportGraphviz;
    }

    public void setExportGraphviz(Boolean exportGraphviz) {
        this.exportGraphviz = exportGraphviz;
    }

    public Boolean getAllowLeadingZeros() {
        return allowLeadingZeros;
    }

    public void setAllowLeadingZeros(Boolean allowLeadingZeros) {
        this.allowLeadingZeros = allowLeadingZeros;
    }

    public Boolean getHornerScheme() {
        return hornerScheme;
    }

    public void setHornerScheme(Boolean hornerScheme) {
        this.hornerScheme = hornerScheme;
    }

    public java.util.Map<String, Integer> getAssignments() {
        return assignments;
    }

    public void setAssignments(java.util.Map<String, Integer> assignments) {
        this.assignments = assignments;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
