package org.labs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeCaseTest {

    @Test
    @DisplayName("Single programmer scenario")
    public void testSingleProgrammer() {
        DiningSimulation simulation = new DiningSimulation(1, 100, 1);
        simulation.start();
        
        Statistics stats = simulation.getStatistics();
        int[] eatenByProgrammer = stats.getAllEatenAmounts();
        
        assertEquals(100, eatenByProgrammer[0], "Single programmer should eat all food");
        assertEquals(0, stats.getRemainingFood(), "No food should remain");
    }

    @Test
    @DisplayName("More waiters than programmers")
    public void testMoreWaitersThanProgrammers() {
        DiningSimulation simulation = new DiningSimulation(5, 500, 10);
        assertDoesNotThrow(simulation::start);
        
        Statistics stats = simulation.getStatistics();
        assertEquals(0, stats.getRemainingFood(), "All food should be consumed");
    }

    @Test
    @DisplayName("Very small amount of food")
    public void testVerySmallFood() {
        DiningSimulation simulation = new DiningSimulation(10, 5, 5);
        simulation.start();
        
        Statistics stats = simulation.getStatistics();
        int totalEaten = stats.getTotalEaten();
        
        assertTrue(totalEaten <= 5, "Cannot eat more than available");
        assertEquals(0, stats.getRemainingFood(), "All available food should be eaten");
    }

    @Test
    @DisplayName("Zero food scenario")
    public void testZeroFood() {
        DiningSimulation simulation = new DiningSimulation(5, 0, 3);
        simulation.start();
        
        Statistics stats = simulation.getStatistics();
        int totalEaten = stats.getTotalEaten();
        
        assertEquals(0, totalEaten, "No food should be eaten when none available");
    }

    @Test
    @DisplayName("Single waiter bottleneck")
    public void testSingleWaiterBottleneck() {
        DiningSimulation simulation = new DiningSimulation(20, 2000, 1);
        assertDoesNotThrow(simulation::start);
        
        Statistics stats = simulation.getStatistics();
        assertEquals(0, stats.getRemainingFood(), "All food should eventually be consumed");
    }

    @Test
    @DisplayName("Large number of programmers")
    public void testLargeNumberOfProgrammers() {
        DiningSimulation simulation = new DiningSimulation(100, 5000, 50);
        assertDoesNotThrow(simulation::start);
        
        Statistics stats = simulation.getStatistics();
        int totalEaten = stats.getTotalEaten();
        
        assertEquals(5000, totalEaten, "All food should be consumed");
    }

    @Test
    @DisplayName("Stress test - many programmers, little food")
    public void testStressScenario() {
        DiningSimulation simulation = new DiningSimulation(50, 100, 10);
        simulation.start();
        
        Statistics stats = simulation.getStatistics();
        int[] eatenByProgrammer = stats.getAllEatenAmounts();
        
        int programmersWhoAte = 0;
        for (int eaten : eatenByProgrammer) {
            if (eaten > 0) programmersWhoAte++;
        }
        
        assertTrue(programmersWhoAte > 0, "At least some programmers should eat");
        assertTrue(programmersWhoAte <= 100, "Cannot have more food than available");
    }
}
