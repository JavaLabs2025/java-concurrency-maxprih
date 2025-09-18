package org.labs;

public class Main {
    public static void main(String[] args) {
        int programmersCount = 7;
        int totalFood = 1_000_000;
        int waitersCount = 2;

        try {
            if (args.length >= 1) programmersCount = Integer.parseInt(args[0]);
            if (args.length >= 2) totalFood = Integer.parseInt(args[1]);
            if (args.length >= 3) waitersCount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("Некорректные параметры. Используем значения по умолчанию.");
        }

        if (programmersCount <= 0 || totalFood <= 0 || waitersCount <= 0) {
            System.err.println("Все параметры должны быть положительными числами!");
            return;
        }

        if (waitersCount > programmersCount) {
            System.out.println("Предупреждение: официантов больше чем программистов");
            return;
        }

        DiningSimulation simulation = new DiningSimulation(programmersCount, totalFood, waitersCount);
        simulation.start();
    }
}
