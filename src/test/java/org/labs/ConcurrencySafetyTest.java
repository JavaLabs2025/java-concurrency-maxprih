package org.labs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

public class ConcurrencySafetyTest {

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    @DisplayName("No deadlock with minimal resources")
    public void testNoDeadlockMinimal() {
        DiningSimulation simulation = new DiningSimulation(5, 100, 1);
        assertDoesNotThrow(simulation::start);
    }

    @Test
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    @DisplayName("No deadlock with many programmers")
    public void testNoDeadlockManyProgrammers() {
        DiningSimulation simulation = new DiningSimulation(50, 1000, 25);
        assertDoesNotThrow(simulation::start);
    }

    @RepeatedTest(10)
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    @DisplayName("Repeated execution without deadlock")
    public void testRepeatedExecution() {
        DiningSimulation simulation = new DiningSimulation(10, 500, 5);
        assertDoesNotThrow(simulation::start);
    }

    @Test
    @DisplayName("Total food consumed equals initial food")
    public void testFoodConservation() {
        DiningSimulation simulation = new DiningSimulation(8, 1000, 6);
        simulation.start();
        
        Statistics stats = simulation.getStatistics();
        int totalEaten = stats.getTotalEaten();
        int remainingFood = stats.getRemainingFood();

        assertEquals(1000, totalEaten + remainingFood, "Total eaten + remaining should equal initial food");
        assertTrue(remainingFood >= 0, "Remaining food cannot be negative");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 20, 50})
    @DisplayName("Different programmer counts don't cause issues")
    public void testVariousProgrammerCounts(int programmerCount) {
        int food = programmerCount * 100;
        int waiters = Math.max(1, programmerCount - 1);
        
        DiningSimulation simulation = new DiningSimulation(programmerCount, food, waiters);
        assertDoesNotThrow(simulation::start);
        
        Statistics stats = simulation.getStatistics();
        int totalEaten = stats.getTotalEaten();
        
        assertTrue(totalEaten <= food, "Cannot eat more food than available");
        assertTrue(totalEaten >= 0, "Cannot eat negative food");
    }

}
