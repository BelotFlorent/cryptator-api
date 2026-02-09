/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.service;

import cryptator.api.dto.CryptamancerHintResponse;
import cryptator.config.CryptaCmdConfig;
import cryptator.config.CryptatorConfig;
import cryptator.parser.CryptaParserWrapper;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;

import static cryptator.Cryptator.createSolver;
import static cryptator.Cryptator.parseCryptarithm;

/**
 * Service for Cryptamancer game mode (stateless)
 */
@Service
public class CryptamancerService {

    /**
     * Get a hint based on the current attempt
     */
    public CryptamancerHintResponse getHint(String cryptarithm, Map<String, Integer> attempt, int hintLevel) {
        CryptamancerHintResponse response = new CryptamancerHintResponse();
        
        try {
            // Parse and solve the cryptarithm
            CryptatorConfig config = new CryptatorConfig();
            config.setSolverType(CryptaCmdConfig.SolverType.SCALAR);
            config.setSolutionLimit(1); // We only need one solution
            
            ICryptaSolver solver = createSolver(config);
            solver.limitSolution(1);
            
            CryptaParserWrapper parser = new CryptaParserWrapper();
            Logger tempLogger = Logger.getLogger("TempLogger");
            tempLogger.setUseParentHandlers(false);
            ICryptaNode node = parseCryptarithm(cryptarithm, parser, tempLogger);
            
            // Get all symbols from the cryptarithm
            Set<Character> allSymbols = extractSymbols(cryptarithm);
            response.setTotalSymbols(allSymbols.size());
            
            // Check current attempt
            if (attempt != null && !attempt.isEmpty()) {
                int correctCount = 0;
                
                // We need to solve to compare
                final ICryptaSolution[] solutionHolder = new ICryptaSolution[1];
                solver.solve(node, config, (n, s) -> {
                    if (solutionHolder[0] == null) {
                        solutionHolder[0] = s;
                    }
                });
                
                if (solutionHolder[0] != null) {
                    for (Map.Entry<String, Integer> entry : attempt.entrySet()) {
                        String symbol = entry.getKey();
                        Integer digit = entry.getValue();
                        
                        if (symbol != null && symbol.length() == 1) {
                            char c = symbol.charAt(0);
                            int correctDigit = solutionHolder[0].getDigit(c);
                            if (correctDigit == digit) {
                                correctCount++;
                            }
                        }
                    }
                    
                    response.setCorrectCount(correctCount);
                    response.setIsCorrect(correctCount == allSymbols.size());
                }
            }
            
            // Generate hint based on level
            String hint = generateHint(cryptarithm, hintLevel);
            response.setHint(hint);
            response.setNextHintLevel(hintLevel + 1);
            response.setSuccess(true);
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError("Error: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Validate a complete solution
     */
    public CryptamancerHintResponse validateSolution(String cryptarithm, Map<String, Integer> solution) {
        CryptamancerHintResponse response = new CryptamancerHintResponse();
        
        try {
            // Parse and solve
            CryptatorConfig config = new CryptatorConfig();
            config.setSolverType(CryptaCmdConfig.SolverType.SCALAR);
            config.setSolutionLimit(1);
            
            ICryptaSolver solver = createSolver(config);
            solver.limitSolution(1);
            
            CryptaParserWrapper parser = new CryptaParserWrapper();
            Logger tempLogger = Logger.getLogger("TempLogger");
            tempLogger.setUseParentHandlers(false);
            ICryptaNode node = parseCryptarithm(cryptarithm, parser, tempLogger);
            
            final ICryptaSolution[] solutionHolder = new ICryptaSolution[1];
            solver.solve(node, config, (n, s) -> {
                if (solutionHolder[0] == null) {
                    solutionHolder[0] = s;
                }
            });
            
            if (solutionHolder[0] == null) {
                response.setSuccess(false);
                response.setError("No solution exists for this cryptarithm");
                return response;
            }
            
            // Compare solution
            Set<Character> allSymbols = extractSymbols(cryptarithm);
            int correctCount = 0;
            
            for (char c : allSymbols) {
                String key = String.valueOf(c);
                if (solution.containsKey(key)) {
                    int expected = solutionHolder[0].getDigit(c);
                    int actual = solution.get(key);
                    if (expected == actual) {
                        correctCount++;
                    }
                }
            }
            
            boolean isCorrect = (correctCount == allSymbols.size());
            
            response.setSuccess(true);
            response.setIsCorrect(isCorrect);
            response.setCorrectCount(correctCount);
            response.setTotalSymbols(allSymbols.size());
            
            if (isCorrect) {
                response.setHint("🎉 Congratulations! You solved it correctly!");
            } else {
                response.setHint("Not quite right. You have " + correctCount + " out of " + allSymbols.size() + " correct.");
            }
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError("Error: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Extract all unique letter symbols from the cryptarithm
     */
    private Set<Character> extractSymbols(String cryptarithm) {
        Set<Character> symbols = new HashSet<>();
        for (char c : cryptarithm.toCharArray()) {
            if (Character.isLetter(c)) {
                symbols.add(Character.toLowerCase(c));
            }
        }
        return symbols;
    }
    
    /**
     * Generate hints based on level
     */
    private String generateHint(String cryptarithm, int level) {
        switch (level) {
            case 1:
                return "Look for letters that appear multiple times in the equation. They must have the same digit!";
            case 2:
                return "Remember: Each letter represents a unique digit (0-9), and different letters represent different digits.";
            case 3:
                return "Try working backwards from the rightmost column. What could the carry be?";
            case 4:
                return "The first letter of each word usually cannot be 0 (unless explicitly allowed).";
            default:
                return "Keep trying different combinations. Think about which digits could produce the results you see!";
        }
    }
}
