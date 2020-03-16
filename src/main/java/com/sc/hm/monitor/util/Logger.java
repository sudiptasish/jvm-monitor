package com.sc.hm.monitor.util;

import java.io.PrintStream;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * 
 * @author Sudiptasish Chanda
 */
public final class Logger {

    private static final boolean verbose = Boolean.getBoolean("monitoring.logging.enabled");
    private static final PrintStream out = System.out;

    /**
     * @return the verbose
     */
    public static boolean isVerbose() {
        return verbose;
    }
    
    /**
     * Log the message to the console.
     * @param message
     */
    public static void log(String message) {
        if (isVerbose()) {
            out.println(message);
        }
    }
    
    /**
     * Log the message to the console.
     * @param message
     */
    public static void log(Object message) {
        if (isVerbose()) {
            out.println(message);
        }
    }
    
    /**
     * Log the message to the console.
     * @param message
     */
    public static void log(int message) {
        if (isVerbose()) {
            out.println(message);
        }
    }
    
    /**
     * Print the stacktrace along with the error message.
     * @param th
     */
    public static void log(Throwable th) {
        
    }
    
    private void printStackTrace(Throwable th) {
        // Guard against malicious overrides of Throwable.equals by
        // using a Set with identity equality semantics.
        Set<Throwable> dejaVu =
            Collections.newSetFromMap(new IdentityHashMap<Throwable, Boolean>());
        dejaVu.add(th);
        
        Class<?> clazz = null;
        StringBuilder _buff = new StringBuilder(100);

        // Print the stack trace
        _buff.append(th);
        
        StackTraceElement[] traces = th.getStackTrace();
        for (StackTraceElement ste : traces) {
            addStackTrace(_buff, ste);
        }

        // Print suppressed exceptions, if any
        for (Throwable suppressed : th.getSuppressed()) {
            printEnclosedStackTrace(suppressed, _buff, traces, "Suppressed", "\t", dejaVu);
        }
        // Print cause, if any
        Throwable causedBy = th.getCause();
        if (causedBy != null) {
            printEnclosedStackTrace(causedBy, _buff, traces, "Caused By", "", dejaVu);
        }
        System.err.println(_buff.toString());
    }
    
    private void printEnclosedStackTrace(Throwable th,
            StringBuilder _buff,
            StackTraceElement[] enclosingTrace,
            String caption,
            String prefix,
            Set<Throwable> dejaVu) {        
                
        if (dejaVu.contains(th)) {
            _buff.append("\t[CIRCULAR REFERENCE:" + th + "]");
        }
        else {
            dejaVu.add(th);
            // Compute number of frames in common between this and enclosing trace
            StackTraceElement[] localTraces = th.getStackTrace();
            int m = localTraces.length - 1;
            int n = enclosingTrace.length - 1;
            while (m >= 0 && n >=0 && localTraces[m].equals(enclosingTrace[n])) {
                m--; n--;
            }
            int framesInCommon = localTraces.length - 1 - m;
            
            // Print our stack trace
            _buff.append(prefix + caption + th);
            
            for (int i = 0; i <= m; i++) {
                addStackTrace(_buff, localTraces[i]);
            }
            if (framesInCommon != 0) {
                _buff.append(prefix + "\t... " + framesInCommon + " more");
            }
            // Print suppressed exceptions, if any
            for (Throwable se : th.getSuppressed()) {
                printEnclosedStackTrace(se, _buff, localTraces, "Suppressed", prefix + "\t", dejaVu);
            }            
            // Print cause, if any
            Throwable ourCause = th.getCause();
            if (ourCause != null) {
                printEnclosedStackTrace(ourCause, _buff, localTraces, "Caused By", prefix, dejaVu);
            }
        }
    }
    
    private static void addStackTrace(StringBuilder _buff, StackTraceElement ste) {
        try {
            Class<?> clazz = Class.forName(ste.getClassName());
            String library = clazz.getProtectionDomain().getCodeSource().getLocation().toString();
            _buff.append("\n\tat ").append(ste).append(" [").append(library).append("]");
        }
        catch (ClassNotFoundException e) {}
    }
}
