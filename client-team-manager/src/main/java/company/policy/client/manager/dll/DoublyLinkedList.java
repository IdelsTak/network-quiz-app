package company.policy.client.manager.dll;

import company.policy.client.core.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DoublyLinkedList {

    private Node root;

    public DoublyLinkedList() {
        root = null;
    }

    public int size() {
        int count = 0;
        Node curr = root;

        while (curr != null) {
            count++;
            curr = curr.next;
        }

        return count;
    }

    public void prepend(Question data) {
        if (root == null) {
            root = new Node(data, null, null);
        } else {
            Node newNode = new Node(data, null, root);
            root.prev = newNode;
            root = newNode;
        }
    }

    public void indexInsertion(Question data, int index) {
        if (index == 0) {
            this.prepend(data);
        } else if (index == size()) {
            this.append(data);
        } else if (index > 0 && index < size()) {
            int count = 0;
            Node curr = root;
            for (; count != index; count++, curr = curr.next);
            Node newNode = new Node(data, curr.prev, null);
            curr.prev = newNode;
            newNode.next = curr;
        }
    }

    public void append(Question data) {
        if (root == null) {
            root = new Node(data, null, null);
        } else {
            Node curr = root;

            while (curr.next != null) {
                curr = curr.next;
            }

            curr.next = new Node(data, curr, null);
        }
    }

    public Question[] getAll() {
        Node curr = root;
        List<Question> vals = new ArrayList<>();

        while (curr != null) {
            vals.add(curr.getElement());
            curr = curr.next;
        }

        return vals.toArray(Question[]::new);
    }

    @Override
    public String toString() {
        Node curr = root;
        List<String> vals = new ArrayList<>();

        while (curr != null) {
            vals.add(policyInfo(curr));
            curr = curr.next;
        }

        return vals.stream().collect(Collectors.joining(" <--> ", "HEAD <-> ", " <-> TAIL"));

    }

    private static String policyInfo(Node curr) {
        Question pqm = curr.getElement();

        return String.format("Qn %d - %s (%s) - %d from %d responses correct",
                pqm.getNumber(),
                pqm.getTopic(),
                pqm.getSubTopic(),
                pqm.getAttempt().getCorrect(),
                pqm.getAttempt().getAttempted());
    }

    public Question deleteEndNode() {
        Node curr = root;
        for (; curr.next != null; curr = curr.next);
        if (curr != null) {
            if (curr == root) {
                return this.deleteBeginNode();
            }

            Question el = curr.getElement();
            curr.prev.next = null;
            curr = null;
            return el;
        }
        return null;
    }

    public Question deleteBeginNode() {
        if (root != null) {
            Question el = root.getElement();

            if (root.next != null) {
                root = root.next;
                root.prev = null;
            } else {
                root = null;
            }
            return el;
        }
        return null;
    }

    public Question deleteIndexNode(int index) {
        if (index == 0) {
            return this.deleteBeginNode();
        } else if (index == size()) {
            return this.deleteEndNode();
        } else if (index > 0 && index < size()) {
            int count = 0;
            Node curr = root;
            for (; count != index; count++, curr = curr.next);
            Question el = curr.getElement();
            curr.next.prev = curr.prev;
            curr.prev.next = curr.next;
            curr = null;
            return el;
        }
        return null;
    }

    public Boolean isEmpty() {
        return (root == null);
    }
}
