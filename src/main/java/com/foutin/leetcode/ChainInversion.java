package com.foutin.leetcode;

import java.util.Stack;

/**
 * @author f2485
 * @Description
 * @date 2025/3/3 10:48
 */
public class ChainInversion {



     static class Node {
         private int value;
         private Node nextNode;

         public Node(int value) {
             this.value = value;
             this.nextNode = null;
         }

         public int getValue() {
             return value;
         }

         public Node getNextNode() {
             return nextNode;
         }

         public void setNextNode(Node node) {
             this.nextNode = node;
         }
     }


    /**
     * 递归翻转链表
     * @param node
     * @return
     */
    public Node reverserLinkedList(Node node){
        //这个递归，返回值只是为了控制返回的是最后一个节点
        //然后通过递归通过栈的特性，这里就是让它可以从最后一个节点开始把自己的子节点的子节点改成自己
        //自己的子节点改为null
        if (node.getNextNode() == null || node == null){
            return node;
        }
        Node newdata = reverserLinkedList(node.getNextNode());
        node.getNextNode().setNextNode(node);
        node.setNextNode(null);
        return newdata;
    }

    public Node reverserLinkedList2(Node node){
        Stack<Node> nodeStack = new Stack<>();
        Node head = null;
        //存入栈中，模拟递归开始的栈状态
        while (node != null){
            nodeStack.push(node);
            node = node.getNextNode();
        }
        //特殊处理第一个栈顶元素（也就是反转前的最后一个元素，因为它位于最后，不需要反转，如果它参与下面的while， 因为它的下一个节点为空，如果getNode()， 那么为空指针异常）
        if ((!nodeStack.isEmpty())){
            head = nodeStack.pop();
        }
        //排除以后就可以快乐的循环
        while (!nodeStack.isEmpty()){
            Node tempNode = nodeStack.pop();
            tempNode.getNextNode().setNextNode(tempNode);
            tempNode.setNextNode(null);
        }
        return head;
    }



}
