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
public class CryptatorController {

    @Autowired
    private CryptatorService cryptatorService;

    /**
     * Health check endpoint
     */
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
    @GetMapping("/solve")
    public ResponseEntity<SolveResponse> solveSimple(
            @RequestParam String cryptarithm,
            @RequestParam(defaultValue = "SCALAR") String solverType,
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
