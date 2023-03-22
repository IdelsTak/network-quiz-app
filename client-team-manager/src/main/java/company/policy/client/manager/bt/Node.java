package company.policy.client.manager.bt;

import company.policy.client.core.Question;

public class Node {

    Question value;
    Node left;
    Node right;

    Node(Question value) {
        this.value = value;
        right = null;
        left = null;
    }
}
