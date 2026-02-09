/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.controller;

import cryptator.api.dto.SolveRequest;
import cryptator.api.dto.SolveResponse;
import cryptator.api.service.CryptatorService;
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
 * REST Controller for solving cryptarithms
 */
@RestController
@RequestMapping("/api/v1/cryptator")
@CrossOrigin(origins = "*")
@Tag(name = "Cryptator", description = "Solve cryptarithms using constraint programming")
public class CryptatorController {

    @Autowired
    private CryptatorService cryptatorService;

    /**
     * Health check endpoint
     */
    @Operation(summary = "Health check", description = "Check if the API is running")
    @ApiResponse(responseCode = "200", description = "API is operational")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Cryptator API is running");
    }

    /**
     * Solve a cryptarithm
     * POST /api/v1/cryptator/solve
     * 
     * Example request body:
     * {
     *   "cryptarithm": "send+more=money",
     *   "solverType": "SCALAR",
     *   "solutionLimit": 10,
     *   "timeLimit": 30,
     *   "arithmeticBase": 10,
     *   "checkSolution": true,
     *   "exportGraphviz": false
     * }
     */
    @Operation(summary = "Solve a cryptarithm", 
               description = "Solve a cryptarithm puzzle using constraint programming. Returns all solutions up to the specified limit.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully solved",
                     content = @Content(schema = @Schema(implementation = SolveResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Solver error")
    })
    @PostMapping("/solve")
    public ResponseEntity<SolveResponse> solve(@Valid @RequestBody SolveRequest request) {
        SolveResponse response = cryptatorService.solveCryptarithm(
            request.getTaskId(),
            request.getCryptarithm(),
            request.getSolverType(),
            request.getSolutionLimit(),
            request.getTimeLimit(),
            request.getArithmeticBase(),
            request.getCheckSolution(),
            request.getExportGraphviz(),
            request.getAllowLeadingZeros() != null ? request.getAllowLeadingZeros() : false,
            request.getHornerScheme() != null ? request.getHornerScheme() : false,
            request.getAssignments()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Quick solve endpoint with minimal parameters
     * GET /api/v1/cryptator/solve?cryptarithm=send+more=money
     */
    @Operation(summary = "Quick solve (GET)", 
               description = "Simplified endpoint to solve a cryptarithm with default parameters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully solved"),
        @ApiResponse(responseCode = "500", description = "Solver error")
    })
    @GetMapping("/solve")
    public ResponseEntity<SolveResponse> solveSimple(
            @Parameter(description = "Cryptarithm to solve (e.g., send+more=money)", required = true)
            @RequestParam String cryptarithm,
            @Parameter(description = "Solver type (SCALAR, VECTOR, TABLE)", example = "SCALAR")
            @RequestParam(defaultValue = "SCALAR") String solverType,
            @Parameter(description = "Maximum number of solutions (0 = all)", example = "0")
            @RequestParam(defaultValue = "0") Integer solutionLimit) {
        
        SolveResponse response = cryptatorService.solveCryptarithm(
            null, // No taskId for simple GET endpoint
            cryptarithm,
            solverType,
            solutionLimit,
            0,
            10,
            false,
            false,
            false,
            false,
            null
        );
        
        return ResponseEntity.ok(response);
    }
}
