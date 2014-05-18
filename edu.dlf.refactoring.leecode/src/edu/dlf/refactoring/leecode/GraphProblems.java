package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

public class GraphProblems {
	
	private class Node {
		Node(int value) {
			this.value = value;
		}
		int value;
		List<Node> neighbors = new ArrayList<Node>();
	}
	
	private void depthFirstVisit(Node head, Consumer<Node> visit) {
		Stack<Node> visitedNode = new Stack<Node>();
		visitedNode.add(head);
		while(!visitedNode.empty()) {
			Node current = visitedNode.pop();
			visit.accept(current);
			visitedNode.addAll(current.neighbors);
		}
	}
	
	private void widthFirstVisit(Node head, Consumer<Node> visit) {
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(head);
		while(!queue.isEmpty()) {
			Node current = queue.remove();
			visit.accept(current);
			queue.addAll(current.neighbors);
		}
	}
	
	private Node cloneGraph(Node head) {
		if(head.neighbors.isEmpty()) {
			return new Node(head.value);
		}
		Node newHead = new Node(head.value);
		head.neighbors.stream().map(n -> cloneGraph(n)).forEach(sub -> newHead.
			neighbors.add(sub));
		return newHead;
	}
}
