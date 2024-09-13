package com.osk2090.blog.list;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
public class ListTests {

    @Test
    void run() {
        // ArrayList 예제
        System.out.println("ArrayList 예제:");
        List<String> arrayList = new ArrayList<>();
        testList(arrayList);

        // LinkedList 예제
        System.out.println("\nLinkedList 예제:");
        List<String> linkedList = new LinkedList<>();
        testList(linkedList);

        // 성능 비교
        System.out.println("\n성능 비교:");
        comparePerformance();
    }

    private static void testList(List<String> list) {
        // 요소 추가
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");
        System.out.println("추가 후: " + list);

        // 요소 삽입
        list.add(1, "Durian");
        System.out.println("중간 삽입 후: " + list);

        // 요소 접근
        System.out.println("인덱스 2의 요소: " + list.get(2));

        // 요소 수정
        list.set(0, "Apricot");
        System.out.println("수정 후: " + list);

        // 요소 삭제
        list.remove(2);
        System.out.println("삭제 후: " + list);

        // 리스트 순회
        System.out.print("순회: ");
        for (String fruit : list) {
            System.out.print(fruit + " ");
        }
        System.out.println();
    }

    private static void comparePerformance() {
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        // 삽입 성능 비교
        long startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            arrayList.add(0, i);
        }
        long arrayListInsertTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            linkedList.add(0, i);
        }
        long linkedListInsertTime = System.nanoTime() - startTime;

        System.out.println("ArrayList 삽입 시간: " + arrayListInsertTime + " ns");
        System.out.println("LinkedList 삽입 시간: " + linkedListInsertTime + " ns");

        // 접근 성능 비교
        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            arrayList.get(i);
        }
        long arrayListAccessTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            linkedList.get(i);
        }
        long linkedListAccessTime = System.nanoTime() - startTime;

        System.out.println("ArrayList 접근 시간: " + arrayListAccessTime + " ns");
        System.out.println("LinkedList 접근 시간: " + linkedListAccessTime + " ns");

        /*
        성능 비교:
        ArrayList 삽입 시간: 469607875 ns
        LinkedList 삽입 시간: 3324791 ns

        ArrayList 접근 시간: 2290291 ns
        LinkedList 접근 시간: 3944856833 ns
         */

        // 결론 삽입하는건 둘다 결국엔 맨 마지막에 넣는거라 상관없지만
        // 특정 인덱스에 접근하는건 어레이리스트는 인덱스로 접근하기때문에 빠르고
        // 링크드리스트는 순차적으로 접근해야되기때문에 느리다.
    }
}
