package org.example.task2;

import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.*;

public class ThreadFileOperations {
    private static final int FILE_SIZE = 5; // You can change the file size as needed
    private static final int MAX_RANDOM_NUMBER = 5; // Max random number
    private static String filePath;
    private static final String primeFilePath = "prime.txt"; // File path for prime numbers
    private static final String factorialFilePath = "factorial.txt"; // File path for factorials

    private static AtomicInteger primeCount = new AtomicInteger(0);
    private static AtomicLong factorialSum = new AtomicLong(0);
    private static AtomicBoolean isFileFilled = new AtomicBoolean(false);

    public static void getMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file path: ");
        filePath = scanner.next();

        Thread fillThread = new Thread(new FillFileThread());
        Thread primeThread = new Thread(new PrimeThread());
        Thread factorialThread = new Thread(new FactorialThread());

        fillThread.start();
        primeThread.start();
        factorialThread.start();

        try {
            fillThread.join();
            primeThread.join();
            factorialThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class FillFileThread implements Runnable {
        @Override
        public void run() {
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                Random random = new Random();
                for (int i = 0; i < FILE_SIZE; i++) {
                    int randomNumber = random.nextInt(MAX_RANDOM_NUMBER) + 1;
                    fileWriter.write(randomNumber + "\n");
                }
                isFileFilled.set(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class PrimeThread implements Runnable {
        @Override
        public void run() {
            while (!isFileFilled.get()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
                 FileWriter primeWriter = new FileWriter(primeFilePath)) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    int num = Integer.parseInt(line);
                    if (isPrime(num)) {
                        primeWriter.write(num + "\n");
                        primeCount.incrementAndGet();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class FactorialThread implements Runnable {
        @Override
        public void run() {
            while(!isFileFilled.get()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            FileWriter factorialWriter = new FileWriter(factorialFilePath)){
                String line;

                while((line = bufferedReader.readLine()) != null){
                    int num = Integer.parseInt(line);
                    long factorial = calculateFactorial(num);
                    factorialWriter.write(factorial + "\n");
                    factorialSum.addAndGet(factorial);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private long calculateFactorial(int n) {
            if (n == 0) {
                return 1;
            } else {
                return n * calculateFactorial(n - 1);
            }
        }
    }

    private static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
