package util;

import java.util.*;

public class Queue {
    private List<Object> queue = new LinkedList<>();

    public void enqueue(Object object) {
        queue.add(object);
    }

    public Object dequeue() {
        if (!isEmpty()) {
            return queue.remove(0);
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public boolean contains(Object object) {
        return queue.contains(object);
    }

    public int size() {
        return queue.size();
    }
}
