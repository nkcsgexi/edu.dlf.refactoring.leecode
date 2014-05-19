package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

public class GraphProblems {
	
	private class GraphNode {
		GraphNode(int value) {
			this.value = value;
		}
		int value;
		List<GraphNode> neighbors = new ArrayList<GraphNode>();
	}
	
	private class BinaryTreeNode {
		BinaryTreeNode left;
		BinaryTreeNode right;
	}
	
	private void depthFirstVisit(GraphNode head, Consumer<GraphNode> visit) {
		Stack<GraphNode> visitedNode = new Stack<GraphNode>();
		visitedNode.add(head);
		while(!visitedNode.empty()) {
			GraphNode current = visitedNode.pop();
			visit.accept(current);
			visitedNode.addAll(current.neighbors);
		}
	}
	
	private void widthFirstVisit(GraphNode head, Consumer<GraphNode> visit) {
		Queue<GraphNode> queue = new LinkedList<GraphNode>();
		queue.add(head);
		while(!queue.isEmpty()) {
			GraphNode current = queue.remove();
			visit.accept(current);
			queue.addAll(current.neighbors);
		}
	}
	
	private GraphNode cloneGraph(GraphNode head) {
		if(head.neighbors.isEmpty()) {
			return new GraphNode(head.value);
		}
		GraphNode newHead = new GraphNode(head.value);
		head.neighbors.stream().map(n -> cloneGraph(n)).forEach(sub -> newHead.
			neighbors.add(sub));
		return newHead;
	}
	
	private BinaryTreeNode lowestCommonAncestor(BinaryTreeNode root, 
		BinaryTreeNode node1, BinaryTreeNode node2) {
		if(root == null) return null;
		if(root == node1 || root == node2) return root;
		BinaryTreeNode left = lowestCommonAncestor(root.left, node1, node2);
		BinaryTreeNode right = lowestCommonAncestor(root.right, node1, node2);
		if(left != null && right != null) return root;
		return left == null ? right : left;
	}
	
	
}
