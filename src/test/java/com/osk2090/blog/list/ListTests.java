package com.osk2090.blog.list;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@Slf4j
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

    @Test
    void arrayListTest() {

        // hint: 이건 인터페이스 타입이 아닌 구체 클래스를 타입으로 선언하여 좀더 static하여 유연한 코딩은 아니다.
        ArrayList list1 = new ArrayList<>(10);

        list1.add(5);
        list1.add(4);
        list1.add(2);
        list1.add(0);
        list1.add(1);
        list1.add(3);

        // list1 컬렉션에서 1번 인덱스부터 4번 인덱스까지 값 복사
        ArrayList list2 = new ArrayList<>(list1.subList(1, 4));
        log.info("일반 출력");
        log.info("list 1: {}", list1);
        log.info("list 2: {}", list2);

        // 오름차순 정렬
        Collections.sort(list1);
        Collections.sort(list2);
        log.info("정렬");
        log.info("list 1: {}", list1);
        log.info("list 2: {}", list2);

        // list1 기준으로 list2에 있는 값을 모두 가지고 있으면 true 리턴
        log.info("list1.containsAll(list2): {}", list1.containsAll(list2));

        list2.add("B");
        list2.add("C");

        log.info("3번 인덱스에 A 저장");
        list2.add(3, "A");
        log.info("list 1: {}", list1);
        log.info("list 2: {}", list2);

        log.info("3번 인덱스에 AA 저장");
        list2.add(3, "AA");
        log.info("list 1: {}", list1);
        log.info("list 2: {}", list2);

        // list1에서 list2와 겹치는 부분만 남기고 나머지는 삭제한다.
        log.info("list1.retainAll(list2): {}", list1.retainAll(list2));
        log.info("list 1: {}", list1);
        log.info("list 2: {}", list2);

        // list1를 기준으로 list2에 있는 값 삭제
        // 이렇게 뒤에서부터 확인해서 삭제한 이유는 앞에서부터 삭제하면 자리이동이 있기때문에 불필요한 자리이동 하지않음
        for (int i = list2.size() - 1; i >= 0; i--) {
            if (list1.contains(list2.get(i))) {
                list2.remove(i);
            }
        }
        log.info("list 1: {}", list1);
        log.info("list 2: {}", list2);
    }
}
