package company.policy.client.manager.dll;

import company.policy.client.core.Question;

public class Node {

    private final Question element;

    public Node prev, next;

    public Node(Question el, Node prev, Node next) {
        this.element = el;
        this.prev = prev;
        this.next = next;
    }

    public Question getElement() {
        return element;
    }
}
