/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.service;

import cryptator.api.dto.GenerateResponse;
import cryptator.cmd.CryptaBiConsumer;
import cryptator.cmd.WordArray;
import cryptator.config.CryptagenConfig;
import cryptator.gen.CryptaListGenerator;
import cryptator.solver.CryptaModelException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service for generating cryptarithms
 */
@Service
public class CryptagenService {

    @Autowired
    private TaskManager taskManager;

    public GenerateResponse generateCryptarithms(String taskIdFromClient, List<String> words, String operatorSymbol,
                                                 int solutionLimit, int timeLimit, boolean shuffle,
                                                 String countryCode, String langCode,
                                                 Integer lowerBound, Integer upperBound,
                                                 Boolean dryRun, String rightMemberType,
                                                 Integer minWords, Integer maxWords,
                                                 Boolean lightPropagation, Integer threads,
                                                 Integer crossGridSize, Boolean allowLeadingZeros) {
        
        GenerateResponse response = new GenerateResponse();
        
        // Use client-provided taskId if available, otherwise generate one
        String taskId = (taskIdFromClient != null && !taskIdFromClient.trim().isEmpty()) 
            ? taskIdFromClient 
            : taskManager.registerTask("GENERATE");
        
        // Register with the provided or generated taskId
        if (taskIdFromClient != null && !taskIdFromClient.trim().isEmpty()) {
            taskManager.registerTask("GENERATE", taskId);
        }
        
        response.setTaskId(taskId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Build word array
            WordArray wordArray;
            if (lowerBound != null && upperBound != null && countryCode != null && langCode != null) {
                wordArray = new WordArray(countryCode, langCode, lowerBound, upperBound);
            } else if (words != null && !words.isEmpty()) {
                wordArray = new WordArray(words);
            } else {
                response.setSuccess(false);
                response.setError("Either provide words list OR countryCode+langCode+lowerBound+upperBound");
                response.setExecutionTimeMs(System.currentTimeMillis() - startTime);
                return response;
            }
            
            // Configure generator
            CryptagenConfig config = new CryptagenConfig();
            
            // Apply all advanced options (now fully functional)
            if (dryRun != null && dryRun) {
                config.setDryRun(true);
            }
            
            if (rightMemberType != null) {
                switch (rightMemberType.toUpperCase()) {
                    case "FREE":
                        config.setRightMemberType(CryptagenConfig.RightMemberType.FREE);
                        break;
                    case "UNIQUE":
                        config.setRightMemberType(CryptagenConfig.RightMemberType.UNIQUE);
                        break;
                    case "FIXED":
                        config.setRightMemberType(CryptagenConfig.RightMemberType.FIXED);
                        break;
                }
            }
            
            if (minWords != null) {
                config.setMinWords(minWords);
            }
            
            if (maxWords != null) {
                config.setMaxWords(maxWords);
            }
            
            if (lightPropagation != null && lightPropagation) {
                config.setLightModel(true);
            }
            
            if (threads != null && threads > 0) {
                config.setNthreads(threads);
            }
            
            if (crossGridSize != null) {
                config.setGridSize(crossGridSize);
            }
            
            if (countryCode != null) {
                config.setCountryCode(countryCode);
            }
            
            if (langCode != null) {
                config.setLangCode(langCode);
            }
            
            if (allowLeadingZeros != null && allowLeadingZeros) {
                config.setAllowLeadingZeros(true);
            }
            
            // Configure generation type based on operator symbol
            if (operatorSymbol != null) {
                switch (operatorSymbol.trim()) {
                    case "+":
                    case "ADD":
                        config.setGenerateType(CryptagenConfig.GenerateType.ADD);
                        break;
                    case "*":
                    case "MUL":
                        config.setGenerateType(CryptagenConfig.GenerateType.MUL);
                        break;
                    case "LMUL":
                        config.setGenerateType(CryptagenConfig.GenerateType.LMUL);
                        break;
                    case "CROSS":
                        config.setGenerateType(CryptagenConfig.GenerateType.CROSS);
                        break;
                    default:
                        config.setGenerateType(CryptagenConfig.GenerateType.ADD);
                }
            }
            
            // Create generator
            Logger logger = Logger.getLogger("CryptagenService");
            logger.setLevel(java.util.logging.Level.ALL);
            CryptaListGenerator generator = new CryptaListGenerator(wordArray, config, logger);
            
            // Capture generated cryptarithms
            CryptarithmCapturingConsumer consumer = new CryptarithmCapturingConsumer(taskId, taskManager);
            
            System.out.println("DEBUG: Starting generation");
            if (words != null) {
                System.out.println("DEBUG: With " + words.size() + " words: " + words);
            } else {
                System.out.println("DEBUG: With number words from " + lowerBound + " to " + upperBound);
            }
            System.out.println("DEBUG: GenerateType=" + config.getGenerateType());
            System.out.println("DEBUG: MinWords=" + config.getMinWords() + ", MaxWords=" + config.getMaxWords());
            
            long count = generator.generate(consumer);
            
            System.out.println("DEBUG: Generator returned count=" + count);
            System.out.println("DEBUG: Consumer captured " + consumer.getCryptarithms().size() + " cryptarithms");
            
            // Check if cancelled
            if (taskManager.isCancelled(taskId)) {
                response.setSuccess(false);
                response.setError("Task cancelled by user");
                response.setCryptarithms(new ArrayList<>());
            } else {
                response.setSuccess(true);
                response.setCryptarithms(consumer.getCryptarithms());
            }
            
        } catch (CryptaModelException e) {
            response.setSuccess(false);
            response.setError("Model error: " + e.getMessage());
        } catch (RuntimeException e) {
            // Check if it's a cancellation exception
            if (e.getMessage() != null && e.getMessage().contains("cancelled")) {
                response.setSuccess(false);
                response.setError("Task cancelled by user");
                response.setCryptarithms(new ArrayList<>());
            } else {
                response.setSuccess(false);
                response.setError("Error: " + e.getMessage());
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError("Error: " + e.getMessage());
        }
        
        long endTime = System.currentTimeMillis();
        response.setExecutionTimeMs(endTime - startTime);
        
        // Unregister task
        taskManager.unregisterTask(taskId);
        
        return response;
    }
    
    /**
     * Custom consumer to capture generated cryptarithms
     */
    private static class CryptarithmCapturingConsumer extends CryptaBiConsumer {
        
        private final List<GenerateResponse.GeneratedCryptarithm> cryptarithms = new ArrayList<>();
        private final String taskId;
        private final TaskManager taskManager;
        
        public CryptarithmCapturingConsumer(String taskId, TaskManager taskManager) {
            super(Logger.getLogger("CryptarithmLogger"));
            this.taskId = taskId;
            this.taskManager = taskManager;
            this.withCryptarithmLog();
        }
        
        @Override
        public void accept(ICryptaNode node, ICryptaSolution solution) {
            // Check if task is cancelled before processing
            if (taskManager.isCancelled(taskId)) {
                // Throw exception to stop the generator immediately
                throw new RuntimeException("Task cancelled by user");
            }
            
            super.accept(node, solution);
            
            String cryptarithm = cryptator.tree.TreeUtils.writeInorder(node);
            String solutionStr = solution.toString();
            
            GenerateResponse.GeneratedCryptarithm gen = new GenerateResponse.GeneratedCryptarithm();
            gen.setCryptarithm(cryptarithm);
            gen.setSolution(solutionStr);
            
            cryptarithms.add(gen);
        }
        
        public List<GenerateResponse.GeneratedCryptarithm> getCryptarithms() {
            return cryptarithms;
        }
    }
}
