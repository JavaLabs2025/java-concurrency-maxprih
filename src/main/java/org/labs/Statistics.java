package org.labs;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    private final AtomicInteger[] eatenByProgrammer;
    private final int totalFood;
    private final AtomicInteger remainingFood;

    public Statistics(int programmersCount, int totalFood, AtomicInteger remainingFood) {
        this.totalFood = totalFood;
        this.remainingFood = remainingFood;
        this.eatenByProgrammer = new AtomicInteger[programmersCount];

        for (int i = 0; i < programmersCount; i++) {
            eatenByProgrammer[i] = new AtomicInteger(0);
        }
    }

    public void incrementEaten(int programmerId) {
        eatenByProgrammer[programmerId].incrementAndGet();
    }

    public int[] getAllEatenAmounts() {
        return Arrays.stream(eatenByProgrammer)
                .mapToInt(AtomicInteger::get)
                .toArray();
    }

    public int getTotalEaten() {
        return Arrays.stream(eatenByProgrammer)
                .mapToInt(AtomicInteger::get)
                .sum();
    }

    public int getRemainingFood() {
        return remainingFood.get();
    }

    public void printStatistics() {
        System.out.println("\n=== Статистика обеда ===");
        System.out.println("Всего было порций: " + totalFood);
        System.out.println("Осталось порций: " + remainingFood.get());

        int totalEaten = getTotalEaten();
        for (int i = 0; i < eatenByProgrammer.length; i++) {
            int eaten = eatenByProgrammer[i].get();
            System.out.println("Программист " + i + " съел: " + eaten + " порций");
        }

        System.out.println("Общий счетчик съеденного: " + totalEaten);

        analyzeFairness();
    }

    private void analyzeFairness() {
        int[] portions = getAllEatenAmounts();
        Arrays.sort(portions);
        int min = portions[0];
        int max = portions[portions.length - 1];
        int difference = max - min;

        System.out.println("Разница между min и max: " + difference + " порций");
        if (max > 0) {
            System.out.println("Коэффициент справедливости: " +
                    String.format("%.2f%%", (1.0 - (double) difference / max) * 100));
        }
    }
}
