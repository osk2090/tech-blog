package com.osk2090.blog.map;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
public class MapTests {
    private static final Map<String, String> mapByMimeType = new TreeMap<>();

    static {
        mapByMimeType.put("application/mp4", "MP4");
        mapByMimeType.put("video/mp4", "MP4");
        mapByMimeType.put("video/x-m4v", "MP4");
        mapByMimeType.put("audio/mp4", "MP4");
        mapByMimeType.put("audio/x-m4a", "MP4");
        mapByMimeType.put("audio/x-mp4a", "MP4");
    }

    /*void init() {
        log.error("여기는 init 시작 구간");
        Runnable writerTask = () -> {
            for (int i = 0; i < 10; i++) {
                map.put(i, "Value " + i);
                System.out.println(Thread.currentThread().getName() + " put: " + i);
            }
        };
        log.error("여기는 init 종료 구간");
    }*/

    @Test
    void test_1() {
        /*this.init();

        // 여러 스레드에서 TreeMap에 접근
        for (int j = 0; j < 5; j++) {
            log.error("여기는 {} 번 시작", j);
            Runnable writerTask = () -> {
                for (int i = 0; i < 10; i++) {
                    map.put(i, "Value " + i);
                    System.out.println(Thread.currentThread().getName() + " put: " + i);
                }
            };

            Runnable readerTask = () -> {
                for (int i = 0; i < 10; i++) {
                    String value = map.get(i);
                *//*if (value == null) {
                    log.error("{} get: {}", Thread.currentThread().getName(), value);
                }*//*
//                System.out.println(Thread.currentThread().getName() + " get: " + value);

                    StringBuilder sb = new StringBuilder();
                    sb.append(Thread.currentThread().getName()).append("-this value: ").append(value);
                    System.out.println(sb);
                }
            };

            Thread writerThread1 = new Thread(writerTask);
            Thread writerThread2 = new Thread(writerTask);

            Thread readerThread1 = new Thread(readerTask);
            Thread readerThread2 = new Thread(readerTask);

            writerThread1.start();
            writerThread2.start();

            readerThread1.start();
            readerThread2.start();

            try {
                writerThread1.join();
                writerThread2.join();

                readerThread1.join();
                readerThread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.error("여기는 {} 번 종료", j);
        }*/
    }

    /*@Test
    void test_2() {
        // 여기서 없는 값을 먼저 넣는다.
        Runnable put = () -> {
            mapByMimeType.put("image/jpeg", "JPEG");
        };

        // 그리고 여기서는 위에 없던 값을 가져온다.
        Runnable get = () -> {
            String s = mapByMimeType.get("image/jpeg");
            log.error("type: {}", s);
        };

        Thread put1 = new Thread(put);
        Thread put2 = new Thread(put);

        Thread get1 = new Thread(get);
        Thread get2 = new Thread(get);

        put1.start();
        put2.start();

        get1.start();
        get2.start();

        try {
            put1.join();
            put2.join();

            get1.join();
            get2.join();
        } catch (Exception e) {
            log.error("ex :{}", e.getMessage());
        }
    }*/

    @Test
    void test_3() {
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        // Runnable to put a value
        Runnable put = () -> {
            for (int i = 0; i < 100; i++) {
                log.error("put: image/jpeg: {}, JPEG: {}", i, i);
                mapByMimeType.put("image/jpeg" + i, "JPEG" + i);
            }
        };

        // Runnable to get a value
        Runnable get = () -> {
            for (int i = 0; i < 100; i++) {
                String value = mapByMimeType.get("image/jpeg" + i);
                log.error("get: image/jpeg: {}, {}", i, value);
            }
        };

        // Submitting 10 put tasks
        for (int i = 0; i < 10; i++) {
            executorService.submit(put);
        }

        // Submitting 10 get tasks
        for (int i = 0; i < 10; i++) {
            executorService.submit(get);
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            log.error("Exception: {}", e.getMessage());
        }

        System.out.println("Finished without exception");


    }

    @Test
    void vsMapTest() {
        // TreeSet 예제
        Set<String> treeSet = new TreeSet<>();
        treeSet.add("Banana");
        treeSet.add("Apple");
        treeSet.add("Cherry");
        treeSet.add("Date");
        treeSet.add("Apple"); // 중복 원소

        System.out.println("TreeSet:");
        for (String fruit : treeSet) {
            System.out.println(fruit);
        }

        // HashSet 예제
        Set<String> hashSet = new HashSet<>();
        hashSet.add("Banana");
        hashSet.add("Apple");
        hashSet.add("Cherry");
        hashSet.add("Date");
        hashSet.add("Apple"); // 중복 원소

        System.out.println("\nHashSet:");
        for (String fruit : hashSet) {
            System.out.println(fruit);
        }

        // 성능 비교
        int numElements = 100000;
        long startTime, endTime;

        // TreeSet 삽입 시간
        Set<Integer> largeTreeSet = new TreeSet<>();
        startTime = System.nanoTime();
        for (int i = 0; i < numElements; i++) {
            largeTreeSet.add(i);
        }
        endTime = System.nanoTime();
        System.out.println("\nTreeSet 삽입 시간: " + (endTime - startTime) / 1000000.0 + " ms");

        // HashSet 삽입 시간
        Set<Integer> largeHashSet = new HashSet<>();
        startTime = System.nanoTime();
        for (int i = 0; i < numElements; i++) {
            largeHashSet.add(i);
        }
        endTime = System.nanoTime();
        System.out.println("HashSet 삽입 시간: " + (endTime - startTime) / 1000000.0 + " ms");

        // TreeSet 검색 시간
        startTime = System.nanoTime();
        largeTreeSet.contains(numElements / 2);
        endTime = System.nanoTime();
        System.out.println("TreeSet 검색 시간: " + (endTime - startTime) / 1000.0 + " μs");

        // HashSet 검색 시간
        startTime = System.nanoTime();
        largeHashSet.contains(numElements / 2);
        endTime = System.nanoTime();
        System.out.println("HashSet 검색 시간: " + (endTime - startTime) / 1000.0 + " μs");
    }
}
