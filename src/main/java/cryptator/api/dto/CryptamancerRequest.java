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
import java.util.Map;

/**
 * Request DTO for Cryptamancer (game mode)
 */
public class CryptamancerRequest {
    
    @NotBlank(message = "Cryptarithm cannot be empty")
    private String cryptarithm;
    
    /**
     * Current attempt: map of symbol to digit (e.g., {"s": 9, "e": 5})
     */
    private Map<String, Integer> attempt;
    
    /**
     * Requested hint level (1-3)
     */
    private Integer hintLevel = 1;
    
    /**
     * Game difficulty: EASY, MEDIUM, HARD
     */
    private String difficulty = "MEDIUM";

    public String getCryptarithm() {
        return cryptarithm;
    }

    public void setCryptarithm(String cryptarithm) {
        this.cryptarithm = cryptarithm;
    }

    public Map<String, Integer> getAttempt() {
        return attempt;
    }

    public void setAttempt(Map<String, Integer> attempt) {
        this.attempt = attempt;
    }

    public Integer getHintLevel() {
        return hintLevel;
    }

    public void setHintLevel(Integer hintLevel) {
        this.hintLevel = hintLevel;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
