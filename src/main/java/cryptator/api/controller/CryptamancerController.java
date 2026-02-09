/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.controller;

import cryptator.api.dto.CryptamancerHintResponse;
import cryptator.api.dto.CryptamancerRequest;
import cryptator.api.service.CryptamancerService;
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

/**
 * REST Controller for Cryptamancer game mode
 */
@RestController
@RequestMapping("/api/v1/cryptamancer")
@CrossOrigin(origins = "*")
@Tag(name = "Cryptamancer", description = "Interactive game mode with hints and validation")
public class CryptamancerController {

    @Autowired
    private CryptamancerService cryptamancerService;

    /**
     * Get a hint based on current attempt
     * POST /api/v1/cryptamancer/hint
     * 
     * Example request body:
     * {
     *   "cryptarithm": "send+more=money",
     *   "attempt": {"s": 9, "e": 5},
     *   "hintLevel": 1
     * }
     */
    @Operation(summary = "Get a hint", 
               description = "Get a progressive hint based on current attempt and hint level")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hint provided",
                     content = @Content(schema = @Schema(implementation = CryptamancerHintResponse.class)))
    })
    @PostMapping("/hint")
    public ResponseEntity<CryptamancerHintResponse> getHint(@Valid @RequestBody CryptamancerRequest request) {
        CryptamancerHintResponse response = cryptamancerService.getHint(
            request.getCryptarithm(),
            request.getAttempt(),
            request.getHintLevel() != null ? request.getHintLevel() : 1
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Validate a complete solution
     * POST /api/v1/cryptamancer/validate
     * 
     * Example request body:
     * {
     *   "cryptarithm": "send+more=money",
     *   "attempt": {"s": 9, "e": 5, "n": 6, "d": 7, "m": 1, "o": 0, "r": 8, "y": 2}
     * }
     */
    @PostMapping("/validate")
    public ResponseEntity<CryptamancerHintResponse> validateSolution(@Valid @RequestBody CryptamancerRequest request) {
        CryptamancerHintResponse response = cryptamancerService.validateSolution(
            request.getCryptarithm(),
            request.getAttempt()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get just a hint without attempt (for new game)
     * GET /api/v1/cryptamancer/hint?cryptarithm=send+more=money&level=1
     */
    @GetMapping("/hint")
    public ResponseEntity<CryptamancerHintResponse> getHintSimple(
            @RequestParam String cryptarithm,
            @RequestParam(defaultValue = "1") Integer level) {
        
        CryptamancerHintResponse response = cryptamancerService.getHint(
            cryptarithm,
            null,
            level
        );
        
        return ResponseEntity.ok(response);
    }
}
