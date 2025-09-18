package org.labs;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Programmer extends Thread {
    private final int id;
    private final Spoon firstSpoon;
    private final Spoon secondSpoon;
    private final Semaphore waiters;
    private final AtomicInteger remainingFood;
    private final Statistics statistics;

    public Programmer(int id, Spoon[] spoons, Semaphore waiters,
                      AtomicInteger remainingFood, Statistics statistics) {
        this.id = id;
        this.waiters = waiters;
        this.remainingFood = remainingFood;
        this.statistics = statistics;

        int leftSpoonId = id;
        int rightSpoonId = (id + 1) % spoons.length;

        if (leftSpoonId < rightSpoonId) {
            this.firstSpoon = spoons[leftSpoonId];
            this.secondSpoon = spoons[rightSpoonId];
        } else {
            this.firstSpoon = spoons[rightSpoonId];
            this.secondSpoon = spoons[leftSpoonId];
        }

        setName("Programmer-" + id);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                think();

                if (!tryToEat()) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Программист " + id + " закончил обед");
    }

    private void think() throws InterruptedException {
        Thread.sleep(ThreadLocalRandom.current().nextInt(1, 3));
    }

    private boolean tryToEat() throws InterruptedException {
        waiters.acquire();

        try {
            firstSpoon.lock();
            try {
                secondSpoon.lock();
                try {
                    return eatFood();
                } finally {
                    secondSpoon.unlock();
                }
            } finally {
                firstSpoon.unlock();
            }
        } finally {
            waiters.release();
        }
    }

    private boolean eatFood() throws InterruptedException {
        int foodLeft = remainingFood.decrementAndGet();

        if (foodLeft >= 0) {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1, 3));
            statistics.incrementEaten(id);
            return true;
        } else {
            remainingFood.incrementAndGet();
            return false;
        }
    }
}
