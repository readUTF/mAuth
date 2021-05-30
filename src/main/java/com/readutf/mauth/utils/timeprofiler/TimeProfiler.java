package com.readutf.mauth.utils.timeprofiler;

import java.util.HashMap;
import java.util.Set;

public class TimeProfiler {

    long start, end;
    HashMap<String, Long> checkPoints;

    public TimeProfiler() {
        this.start = System.currentTimeMillis();
        checkPoints = new HashMap<>();
    }

    public void addCheckPoint(String name) {
        checkPoints.put(name, System.currentTimeMillis());
    }

    public void printProfile() {
        end = System.currentTimeMillis();

        System.out.println("Took: " + (end - start) + "ms");

        if(!checkPoints.isEmpty()) {
            System.out.println("Sections:");
            String first = checkPoints.keySet().toArray(new String[checkPoints.size()])[0];
            System.out.println(first + ": " + (checkPoints.get(first) - start) + "ms");

            if(checkPoints.size() > 1) {
                for(int x = 0; x < checkPoints.size() -1; x++) {
                    String current = checkPoints.keySet().toArray(new String[checkPoints.size()])[x];
                    String next = checkPoints.keySet().toArray(new String[checkPoints.size()])[x + 1];

                    System.out.println(current  + " -> " + next + ": " + (checkPoints.get(next) - checkPoints.get(current)) + "ms");
                }
            }
        }



    }

}
