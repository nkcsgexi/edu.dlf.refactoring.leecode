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
		BinaryTreeNode parent;
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
	
	/* 
	 * Implement a function to check if a tree is balanced. For the purposes of 
	 * this question, a balanced tree is defined to be a tree such that no two 
	 * leaf nodes differ in distance from the root by more than one.
	 * */
	private static boolean isBalanced(BinaryTreeNode root) {
		int[] depths = getShortAndLongDistance(root);
		return depths[1] - depths[0] <= 1;
	}
	
	private static int[] getShortAndLongDistance(BinaryTreeNode root) {
		if(root == null) 
			return new int[]{-1 , -1};
		int[] left = getShortAndLongDistance(root.left);
		int[] right = getShortAndLongDistance(root.right);
		int max = Math.max(left[1], right[1]);
		int min = Math.min(left[0], right[0]);
		return new int[]{min + 1, max + 1};
	}
	
	/* 
	 * Given a directed graph, design an algorithm to find out whether there 
	 * is a route between two nodes.
	 * */
	private static boolean isConnected(GraphNode node1, GraphNode node2) {
		Stack<GraphNode> stack = new Stack<GraphNode>();
		stack.push(node1);
		while(!stack.isEmpty()) {
			GraphNode current = stack.pop();
			if(current == node2)
				return true;
			current.neighbors.forEach(n -> stack.push(n));
		}
		return false;
	}
	
	/* 
	 * Given a sorted (increasing order) array, write an algorithm to create a 
	 * binary tree with minimal height.
	 * */
	private static BinaryTreeNode createTreeWithMinimumHeight(int[] numbers, 
			int start, int end) {
		int middle = (start + end) / 2;
		BinaryTreeNode node = new BinaryTreeNode(numbers[middle]);
		BinaryTreeNode left = null, right = null;
		if(middle > start)
			left = createTreeWithMinimumHeight(numbers, start, middle - 1);
		if(middle < end)
			right = createTreeWithMinimumHeight(numbers, middle + 1, end);
		node.left = left;
		node.right = right;
		return node;
	}
	
	/* 
	 * Given a binary search tree, design an algorithm which creates a linked 
	 * list of all the nodes at each depth (eg, if you have a tree with depth D, 
	 * you’ll have D linked lists).
	 * */
	private static LinkedList<LinkedList<BinaryTreeNode>>
			createLinkedListsFromTree(BinaryTreeNode root) {
		if(root == null) return null;
		LinkedList<LinkedList<BinaryTreeNode>> result = new 
				LinkedList<LinkedList<BinaryTreeNode>>();
		LinkedList<BinaryTreeNode> current = new LinkedList<BinaryTreeNode>();
		current.add(root);
		LinkedList<LinkedList<BinaryTreeNode>> left = null, right = null;
		if(root.left != null) left  = createLinkedListsFromTree(root.left); 
		if(root.right != null) right = createLinkedListsFromTree(root.right);
		result.add(current);
		if(left == null && right == null){
			return result;
		}
		if(left != null && right != null) {
			for(int i = 0; i < Math.max(left.size(), right.size()); i++) {
				if(i < left.size() && i < right.size()) {
					LinkedList<BinaryTreeNode> list = new 
						LinkedList<BinaryTreeNode>();
					list.addAll(left.get(i));
					list.addAll(right.get(i));
					result.add(list);
					continue;
				}
				result.add((i < left.size() ? left : right).get(i));
			}
			return result;
		}
		result.addAll(left != null ? left : right);
		return result;
	}
	/* 
	 * Write an algorithm to find the ‘next’ node (e.g., in-order successor) of 
	 * a given node in a binary search tree where each node has a link to its 
	 * parent.
	 * */
	private static BinaryTreeNode findNextInorder(BinaryTreeNode node) {
		if(node.right != null) {
			return getLeftMostChild(node);
		}
		while(node.parent != null && node.parent.right == node) node = node.parent;
		if(node.parent == null) return null;
		return node.parent;
	}

	private static BinaryTreeNode getLeftMostChild(BinaryTreeNode node) {
		BinaryTreeNode current;
		for(current = node.right; current.left != null; current = current.left);
		return current;
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
