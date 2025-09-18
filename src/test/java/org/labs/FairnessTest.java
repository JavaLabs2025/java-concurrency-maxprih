package org.labs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class FairnessTest {

    @Test
    @DisplayName("Fair distribution with N-1 waiters")
    public void testFairDistributionEqual() {
        DiningSimulation simulation = new DiningSimulation(10, 10000, 9);
        simulation.start();
        
        Statistics stats = simulation.getStatistics();
        int[] eatenByProgrammer = stats.getAllEatenAmounts();
        
        assertFairDistribution(eatenByProgrammer, 0.3);
    }

    @Test
    @DisplayName("Fair distribution with fewer waiters")
    public void testFairDistributionFewerWaiters() {
        DiningSimulation simulation = new DiningSimulation(20, 5000, 5);
        simulation.start();
        
        Statistics stats = simulation.getStatistics();
        int[] eatenByProgrammer = stats.getAllEatenAmounts();
        
        assertFairDistribution(eatenByProgrammer, 0.3);
    }

    @ParameterizedTest
    @CsvSource({
        "7, 2000, 3",
        "10, 4000, 3",
        "15, 6000, 5",
        "30, 8000, 10"
    })
    @DisplayName("Fairness across different configurations")
    public void testFairnessVariousConfigs(int programmers, int food, int waiters) {
        DiningSimulation simulation = new DiningSimulation(programmers, food, waiters);
        simulation.start();
        
        Statistics stats = simulation.getStatistics();
        int[] eatenByProgrammer = stats.getAllEatenAmounts();
        
        assertFairDistribution(eatenByProgrammer, 0.3);
    }

    @Test
    @DisplayName("No starvation - every programmer eats something")
    public void testNoStarvation() {
        DiningSimulation simulation = new DiningSimulation(15, 3000, 4);
        simulation.start();
        
        Statistics stats = simulation.getStatistics();
        int[] eatenByProgrammer = stats.getAllEatenAmounts();
        
        for (int i = 0; i < eatenByProgrammer.length; i++) {
            assertTrue(eatenByProgrammer[i] > 0, "Programmer " + i + " should have eaten at least something");
        }
    }

    @Test
    @DisplayName("Fairness with high contention")
    public void testFairnessHighContention() {
        DiningSimulation simulation = new DiningSimulation(30, 15000, 3);
        simulation.start();
        
        Statistics stats = simulation.getStatistics();
        int[] eatenByProgrammer = stats.getAllEatenAmounts();
        
        Arrays.sort(eatenByProgrammer);
        int min = eatenByProgrammer[0];
        int max = eatenByProgrammer[eatenByProgrammer.length - 1];
        
        assertTrue(min > 0, "Even with high contention, everyone should eat something");
        
        assertTrue(max <= min * 3, "Max eaten shouldn't be more than 3x minimum eaten");
    }

    private void assertFairDistribution(int[] eatenByProgrammer, double tolerance) {
        int total = Arrays.stream(eatenByProgrammer).sum();
        double expected = (double) total / eatenByProgrammer.length;
        
        Arrays.sort(eatenByProgrammer);
        int min = eatenByProgrammer[0];
        int max = eatenByProgrammer[eatenByProgrammer.length - 1];
        
        double fairnessRatio = (double) min / max;
        double expectedRatio = 1.0 - tolerance;
        
        assertTrue(fairnessRatio >= expectedRatio, String.format("Fairness ratio %.3f should be >= %.3f (min=%d, max=%d)", fairnessRatio, expectedRatio, min, max));
        
        for (int eaten : eatenByProgrammer) {
            double deviation = Math.abs(eaten - expected) / expected;
            assertTrue(deviation <= tolerance, String.format("Deviation %.3f should be <= %.3f for eaten=%d, expected=%.1f", deviation, tolerance, eaten, expected));
        }
    }
}
