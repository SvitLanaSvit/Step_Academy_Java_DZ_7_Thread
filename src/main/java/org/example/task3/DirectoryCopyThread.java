package org.example.task3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class DirectoryCopyThread extends Thread {
    private final File source;
    private final File destination;
    private AtomicInteger fileCount;

    public DirectoryCopyThread(File source, File destination, AtomicInteger fileCount) {
        this.source = source;
        this.destination = destination;
        this.fileCount = fileCount;
    }

    @Override
    public void run() {
        try {
            copyDirectory(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyDirectory(File sourceDir, File destDir) throws IOException {
        if (sourceDir.isDirectory()) {
            if (!destDir.exists()) {
                destDir.mkdir();
            }
            String[] children = sourceDir.list();
            if (children != null) {
                for (String child : children) {
                    copyDirectory(new File(sourceDir, child), new File(destDir, child));
                }
            }
        } else {
            Files.copy(sourceDir.toPath(), destDir.toPath(), StandardCopyOption.REPLACE_EXISTING);
            fileCount.getAndIncrement();
        }
    }

    public static void main(String[] args) {
        //Завдання 3:
        //Користувач з клавіатури вводить шлях до існуючої директорії і до нової директорії. Після чого запускається
        //потік, що має скопіювати вміст директорії в нове місце. Необхідно зберегти структуру директорії. В методі
        //main необхідно відобразити статистику виконаних операцій.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the source directory path:");
        String sourceDirPath = scanner.next();
        System.out.println("Enter the destination directory path:");
        String destDirPath = scanner.next();

        File sourceDir = new File(sourceDirPath);
        File destDir = new File(destDirPath);

        if (!sourceDir.exists()) {
            System.out.println("Source directory does not exist.");
            return;
        }

        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        AtomicInteger fileCount = new AtomicInteger(0);

        DirectoryCopyThread copyThread = new DirectoryCopyThread(sourceDir, destDir, fileCount);
        copyThread.start();

        try {
            copyThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int sourceContents = countFilesInDirectory(sourceDir);
        int destContents = countFilesInDirectory(destDir);

        System.out.println("Statistics:");
        System.out.println("Source directory '" + sourceDirPath + "' contains " + sourceContents + " files.");
        System.out.println("Destination directory '" + destDirPath + "' contains " + destContents + " files.");
    }

    private static int countFilesInDirectory(File directory) {
        if (directory.isDirectory()) {
            return Objects.requireNonNull(directory.listFiles()).length;
        } else {
            return 1;
        }
    }
}
