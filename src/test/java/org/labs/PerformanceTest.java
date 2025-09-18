package org.labs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

public class PerformanceTest {

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("Performance test - should complete within time limit")
    public void testPerformanceSmall() {
        DiningSimulation simulation = new DiningSimulation(10, 10000, 8);
        
        long startTime = System.currentTimeMillis();
        simulation.start();
        long endTime = System.currentTimeMillis();
        
        long duration = endTime - startTime;
        assertTrue(duration < 10000, "Should complete within 10 seconds");
        
        Statistics stats = simulation.getStatistics();
        assertEquals(0, stats.getRemainingFood(), "All food should be consumed");
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    @DisplayName("Performance test - large scenario")
    public void testPerformanceLarge() {
        DiningSimulation simulation = new DiningSimulation(50, 50000, 45);
        
        long startTime = System.currentTimeMillis();
        simulation.start();
        long endTime = System.currentTimeMillis();
        
        long duration = endTime - startTime;
        assertTrue(duration < 30000, "Should complete within 30 seconds");
        
        Statistics stats = simulation.getStatistics();
        assertEquals(0, stats.getRemainingFood(), "All food should be consumed");
    }

    @Test
    @DisplayName("Throughput test")
    public void testThroughput() {
        int programmers = 20;
        int food = 100000;
        int waiters = 15;
        
        DiningSimulation simulation = new DiningSimulation(programmers, food, waiters);
        
        long startTime = System.currentTimeMillis();
        simulation.start();
        long endTime = System.currentTimeMillis();
        
        long duration = endTime - startTime;
        double throughput = (double) food / (duration / 1000.0);
        
        assertTrue(throughput > 1000, "Should achieve reasonable throughput (>1000 food/sec)");
        System.out.println("Throughput: " + throughput + " food/second");
    }
}
