/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.dto;

/**
 * Response DTO for Cryptamancer hint requests
 */
public class CryptamancerHintResponse {
    
    private boolean success;
    private String hint;
    private Integer nextHintLevel;
    private Boolean isCorrect;
    private Integer correctCount;
    private Integer totalSymbols;
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Integer getNextHintLevel() {
        return nextHintLevel;
    }

    public void setNextHintLevel(Integer nextHintLevel) {
        this.nextHintLevel = nextHintLevel;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    public Integer getTotalSymbols() {
        return totalSymbols;
    }

    public void setTotalSymbols(Integer totalSymbols) {
        this.totalSymbols = totalSymbols;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
