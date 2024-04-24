package com.matainja.bootapplication;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Logger {
    private static final String TAG = "MyLogger";
    private boolean loggingEnabled = true; // Flag to control logging loop

    public void logAppStatus(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String currentProcessName = getCurrentProcessName(activityManager);
        if (context.getApplicationInfo().processName.equals(currentProcessName)) {
            Log.d(TAG, "App is running");
        } else {
            Log.d(TAG, "App has crashed intentionally"); // Modify the log message
            saveCrashLog(context);

        }
    }

    private String getCurrentProcessName(ActivityManager activityManager) {
        String currentProcessName = null;
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses != null && !runningAppProcesses.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
                if (runningAppProcess.pid == Process.myPid()) {
                    currentProcessName = runningAppProcess.processName;
                    break;
                }
            }
        }
        return currentProcessName;
    }

    public void startLogging(final Activity activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File logFile = new File(activity.getExternalFilesDir(null), "cpu_memory_log.txt");
                    FileWriter fileWriter = new FileWriter(logFile, true);
                    while (loggingEnabled) {
                        logAppStatus(activity);
                        logCPUUsage(fileWriter);
                        logMemoryUsage(activity);
                        fileWriter.write("App status: ");
                        fileWriter.write(TAG);
                        fileWriter.write("\n");
                        fileWriter.flush();
                        Thread.sleep(5000);
                    }
                    fileWriter.close(); // Close file writer when logging stops
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopLogging() {
        loggingEnabled = false; // Stop the logging loop
    }

    public void logCPUUsage(FileWriter fileWriter) throws IOException {
        long startTime = System.nanoTime();
        long startCpuTime = Process.getElapsedCpuTime();

        // Perform some CPU-intensive task here

        long endTime = System.nanoTime();
        long endCpuTime = Process.getElapsedCpuTime();

        long elapsedTime = endTime - startTime;
        long elapsedCpuTime = endCpuTime - startCpuTime;

        double cpuUsage = (double) elapsedCpuTime / elapsedTime * 100;

        Log.d(TAG, "CPU usage: " + cpuUsage + "%");
        fileWriter.write("CPU usage: " + cpuUsage + "%\n");
    }

    public void logMemoryUsage(Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long totalMemory = memoryInfo.totalMem;
        long freeMemory = memoryInfo.availMem;
        long usedMemory = totalMemory - freeMemory;
        Log.d("Memory Usage", "Total Memory: " + totalMemory + " bytes");
        Log.d("Memory Usage", "Free Memory: " + freeMemory + " bytes");
        Log.d("Memory Usage", "Used Memory: " + usedMemory + " bytes");
    }
    private void saveCrashLog(Context context) {
        try {
            File logFile = new File(context.getExternalFilesDir(null), "neoSign_crash_log.txt");
            FileWriter fileWriter = new FileWriter(logFile, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("Crash occurred: " + System.currentTimeMillis());
            printWriter.println("Stack trace:");

            // Get the stack trace and print it to the file
            Throwable throwable = new Throwable();
            throwable.printStackTrace(printWriter);

            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
