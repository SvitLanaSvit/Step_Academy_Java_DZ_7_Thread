package org.example.task1;

import java.util.Arrays;
import java.util.Random;

public class ArraySumAverageCalculation {
    private static int[] array;
    private static int sum = 0;
    private static double average = 0;

    public static void getMenu() {
        int arraySize = 10;
        array = new int[arraySize];

        Thread fillThread = new Thread(new FillArrayThread());
        Thread sumThread = new Thread(new SumArrayThread());
        Thread averageThread = new Thread(new AverageArrayThread());

        fillThread.start();

        try {
            fillThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sumThread.start();
        averageThread.start();

        try {
            sumThread.join();
            averageThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Array: " + Arrays.toString(array));
        System.out.println("Sum: " + sum);
        System.out.println("Mean: " + average);
    }

    static class FillArrayThread implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            for (int i = 0; i < array.length; i++) {
                array[i] = random.nextInt(100);
            }
        }
    }

    static class SumArrayThread implements Runnable {
        @Override
        public void run() {
            sum = Arrays.stream(array).sum();
        }
    }

    static class AverageArrayThread implements Runnable {
        @Override
        public void run() {
            average = Arrays.stream(array).average().orElse(0.0);
        }
    }
}