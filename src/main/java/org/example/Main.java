package org.example;

import java.util.*;
import java.util.concurrent.*;


public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        int len = 1000;
        int max = 10;
        List<Future<Integer>> future = new ArrayList<>();
        for (int o = 0; o < len; o++) {

            Runnable call = () -> {
                int count = 0;
                String str = generateRoute("RLRFR", 100);
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) == "R".charAt(0)) {
                        count++;
                    }
                }
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(count)) {
                        sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                    } else {
                        sizeToFreq.put(count, 1);
                    }
                }
                //System.out.println(str + " " + count);
            };

            future.add(
                    (Future<Integer>) threadPool.submit(call)
            );

        }

        for (int i = 0; i < len; i++) {
            future.get(i).get();
        }
        threadPool.shutdown();
        ArrayList<Map.Entry<Integer, Integer>> list = new ArrayList<>(sizeToFreq.entrySet());
        list.sort(Map.Entry.comparingByValue());
        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", list.get(list.size() - 1).getKey(), list.get(list.size() - 1).getValue());
        System.out.println("Другие размеры:");
        if (max > list.size()) {
            max = list.size();
        }
        for (int i = 0; i < max; i++) {
            System.out.printf("- %d (%d раз)\n", list.get(list.size() - 1 - i).getKey(), list.get(list.size() - 1 - i).getValue());
        }
        if (max < list.size()) {
            System.out.printf("...");
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}