/*
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021-2026, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.api.service;

import cryptator.api.dto.SolveResponse;
import cryptator.cmd.CryptaBiConsumer;
import cryptator.config.CryptaCmdConfig;
import cryptator.config.CryptatorConfig;
import cryptator.parser.CryptaParserException;
import cryptator.parser.CryptaParserWrapper;
import cryptator.solver.CryptaModelException;
import cryptator.solver.CryptaSolverException;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaSolution;
import cryptator.specs.ICryptaSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

import static cryptator.Cryptator.createSolver;
import static cryptator.Cryptator.parseCryptarithm;

/**
 * Service for solving cryptarithms
 */
@Service
public class CryptatorService {

    @Autowired
    private TaskManager taskManager;

    public SolveResponse solveCryptarithm(String taskIdFromClient, String cryptarithm, String solverTypeStr, 
                                         int solutionLimit, int timeLimit,
                                         int arithmeticBase, boolean checkSolution,
                                         boolean exportGraphviz, boolean allowLeadingZeros,
                                         boolean hornerScheme, java.util.Map<String, Integer> assignments) {
        
        SolveResponse response = new SolveResponse();
        response.setCryptarithm(cryptarithm);
        
        // Use client-provided taskId if available, otherwise generate one
        String taskId = (taskIdFromClient != null && !taskIdFromClient.trim().isEmpty()) 
            ? taskIdFromClient 
            : taskManager.registerTask("SOLVE");
        
        // Register with the provided or generated taskId
        if (taskIdFromClient != null && !taskIdFromClient.trim().isEmpty()) {
            taskManager.registerTask("SOLVE", taskId);
        }
        
        response.setTaskId(taskId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Configure the solver
            CryptatorConfig config = new CryptatorConfig();
            config.setSolverType(CryptaCmdConfig.SolverType.valueOf(solverTypeStr));
            config.setSolutionLimit(solutionLimit);
            config.setTimeLimit(timeLimit);
            config.setArithmeticBase(arithmeticBase);
            config.setAllowLeadingZeros(allowLeadingZeros);
            config.setHornerScheme(hornerScheme);
            
            // Note: Manual assignments (--assign) are not directly supported via config
            // This would require solver-level API access not exposed in CryptatorConfig
            if (assignments != null && !assignments.isEmpty()) {
                System.out.println("Warning: Manual assignments requested but not yet implemented in API");
                // TODO: Implement solver.assign() if needed
            }
            
            // Parse cryptarithm
            CryptaParserWrapper parser = new CryptaParserWrapper();
            Logger tempLogger = Logger.getLogger("TempLogger");
            tempLogger.setUseParentHandlers(false);
            ICryptaNode node = parseCryptarithm(cryptarithm, parser, tempLogger);
            
            // Create solver
            ICryptaSolver solver = createSolver(config);
            solver.limitSolution(solutionLimit);
            solver.limitTime(timeLimit);
            
            // Capture solutions
            SolutionCapturingConsumer consumer = new SolutionCapturingConsumer(checkSolution, arithmeticBase, node, taskId, taskManager);
            boolean solved = solver.solve(node, config, consumer);
            
            // Check if cancelled
            if (taskManager.isCancelled(taskId)) {
                response.setSuccess(false);
                response.setError("Task cancelled by user");
            } else {
                response.setSuccess(solved);
                response.setSolutions(consumer.getSolutions());
                response.setSolutionCount(consumer.getSolutions().size());
            }
            
        } catch (CryptaParserException e) {
            response.setSuccess(false);
            response.setError("Parse error: " + e.getMessage());
        } catch (CryptaModelException e) {
            response.setSuccess(false);
            response.setError("Model error: " + e.getMessage());
        } catch (CryptaSolverException e) {
            response.setSuccess(false);
            response.setError("Solver error: " + e.getMessage());
        } catch (RuntimeException e) {
            // Check if it's a cancellation exception
            if (e.getMessage() != null && e.getMessage().contains("cancelled")) {
                response.setSuccess(false);
                response.setError("Task cancelled by user");
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
     * Custom consumer to capture solutions
     */
    private static class SolutionCapturingConsumer extends CryptaBiConsumer {
        
        private final List<SolveResponse.Solution> solutions = new ArrayList<>();
        private final ICryptaNode lastNode;
        private final String taskId;
        private final TaskManager taskManager;
        
        public SolutionCapturingConsumer(boolean checkSolution, int base, ICryptaNode node, String taskId, TaskManager taskManager) {
            super(Logger.getLogger("SolutionLogger"));
            this.lastNode = node;
            this.taskId = taskId;
            this.taskManager = taskManager;
            if (checkSolution) {
                this.withSolutionCheck(base);
            }
        }
        
        @Override
        public void accept(ICryptaNode node, ICryptaSolution solution) {
            // Check if task is cancelled before processing
            if (taskManager.isCancelled(taskId)) {
                // Throw exception to stop the solver immediately
                throw new RuntimeException("Task cancelled by user");
            }
            
            super.accept(node, solution);
            
            try {
                // Get assignment and evaluation strings from the solution
                String assignment = solution.toString();
                String evaluation = cryptator.tree.TreeUtils.writeInorder(node) + " = " + solution.toString();
                
                SolveResponse.Solution sol = new SolveResponse.Solution();
                sol.setAssignment(assignment);
                sol.setEvaluation(evaluation);
                sol.setValid(getErrorCount() == 0);
                
                solutions.add(sol);
            } catch (Exception e) {
                // Log error but continue
            }
        }
        
        public List<SolveResponse.Solution> getSolutions() {
            return solutions;
        }
    }
}
