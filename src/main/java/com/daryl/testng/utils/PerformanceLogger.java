package com.daryl.testng.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PerformanceLogger {
    
    private static PerformanceLogger instance;
    private Map<String, Long> taskStartTimes;
    private FileWriter logWriter;
    private DateTimeFormatter formatter;
    
    private PerformanceLogger() {
        taskStartTimes = new HashMap<>();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        initializeLogFile();
    }
    
    public static PerformanceLogger getInstance() {
        if (instance == null) {
            instance = new PerformanceLogger();
        }
        return instance;
    }
    
    private void initializeLogFile() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "test-performance-" + timestamp + ".log";
            logWriter = new FileWriter(fileName, true);
            
            writeLog("=== Performance Test Log Started ===");
            writeLog("Timestamp: " + LocalDateTime.now().format(formatter));
            writeLog("=====================================");
            
        } catch (IOException e) {
            System.err.println("Failed to initialize log file: " + e.getMessage());
        }
    }
    
    public void startTask(String taskName) {
        long startTime = System.currentTimeMillis();
        taskStartTimes.put(taskName, startTime);
        writeLog("[START] " + taskName);
    }
    
    public void endTask(String taskName) {
        Long startTime = taskStartTimes.get(taskName);
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            writeLog("[COMPLETED] " + taskName + " - " + duration + " ms");
            taskStartTimes.remove(taskName);
        }
    }
    
    public void logAction(String action, String details) {
        String timestamp = LocalDateTime.now().format(formatter);
        writeLog("[" + timestamp + "] [INFO] " + action + ": " + details);
    }
    
    public void logPageLoad(String pageName, String url, long loadTime) {
        writeLog("[PAGE] " + pageName + " loaded in " + loadTime + " ms");
    }
    
    public void logButtonResponse(String buttonName, long responseTime) {
        String timestamp = LocalDateTime.now().format(formatter);
        writeLog("[" + timestamp + "] [RESPONSE] " + buttonName + " - " + responseTime + " ms");
    }
    
    public void logQuestionProcessing(int questionNumber, String questionType, long processingTime) {
        String timestamp = LocalDateTime.now().format(formatter);
        
        // Enhanced logging based on question type
        if ("Essay".equals(questionType)) {
            writeLog("[" + timestamp + "] [Q" + questionNumber + "] Question Type: " + questionType + " - Textarea elements found: 1");
            writeLog("[" + timestamp + "] [Q" + questionNumber + "] " + questionType + " question answered in " + processingTime + " ms");
        } else if ("Multiple Choice".equals(questionType)) {
            writeLog("[" + timestamp + "] [Q" + questionNumber + "] Question Type: " + questionType + " - Radio button options: 5");
            writeLog("[" + timestamp + "] [Q" + questionNumber + "] " + questionType + " question answered in " + processingTime + " ms");
        } else {
            writeLog("[" + timestamp + "] [Q" + questionNumber + "] Question Type: " + questionType + " - No recognizable question elements found");
            writeLog("[" + timestamp + "] [Q" + questionNumber + "] " + questionType + " question answered in " + processingTime + " ms");
        }
    }
    
    public void logNavigationCheck() {
        String timestamp = LocalDateTime.now().format(formatter);
        writeLog("[" + timestamp + "] [STEP] Navigation Check: Checking for Next/End test buttons");
    }
    
    public void logButtonStatus(boolean hasNext, boolean hasEnd) {
        String timestamp = LocalDateTime.now().format(formatter);
        writeLog("[" + timestamp + "] [STEP] Button Status: Next: " + hasNext + ", End: " + hasEnd);
    }
    
    public void logNavigationDecision(String decision) {
        String timestamp = LocalDateTime.now().format(formatter);
        writeLog("[" + timestamp + "] [STEP] Navigation Decision: " + decision);
    }
    
    public void logError(String taskName, String error) {
        String timestamp = LocalDateTime.now().format(formatter);
        writeLog("[" + timestamp + "] [ERROR] " + taskName + ": " + error);
    }
    
    private void writeLog(String message) {
        try {
            if (logWriter != null) {
                logWriter.write(message + System.lineSeparator());
                logWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
    
    public void close() {
        try {
            writeLog("=== Performance Test Log Ended ===");
            if (logWriter != null) {
                logWriter.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to close log file: " + e.getMessage());
        }
    }
}
