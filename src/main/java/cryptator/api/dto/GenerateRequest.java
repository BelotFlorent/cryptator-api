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
 * Request DTO for generating cryptarithms
 */
public class GenerateRequest {
    
    /**
     * Optional task ID provided by the client for cancellation support
     */
    private String taskId;
    
    // Optional: either provide words OR countryCode+langCode+bounds for doubly-true
    private List<String> words;
    
    private String operatorSymbol = "+";
    
    private Integer solutionLimit = 1;
    
    private Integer timeLimit = 60;
    
    private Boolean shuffle = false;
    
    private String countryCode;
    
    private String langCode;
    
    private Integer lowerBound;
    
    private Integer upperBound;
    
    // ===== Options avancées de génération =====
    
    /**
     * Dry run mode: génère sans résoudre (plus rapide)
     */
    private Boolean dryRun = false;
    
    /**
     * Type de membre droit: FREE, UNIQUE, FIXED
     * - FREE: aucune contrainte
     * - UNIQUE: le membre droit doit être différent des membres gauches
     * - FIXED: le membre droit est fixe
     */
    private String rightMemberType = "UNIQUE";
    
    /**
     * Nombre minimum de mots à utiliser
     */
    private Integer minWords;
    
    /**
     * Nombre maximum de mots à utiliser (-1 = illimité)
     */
    private Integer maxWords;
    
    /**
     * Utiliser un modèle CP léger (propagation réduite)
     */
    private Boolean lightPropagation = false;
    
    /**
     * Nombre de threads pour la génération parallèle
     */
    private Integer threads = 1;
    
    /**
     * Taille de la grille (pour mode CROSS uniquement)
     */
    private Integer crossGridSize;
    
    /**
     * Autoriser les zéros en début de mot
     */
    private Boolean allowLeadingZeros = false;

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public String getOperatorSymbol() {
        return operatorSymbol;
    }

    public void setOperatorSymbol(String operatorSymbol) {
        this.operatorSymbol = operatorSymbol;
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

    public Boolean getShuffle() {
        return shuffle;
    }

    public void setShuffle(Boolean shuffle) {
        this.shuffle = shuffle;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public Integer getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Integer lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Integer getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Integer upperBound) {
        this.upperBound = upperBound;
    }

    public Boolean getDryRun() {
        return dryRun;
    }

    public void setDryRun(Boolean dryRun) {
        this.dryRun = dryRun;
    }

    public String getRightMemberType() {
        return rightMemberType;
    }

    public void setRightMemberType(String rightMemberType) {
        this.rightMemberType = rightMemberType;
    }

    public Integer getMinWords() {
        return minWords;
    }

    public void setMinWords(Integer minWords) {
        this.minWords = minWords;
    }

    public Integer getMaxWords() {
        return maxWords;
    }

    public void setMaxWords(Integer maxWords) {
        this.maxWords = maxWords;
    }

    public Boolean getLightPropagation() {
        return lightPropagation;
    }

    public void setLightPropagation(Boolean lightPropagation) {
        this.lightPropagation = lightPropagation;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Integer getCrossGridSize() {
        return crossGridSize;
    }

    public void setCrossGridSize(Integer crossGridSize) {
        this.crossGridSize = crossGridSize;
    }
    
    public Boolean getAllowLeadingZeros() {
        return allowLeadingZeros;
    }

    public void setAllowLeadingZeros(Boolean allowLeadingZeros) {
        this.allowLeadingZeros = allowLeadingZeros;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
