package company.policy.client.manager.bt;

import company.policy.client.core.Question;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class BinaryTree {

    public Node root;

    public void add(Question value) {
        root = addRecursive(root, value);
    }

    private Node addRecursive(Node current, Question value) {

        if (current == null) {
            return new Node(value);
        }

        if (value.getNumber()< current.value.getNumber()) {
            current.left = addRecursive(current.left, value);
        } else if (value.getNumber() > current.value.getNumber()) {
            current.right = addRecursive(current.right, value);
        }

        return current;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int getSize() {
        return getSizeRecursive(root);
    }

    private int getSizeRecursive(Node current) {
        return current == null ? 0 : getSizeRecursive(current.left) + 1 + getSizeRecursive(current.right);
    }

    public boolean containsNode(Question value) {
        return containsNodeRecursive(root, value);
    }

    private boolean containsNodeRecursive(Node current, Question value) {
        if (current == null) {
            return false;
        }

        if (value.getNumber() == current.value.getNumber()) {
            return true;
        }

        return value.getNumber() < current.value.getNumber()
                ? containsNodeRecursive(current.left, value)
                : containsNodeRecursive(current.right, value);
    }

    public void delete(Question value) {
        root = deleteRecursive(root, value);
    }

    private Node deleteRecursive(Node current, Question value) {
        if (current == null) {
            return null;
        }

        if (value.getNumber() == current.value.getNumber()) {
            // Case 1: no children
            if (current.left == null && current.right == null) {
                return null;
            }

            // Case 2: only 1 child
            if (current.right == null) {
                return current.left;
            }

            if (current.left == null) {
                return current.right;
            }

            // Case 3: 2 children
            Question smallestValue = findSmallestValue(current.right);
//            current.value = new Question(smallestValue.getNumber());
            current.value = Question.from(smallestValue);
            current.right = deleteRecursive(current.right, smallestValue);
            return current;
        }
        if (value.getNumber() < current.value.getNumber()) {
            current.left = deleteRecursive(current.left, value);
            return current;
        }

        current.right = deleteRecursive(current.right, value);
        return current;
    }

    private Question findSmallestValue(Node root) {
        return root.left == null ? root.value : findSmallestValue(root.left);
    }

    public void traverseInOrder(Node node) {
        if (node != null) {
            traverseInOrder(node.left);
            visit(node.value);
            traverseInOrder(node.right);
        }
    }

    public void traversePreOrder(Node node) {
        if (node != null) {
            visit(node.value);
            traversePreOrder(node.left);
            traversePreOrder(node.right);
        }
    }

    public void traversePostOrder(Node node) {
        if (node != null) {
            traversePostOrder(node.left);
            traversePostOrder(node.right);
            visit(node.value);
        }
    }

    public void traverseLevelOrder() {
        if (root == null) {
            return;
        }

        Queue<Node> nodes = new LinkedList<>();
        nodes.add(root);

        while (!nodes.isEmpty()) {

            Node node = nodes.remove();

            System.out.print(" " + node.value);

            if (node.left != null) {
                nodes.add(node.left);
            }

            if (node.right != null) {
                nodes.add(node.right);
            }
        }
    }
    
    private final List<Question> traversedModels = new ArrayList<>();

    public List<Question> traverseInOrderWithoutRecursion() {
        Stack<Node> stack = new Stack<>();
        Node current = root;
        
        traversedModels.clear();

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            Node top = stack.pop();
            traversedModels.add(top.value);
            visit(top.value);
            current = top.right;
        }
        
        return new ArrayList(traversedModels);
    }

    public List traversePreOrderWithoutRecursion() {
        Stack<Node> stack = new Stack<>();
        Node current = root;
        stack.push(root);
        
        traversedModels.clear();

        while (current != null && !stack.isEmpty()) {
            current = stack.pop();
            traversedModels.add(current.value);
            visit(current.value);

            if (current.right != null) {
                stack.push(current.right);
            }

            if (current.left != null) {
                stack.push(current.left);
            }
        }
        
        return new ArrayList(traversedModels);
    }

    public List traversePostOrderWithoutRecursion() {
        Stack<Node> stack = new Stack<>();
        Node prev = root;
        Node current = root;
        stack.push(root);
        
        traversedModels.clear();

        while (current != null && !stack.isEmpty()) {
            current = stack.peek();
            boolean hasChild = (current.left != null || current.right != null);
            boolean isPrevLastChild = (prev == current.right || (prev == current.left && current.right == null));

            if (!hasChild || isPrevLastChild) {
                current = stack.pop();
                traversedModels.add(current.value);
                visit(current.value);
                prev = current;
            } else {
                if (current.right != null) {
                    stack.push(current.right);
                }
                if (current.left != null) {
                    stack.push(current.left);
                }
            }
        }
        
        return new ArrayList(traversedModels);
    }

    private void visit(Question value) {
        // System.out.print(" " + String.format("%d - %s (%s)", value.getNumber(), value.getTopic(), value.getSubTopic()));
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
