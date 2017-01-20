package util;

import java.util.*;

public class LinkQueue {
    // 已访问的url集合
    private Set<Object> visitedUrl = new HashSet<>();
    // 待访问的url
    private Queue unVisitedUrl = new Queue();

    public Queue getUnVisitedUrl() {
        return unVisitedUrl;
    }

    public void addVisitedUrl(String url) {
        visitedUrl.add(url);
    }

    public void removeVisitedUrl(String url) {
        visitedUrl.remove(url);
    }

    // 从待访问的url队列中取出队首url进行访问
    public Object unVisitedUrlDequeue() {
        return unVisitedUrl.dequeue();
    }

    public void addUnvisitedUrl(String url) {
        if ((url != null) && (!url.trim().equals(""))
                && (!visitedUrl.contains(url))
                && (!unVisitedUrl.contains(url))) {
            unVisitedUrl.enqueue(url);
        }
    }

    public int getVisitedUrlNum() {
        return visitedUrl.size();
    }

    public boolean unVisitedUrlIsEmpty() {
        return unVisitedUrl.isEmpty();
    }
}
