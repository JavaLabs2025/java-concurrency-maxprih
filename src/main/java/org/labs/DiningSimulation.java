package org.labs;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class DiningSimulation {
    private final int programmersCount;
    private final int totalFood;
    private final int waitersCount;

    private final Spoon[] spoons;
    private final Semaphore waiters;
    private final AtomicInteger remainingFood;
    private final Statistics statistics;

    public DiningSimulation(int programmersCount, int totalFood, int waitersCount) {
        this.programmersCount = programmersCount;
        this.totalFood = totalFood;
        this.waitersCount = waitersCount;

        this.spoons = new Spoon[programmersCount];
        for (int i = 0; i < programmersCount; i++) {
            spoons[i] = new Spoon(i);
        }

        this.waiters = new Semaphore(waitersCount, true);
        this.remainingFood = new AtomicInteger(totalFood);
        this.statistics = new Statistics(programmersCount, totalFood, remainingFood);
    }

    public void start() {
        System.out.println("Запуск симуляции:");
        System.out.println("Программистов: " + programmersCount);
        System.out.println("Порций еды: " + totalFood);
        System.out.println("Официантов: " + waitersCount);
        System.out.println("Начинаем обед...\n");

        Thread[] programmers = new Thread[programmersCount];
        for (int i = 0; i < programmersCount; i++) {
            programmers[i] = new Programmer(i, spoons, waiters, remainingFood, statistics);
        }

        long startTime = System.currentTimeMillis();
        for (Thread programmer : programmers) {
            programmer.start();
        }
        waitForCompletion(programmers);
        long endTime = System.currentTimeMillis();

        statistics.printStatistics();
        System.out.println("\nВремя выполнения: " + (endTime - startTime) + " мс");
    }

    private void waitForCompletion(Thread[] programmers) {
        for (Thread programmer : programmers) {
            try {
                programmer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                for (Thread p : programmers) {
                    p.interrupt();
                }
                break;
            }
        }
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
