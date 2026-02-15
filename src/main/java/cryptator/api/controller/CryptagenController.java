/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.controller;

import cryptator.api.dto.GenerateRequest;
import cryptator.api.dto.GenerateResponse;
import cryptator.api.service.CryptagenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

/**
 * REST Controller for generating cryptarithms
 */
@RestController
@RequestMapping("/api/v1/cryptagen")
@CrossOrigin(origins = "*")
@Tag(name = "Cryptagen", description = "Generate cryptarithms from word lists")
public class CryptagenController {

    @Autowired
    private CryptagenService cryptagenService;

    /**
     * Generate cryptarithms from a list of words
     * POST /api/v1/cryptagen/generate
     * 
     * Example request body:
     * {
     *   "words": ["ONE", "TWO", "THREE"],
     *   "operatorSymbol": "+",
     *   "solutionLimit": 5,
     *   "timeLimit": 60,
     *   "shuffle": false
     * }
     */
    @Operation(summary = "Generate cryptarithms", 
               description = "Generate cryptarithms from a list of words with specified operator")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully generated",
                     content = @Content(schema = @Schema(implementation = GenerateResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/generate")
    public ResponseEntity<GenerateResponse> generate(@Valid @RequestBody GenerateRequest request) {
        GenerateResponse response = cryptagenService.generateCryptarithms(
            request.getTaskId(),
            request.getWords(),
            request.getOperatorSymbol(),
            request.getSolutionLimit(),
            request.getTimeLimit(),
            request.getShuffle(),
            request.getCountryCode(),
            request.getLangCode(),
            request.getLowerBound(),
            request.getUpperBound(),
            request.getDryRun(),
            request.getRightMemberType(),
            request.getMinWords(),
            request.getMaxWords(),
            request.getLightPropagation(),
            request.getThreads(),
            request.getCrossGridSize(),
            request.getAllowLeadingZeros()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Simple generation endpoint with query parameters
     * GET /api/v1/cryptagen/generate?words=ONE,TWO,THREE&operator=+
     */
    @GetMapping("/generate")
    public ResponseEntity<GenerateResponse> generateSimple(
            @RequestParam String words,
            @RequestParam(defaultValue = "+") String operator,
            @RequestParam(defaultValue = "5") Integer limit) {
        
        GenerateResponse response = cryptagenService.generateCryptarithms(
            null, // No taskId for simple GET endpoint
            Arrays.asList(words.split(",")),
            operator,
            limit,
            60,
            false,
            null,
            null,
            null,
            null,
            false,
            "UNIQUE",
            null,
            null,
            false,
            1,
            null,
            false // allowLeadingZeros
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Generate doubly-true cryptarithms (number words)
     * POST /api/v1/cryptagen/generate-doubly-true
     * 
     * Example request body:
     * {
     *   "countryCode": "FR",
     *   "langCode": "fr",
     *   "lowerBound": 1,
     *   "upperBound": 100,
     *   "operatorSymbol": "+",
     *   "solutionLimit": 5
     * }
     */
    @PostMapping("/generate-doubly-true")
    public ResponseEntity<GenerateResponse> generateDoublyTrue(@Valid @RequestBody GenerateRequest request) {
        if (request.getCountryCode() == null || request.getLangCode() == null 
            || request.getLowerBound() == null || request.getUpperBound() == null) {
            GenerateResponse errorResponse = new GenerateResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError("For doubly-true generation, countryCode, langCode, lowerBound, and upperBound are required");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        GenerateResponse response = cryptagenService.generateCryptarithms(
            request.getTaskId(),
            null,
            request.getOperatorSymbol(),
            request.getSolutionLimit(),
            request.getTimeLimit(),
            request.getShuffle(),
            request.getCountryCode(),
            request.getLangCode(),
            request.getLowerBound(),
            request.getUpperBound(),
            request.getDryRun(),
            request.getRightMemberType(),
            request.getMinWords(),
            request.getMaxWords(),
            request.getLightPropagation(),
            request.getThreads(),
            request.getCrossGridSize(),
            request.getAllowLeadingZeros()
        );
        
        return ResponseEntity.ok(response);
    }
}
