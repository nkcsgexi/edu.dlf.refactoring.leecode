package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

public class GraphProblems {
	
	private static class GraphNode {
		GraphNode(int value) {
			this.value = value;
		}
		int value;
		List<GraphNode> neighbors = new ArrayList<GraphNode>();
	}
	
	private static class BinaryTreeNode {
		int value;
		BinaryTreeNode left;
		BinaryTreeNode right;
		BinaryTreeNode(int value) {
			this.value = value;
		}
	}
	
	private static void previstTree(BinaryTreeNode root, Consumer<BinaryTreeNode> visit) {
		visit.accept(root);
		if(root.left != null) previstTree(root.left, visit);
		if(root.right != null) previstTree(root.right, visit);
	}
	
	private static void postvisitTree(BinaryTreeNode root, Consumer<BinaryTreeNode> visit) {
		if(root.left != null) postvisitTree(root.left, visit);
		if(root.right != null) postvisitTree(root.right, visit);
		visit.accept(root);	
	}
	
	private static void invisitTree(BinaryTreeNode root, Consumer<BinaryTreeNode> visit) {
		if(root.left != null) invisitTree(root.left, visit);
		visit.accept(root);
		if(root.right != null) invisitTree(root.right, visit);
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
	
	private static BinaryTreeNode createTreeByPreorderAndInorderTraversal(
			List<Integer> inorder, List<Integer> preorder) {	
		if(inorder.size() == 0) {
			return null;
		}
		if(inorder.size() == 1) {
			if(inorder.get(0) == preorder.get(0)) 
				return new BinaryTreeNode(inorder.get(0));
			else 
				return null;
		}
		BinaryTreeNode root = new BinaryTreeNode(preorder.get(0));
		for(int index = 0; index < inorder.size(); index ++) {
			if(inorder.get(index) != preorder.get(0)) continue;
			List<Integer> beforeIn = getSublist(inorder, 0, index);
			List<Integer> afterIn = getSublist(inorder, index + 1, inorder.size());
			List<Integer> beforePre = getSublist(preorder, 1, index + 1);
			List<Integer> afterPre = getSublist(preorder, index + 1, preorder.size());
			BinaryTreeNode left, right;
			left = createTreeByPreorderAndInorderTraversal(beforeIn, beforePre);
			right = createTreeByPreorderAndInorderTraversal(afterIn, afterPre);
			if(left != null || beforeIn.isEmpty()) {
				if(right != null || afterIn.isEmpty()) {
					root.left = left;
					root.right = right;
					return root;
				}
			}	
		}
		return null;
	}
	
	private static List<Integer> getSublist(List<Integer> list, int start, int end) {
		return list.subList(start, end);
	}
	
	private static List<Integer> toList(int[] array) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int a : array) {
			list.add(a);
		}
		return list;
	}
	
	public static void main(String[] args) {
		List<Integer> preorder = toList(new int[]{7,10,4,3,1,2,8,11});
		List<Integer> inorder = toList(new int[]{4,10,3,1,7,11,8,2});
		BinaryTreeNode root = createTreeByPreorderAndInorderTraversal(inorder, 
			preorder);
		previstTree(root, r -> System.out.println(r.value));
		invisitTree(root, r -> System.out.println(r.value));
	}
	
	
}
