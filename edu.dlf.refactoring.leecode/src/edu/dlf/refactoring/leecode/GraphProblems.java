package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

public class GraphProblems {

	private static class GraphNode implements Comparable<GraphNode> {
		GraphNode(int value) {
			this.value = value;
		}

		int value;
		int distance;
		List<GraphNode> neighbors = new ArrayList<GraphNode>();

		@Override
		public int compareTo(GraphNode o) {
			return this.distance - o.distance;
		}
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

	private static void previstTree(BinaryTreeNode root,
			Consumer<BinaryTreeNode> visit) {
		visit.accept(root);
		if (root.left != null)
			previstTree(root.left, visit);
		if (root.right != null)
			previstTree(root.right, visit);
	}

	private static void postvisitTree(BinaryTreeNode root,
			Consumer<BinaryTreeNode> visit) {
		if (root.left != null)
			postvisitTree(root.left, visit);
		if (root.right != null)
			postvisitTree(root.right, visit);
		visit.accept(root);
	}

	private static void invisitTree(BinaryTreeNode root,
			Consumer<BinaryTreeNode> visit) {
		if (root.left != null)
			invisitTree(root.left, visit);
		visit.accept(root);
		if (root.right != null)
			invisitTree(root.right, visit);
	}

	private void depthFirstVisit(GraphNode head, Consumer<GraphNode> visit) {
		Stack<GraphNode> visitedNode = new Stack<GraphNode>();
		visitedNode.add(head);
		while (!visitedNode.empty()) {
			GraphNode current = visitedNode.pop();
			visit.accept(current);
			visitedNode.addAll(current.neighbors);
		}
	}

	private void widthFirstVisit(GraphNode head, Consumer<GraphNode> visit) {
		Queue<GraphNode> queue = new LinkedList<GraphNode>();
		queue.add(head);
		while (!queue.isEmpty()) {
			GraphNode current = queue.remove();
			visit.accept(current);
			queue.addAll(current.neighbors);
		}
	}

	private GraphNode cloneGraph(GraphNode head) {
		if (head.neighbors.isEmpty()) {
			return new GraphNode(head.value);
		}
		GraphNode newHead = new GraphNode(head.value);
		head.neighbors.stream().map(n -> cloneGraph(n))
				.forEach(sub -> newHead.neighbors.add(sub));
		return newHead;
	}

	private BinaryTreeNode lowestCommonAncestor(BinaryTreeNode root,
			BinaryTreeNode node1, BinaryTreeNode node2) {
		if (root == null)
			return null;
		if (root == node1 || root == node2)
			return root;
		BinaryTreeNode left = lowestCommonAncestor(root.left, node1, node2);
		BinaryTreeNode right = lowestCommonAncestor(root.right, node1, node2);
		if (left != null && right != null)
			return root;
		return left == null ? right : left;
	}

	private static BinaryTreeNode createTreeByPreorderAndInorderTraversal(
			List<Integer> inorder, List<Integer> preorder) {
		if (inorder.size() == 0) {
			return null;
		}
		if (inorder.size() == 1) {
			if (inorder.get(0) == preorder.get(0))
				return new BinaryTreeNode(inorder.get(0));
			else
				return null;
		}
		BinaryTreeNode root = new BinaryTreeNode(preorder.get(0));
		for (int index = 0; index < inorder.size(); index++) {
			if (inorder.get(index) != preorder.get(0))
				continue;
			List<Integer> beforeIn = getSublist(inorder, 0, index);
			List<Integer> afterIn = getSublist(inorder, index + 1,
					inorder.size());
			List<Integer> beforePre = getSublist(preorder, 1, index + 1);
			List<Integer> afterPre = getSublist(preorder, index + 1,
					preorder.size());
			BinaryTreeNode left, right;
			left = createTreeByPreorderAndInorderTraversal(beforeIn, beforePre);
			right = createTreeByPreorderAndInorderTraversal(afterIn, afterPre);
			if (left != null || beforeIn.isEmpty()) {
				if (right != null || afterIn.isEmpty()) {
					root.left = left;
					root.right = right;
					return root;
				}
			}
		}
		return null;
	}

	private static List<Integer> getSublist(List<Integer> list, int start,
			int end) {
		return list.subList(start, end);
	}

	private static List<Integer> toList(int[] array) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int a : array) {
			list.add(a);
		}
		return list;
	}

	/*
	 * Implement a function to check if a tree is balanced. For the purposes of
	 * this question, a balanced tree is defined to be a tree such that no two
	 * leaf nodes differ in distance from the root by more than one.
	 */
	private static boolean isBalanced(BinaryTreeNode root) {
		int[] depths = getShortAndLongDistance(root);
		return depths[1] - depths[0] <= 1;
	}

	private static int[] getShortAndLongDistance(BinaryTreeNode root) {
		if (root == null)
			return new int[] { -1, -1 };
		int[] left = getShortAndLongDistance(root.left);
		int[] right = getShortAndLongDistance(root.right);
		int max = Math.max(left[1], right[1]);
		int min = Math.min(left[0], right[0]);
		return new int[] { min + 1, max + 1 };
	}

	/*
	 * Given a directed graph, design an algorithm to find out whether there is
	 * a route between two nodes.
	 */
	private static boolean isConnected(GraphNode node1, GraphNode node2) {
		Stack<GraphNode> stack = new Stack<GraphNode>();
		stack.push(node1);
		while (!stack.isEmpty()) {
			GraphNode current = stack.pop();
			if (current == node2)
				return true;
			current.neighbors.forEach(n -> stack.push(n));
		}
		return false;
	}

	/*
	 * Given a sorted (increasing order) array, write an algorithm to create a
	 * binary tree with minimal height.
	 */
	private static BinaryTreeNode createTreeWithMinimumHeight(int[] numbers,
			int start, int end) {
		int middle = (start + end) / 2;
		BinaryTreeNode node = new BinaryTreeNode(numbers[middle]);
		BinaryTreeNode left = null, right = null;
		if (middle > start)
			left = createTreeWithMinimumHeight(numbers, start, middle - 1);
		if (middle < end)
			right = createTreeWithMinimumHeight(numbers, middle + 1, end);
		node.left = left;
		node.right = right;
		return node;
	}

	/*
	 * Given a binary search tree, design an algorithm which creates a linked
	 * list of all the nodes at each depth (eg, if you have a tree with depth D,
	 * you’ll have D linked lists).
	 */
	private static LinkedList<LinkedList<BinaryTreeNode>> createLinkedListsFromTree(
			BinaryTreeNode root) {
		if (root == null)
			return null;
		LinkedList<LinkedList<BinaryTreeNode>> result = new LinkedList<LinkedList<BinaryTreeNode>>();
		LinkedList<BinaryTreeNode> current = new LinkedList<BinaryTreeNode>();
		current.add(root);
		LinkedList<LinkedList<BinaryTreeNode>> left = null, right = null;
		if (root.left != null)
			left = createLinkedListsFromTree(root.left);
		if (root.right != null)
			right = createLinkedListsFromTree(root.right);
		result.add(current);
		if (left == null && right == null) {
			return result;
		}
		if (left != null && right != null) {
			for (int i = 0; i < Math.max(left.size(), right.size()); i++) {
				if (i < left.size() && i < right.size()) {
					LinkedList<BinaryTreeNode> list = new LinkedList<BinaryTreeNode>();
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
	 */
	private static BinaryTreeNode findNextInorder(BinaryTreeNode node) {
		if (node.right != null) {
			return getLeftMostChild(node);
		}
		while (node.parent != null && node.parent.right == node)
			node = node.parent;
		if (node.parent == null)
			return null;
		return node.parent;
	}

	private static BinaryTreeNode getLeftMostChild(BinaryTreeNode node) {
		BinaryTreeNode current;
		for (current = node.right; current.left != null; current = current.left)
			;
		return current;
	}

	/* find the most recent common ancestor. */
	private static BinaryTreeNode findCommonAncestor(BinaryTreeNode root,
			BinaryTreeNode node1, BinaryTreeNode node2) {
		int left = coveredCount(root.left, node1, node2);
		if (left == 2)
			return findCommonAncestor(root.left, node1, node2);
		int right = coveredCount(root.right, node1, node2);
		if (right == 2)
			return findCommonAncestor(root.right, node1, node2);
		return root;
	}

	private static int coveredCount(BinaryTreeNode root, BinaryTreeNode node1,
			BinaryTreeNode node2) {
		return (covers(root, node1) ? 1 : 0) + (covers(root, node2) ? 1 : 0);
	}

	private static boolean covers(BinaryTreeNode root, BinaryTreeNode node) {
		if (root == null)
			return false;
		if (root == node)
			return true;
		return covers(root.left, node) || covers(root.right, node);
	}

	/*
	 * You have two very large binary trees: T1, with millions of nodes, and T2,
	 * with hundreds of nodes. Create an algorithm to decide if T2 is a subtree
	 * of T1.
	 */
	private static boolean isSubTree(BinaryTreeNode rootMillion,
			BinaryTreeNode rootHundred) {
		if (isTreeMatch(rootMillion, rootHundred))
			return true;
		return isSubTree(rootMillion.left, rootHundred)
				|| isSubTree(rootMillion.right, rootHundred);
	}

	private static boolean isTreeMatch(BinaryTreeNode node1,
			BinaryTreeNode node2) {
		if (node2 == null)
			return true;
		if (node1 == null)
			return false;
		if (node1.value == node2.value) {
			return isTreeMatch(node1.left, node2.left);
		} else
			return false;
	}

	/*
	 * You are given a binary tree in which each node contains a value. Design
	 * an algorithm to print all paths which sum up to that value. Note that it
	 * can be any path in the tree - it does not have to start at the root.
	 */
	private static void findPath(BinaryTreeNode root, int sum) {
		if (root == null)
			return;
		ArrayList<ArrayList<BinaryTreeNode>> paths = findPathStartingAt(root,
				sum);
		paths.forEach(p -> {
			System.out.print("Path:");
			p.forEach(n -> System.out.print(" " + n.value));
			System.out.println();
		});
		findPath(root.left, sum);
		findPath(root.right, sum);
	}

	private static ArrayList<ArrayList<BinaryTreeNode>> findPathStartingAt(
			BinaryTreeNode node, int num) {
		ArrayList<ArrayList<BinaryTreeNode>> result = new ArrayList<ArrayList<BinaryTreeNode>>();
		if (node == null || node.value > num)
			return result;
		if (node.value == num) {
			ArrayList<BinaryTreeNode> path = new ArrayList<BinaryTreeNode>();
			path.add(node);
			result.add(path);
			return result;
		}
		result.addAll(findPathStartingAt(node.left, num - node.value));
		result.addAll(findPathStartingAt(node.right, num - node.value));
		result.forEach(p -> p.add(0, node));
		return result;
	}

	private static void dijkstraAlgorithm(GraphNode startNode,
			List<GraphNode> allNodes) {
		PriorityQueue<GraphNode> queue = new PriorityQueue<GraphNode>();
		for (GraphNode n : allNodes) {
			n.distance = Integer.MAX_VALUE;
		}
		startNode.distance = 0;
		queue.addAll(allNodes);
		while (queue.size() != 0) {
			GraphNode current = queue.remove();
			for (GraphNode nei : current.neighbors) {
				nei.distance = Math.min(nei.distance, nei.value
						+ current.distance);
			}
		}
	}

	private static void testPriorityQueue() {
		PriorityQueue<GraphNode> queue = new PriorityQueue<GraphNode>();
		GraphNode[] nodes = new GraphNode[] { new GraphNode(1),
				new GraphNode(2), new GraphNode(3) };
		for (GraphNode n : nodes) {
			queue.add(n);
		}
		System.out.println(queue.remove().value);
		nodes[2].value = -3;
		System.out.println(queue.remove().value);
	}

	private static class AStarNode implements Comparable<AStarNode> {
		List<AStarNode> neighbors;
		List<Integer> distances;
		int gScore;
		int fScore;

		public int compareTo(AStarNode another) {
			return fScore - another.fScore;
		}
	}

	private static int estimateDistance(AStarNode node) {
		return 0;
	}

	private static int AStarSearch(AStarNode start, AStarNode goal) {
		Queue<AStarNode> openSet = new PriorityQueue<AStarNode>();
		HashSet<AStarNode> closeSet = new HashSet<AStarNode>();
		openSet.add(start);
		start.gScore = 0;
		start.fScore = estimateDistance(start);
		while (openSet.size() != 0) {
			AStarNode current = openSet.peek();
			if (current == goal) {
				return current.gScore;
			}
			openSet.remove(current);
			closeSet.add(current);
			for (int i = 0; i < current.neighbors.size(); i++) {
				AStarNode nei = current.neighbors.get(i);
				if (closeSet.contains(nei)) {
					continue;
				}
				int tentativeGscore = current.gScore + current.distances.get(i);
				if (openSet.contains(nei) || tentativeGscore < nei.gScore) {
					nei.gScore = tentativeGscore;
					nei.fScore = estimateDistance(nei) + nei.gScore;
					if (!openSet.contains(nei))
						openSet.add(nei);
				}
			}
		}
		return Integer.MAX_VALUE;
	}

	private static int sequence = 0;

	private static BinaryTreeNode InorderVisit(BinaryTreeNode node, int target) {
		BinaryTreeNode before = null, after = null;
		if (node.left != null) {
			before = InorderVisit(node.left, target);
			if (before != null)
				return before;
		}
		sequence++;
		if (sequence == target)
			return node;
		if (node.right != null) {
			after = InorderVisit(node.right, target);
			if (after != null)
				return after;
		}
		return null;
	}

	private static int getKthElementInBinarySearchTree(BinaryTreeNode root,
			int k) {
		sequence = 0;
		BinaryTreeNode node = InorderVisit(root, k);
		return node != null ? node.value : -1;
	}

	private static BinaryTreeNode constructBinarySearchTree(int[] nums) {
		if(nums.length == 0)
			return null;
		BinaryTreeNode root = new BinaryTreeNode(nums[0]);
		for(int i = 1; i < nums.length; i ++) {
			int value = nums[i];
			BinaryTreeNode current = root;
			BinaryTreeNode parent = null;
			boolean found = false;
			boolean left = false;
			while(current != null) {
				parent = current;
				if(value > current.value){
					current = current.right;
					left = false;
				}else if(value < current.value) {
					current = current.left;
					left = true;
				}else {
					found = true;
				}
			}
			if(!found) {
				if(left)
					parent.left = new BinaryTreeNode(value);
				else
					parent.right = new BinaryTreeNode(value);
			}
		}
		return root;
	}

	private static class DoubleLinkedListNode {
		int value;
		DoubleLinkedListNode before;
		DoubleLinkedListNode after;
	}

	private static DoubleLinkedListNode[] convertBinarySearchTreeToSortedDoubleLinkedList(BinaryTreeNode
			node) {
		if(node == null)
			return new DoubleLinkedListNode[]{null, null};
		DoubleLinkedListNode[] before = convertBinarySearchTreeToSortedDoubleLinkedList(node.left);
		DoubleLinkedListNode current = new DoubleLinkedListNode();
		current.value = node.value;
		if(before[1] != null) {
			before[1].after = current;
			current.before = before[1];
		}
		DoubleLinkedListNode[] after = convertBinarySearchTreeToSortedDoubleLinkedList(node.right);
		if(after[0] != null) {
			after[0].before = current;
			current.after = after[0];
		}
		return new DoubleLinkedListNode[]{before[0] == null ? current :
			before[0], after[1] == null ? current : after[1]};
	}

	private static void testConvertToLinkedList() {
		BinaryTreeNode root = constructBinarySearchTree(new int[] { 7, 10, 4, 3, 1, 2, 8, 11 });
		DoubleLinkedListNode start = convertBinarySearchTreeToSortedDoubleLinkedList(root)[0];
		while(start != null) {
			System.out.print(start.value + "<->");
			start = start.after;
		}
	}

	private static BinaryTreeNode convertSortedListToBalancedSearchTree(List<Integer> list) {
		if(list.size() == 0)
			return null;
		int index = list.size()/2;
		int middle = list.get(index);
		BinaryTreeNode current = new BinaryTreeNode(middle);
		current.left = convertSortedListToBalancedSearchTree(list.subList(0,
				index));
		current.right = convertSortedListToBalancedSearchTree(list.subList(
				index + 1, list.size()));
		return current;
	}

	private static int[] checkIfBinaryTreeBalancedHelper(BinaryTreeNode root) {
		if(root == null) {
			return new int[]{0,0};
		}
		int[] left = checkIfBinaryTreeBalancedHelper(root.left);
		int[] right = checkIfBinaryTreeBalancedHelper(root.right);
		int min = Math.min(left[0], right[0]) + 1;
		int max = Math.max(left[1], right[1]) + 1;
		return new int[]{min, max};
	}

	private static boolean checkIfBalanced(BinaryTreeNode node) {
		int[] depth = checkIfBinaryTreeBalancedHelper(node);
		return depth[1] - depth[0] < 2;
	}

	private static void testConvertToBalanceTree() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		BinaryTreeNode node = convertSortedListToBalancedSearchTree(list);
		System.out.println(checkIfBalanced(node));
		list.add(4);
		node = convertSortedListToBalancedSearchTree(list);
		System.out.println(checkIfBalanced(node));
		for(int i = 5; i < 100; i ++) {
			list.add(i);
			node = convertSortedListToBalancedSearchTree(list);
			System.out.println(checkIfBalanced(node));
		}
	}

	private static class Edge {
		int parent;
		int son;
		boolean left;
		public Edge(int parent, int son, boolean left) {
			this.parent = parent;
			this.son = son;
			this.left = left;
		}
	}

	private static BinaryTreeNode createBinaryTree(List<Edge> edges) {
		HashMap<Integer, BinaryTreeNode> map = new HashMap<Integer,
			BinaryTreeNode>();
		HashSet<BinaryTreeNode> sons = new HashSet<BinaryTreeNode>();
		for(Edge e : edges) {
			BinaryTreeNode p = null, s = null;
			if(map.containsKey(e.parent)) {
				p = map.get(e.parent);
			}else {
				p = new BinaryTreeNode(e.parent);
				map.put(e.parent, p);
			}

			if(map.containsKey(e.son)) {
				s = map.get(e.son);
			}else {
				s = new BinaryTreeNode(e.son);
				map.put(e.son, s);
			}
			if(e.left) {
				p.left = s;
			} else {
				p.right = s;
			}
			sons.add(s);
		}
		for(BinaryTreeNode n : map.values()) {
			if(!sons.contains(n)) {
				return n;
			}
		}
		return null;
	}
	private static void testGetLargestBinarySearchTree() {
		BinaryTreeNode root = createTree();
		getLargestBST2(root);
		System.out.println(TreeInfo.globalMax);
	}

	private static BinaryTreeNode createTree() {
		List<Edge> edges = new ArrayList<Edge>();
		edges.add(new Edge(10, 5, true));
		edges.add(new Edge(10, 15, false));
		edges.add(new Edge(5, 1, true));
		edges.add(new Edge(5, 8, false));
		edges.add(new Edge(15, 7, false));
		return createBinaryTree(edges);
	}

	private static int[] getLargestBinarySearchTree(BinaryTreeNode root) {
		//[0] means the largest rooted at the current node
		//[1] means the largest overall
		if(root == null) {
			return new int[]{0,0};
		}
		int[] left = getLargestBinarySearchTree(root.left);
		int[] right = getLargestBinarySearchTree(root.right);
		int leftV = root.left == null ? Integer.MAX_VALUE : root.left.value;
		int rightV = root.right == null ? Integer.MIN_VALUE : root.right.value;
		int maxRootAtCurrent = root.value > leftV ? left[0] + 1 : 1;
		maxRootAtCurrent = root.value < rightV ? right[0] + maxRootAtCurrent :
			maxRootAtCurrent;
		int maxOverall = Math.max(maxRootAtCurrent, Math.max(left[1], right[1]));
		return new int[] {maxRootAtCurrent, maxOverall};
	}

	private static class TreeInfo {
		static int globalMax = Integer.MIN_VALUE;
		final int size;
		final int max;
		final int min;
		public TreeInfo(int size, int max, int min) {
			this.size = size;
			this.max = max;
			this.min = min;
			globalMax = Math.max(globalMax, this.size);
		}

	}

	private static TreeInfo getLargestBST2(BinaryTreeNode root) {
		if(root == null) {
			return new TreeInfo(0, Integer.MIN_VALUE,
				Integer.MAX_VALUE);
		}
		TreeInfo leftInfo = getLargestBST2(root.left);
		TreeInfo rightInfo = getLargestBST2(root.right);
		int max = root.value;
		int min = root.value;
		int size = 1;
		if(leftInfo.size > 0 && leftInfo.max < root.value) {
			max = Math.max(max, leftInfo.max);
			min = Math.min(min, leftInfo.min);
			size += leftInfo.size;
		}
		if(rightInfo.size > 0 && rightInfo.min > root.value) {
			max = Math.max(max, rightInfo.max);
			min = Math.min(min, rightInfo.min);
			size += rightInfo.size;
		}
		return new TreeInfo(size, max, min);
	}

	private static List<Integer> printPostOrderTraversalWithoutUsingRecursion(
			BinaryTreeNode root) {
		List<Integer> results = new ArrayList<Integer>();
		HashSet<BinaryTreeNode> closedNodes = new HashSet<BinaryTreeNode>();
		Stack<BinaryTreeNode> openNodes = new Stack<BinaryTreeNode>();
		openNodes.add(root);
		while(openNodes.size() != 0) {
			BinaryTreeNode head = openNodes.peek();
			boolean leftOk = true;
			boolean rightOk = true;
			if(head.left != null && !closedNodes.contains(head.left)) {
				openNodes.push(head.left);
				leftOk = false;
			}
			if(head.right != null && !closedNodes.contains(head.right)) {
				openNodes.push(head.right);
				leftOk = false;
			}
			if(leftOk && rightOk) {
				openNodes.pop();
				results.add(head.value);
				closedNodes.add(head);
			}
		}
		return results;
	}

	private static void testPostOrderTraversal() {
		List<Integer> list = printPostOrderTraversalWithoutUsingRecursion
				(createTree());
		for(int i : list) {
			System.out.print(i + " ");
		}
	}

	private static List<BinaryTreeNode> getAllLeafNode(BinaryTreeNode root) {
		Stack<BinaryTreeNode> openList = new Stack<BinaryTreeNode>();
		HashSet<BinaryTreeNode> closeList = new HashSet<BinaryTreeNode>();
		openList.push(root);
		List<BinaryTreeNode> leaves = new ArrayList<BinaryTreeNode>();
		while(openList.size() != 0) {
			BinaryTreeNode current = openList.peek();
			boolean leftOk = true, rightOk = true;
			if(current.right != null && !closeList.contains(current.right)) {
				openList.push(current.right);
				rightOk = false;
			}

			if(current.left != null && !closeList.contains(current.left)) {
				openList.push(current.left);
				leftOk = false;
			}
			if(leftOk && rightOk) {
				openList.pop();
				closeList.add(current);
				if(current.left == null && current.right == null) {
					leaves.add(current);
				}
			}
		}
		return leaves;
	}

	private static List<BinaryTreeNode> getLeftEdge(BinaryTreeNode root) {
		List<BinaryTreeNode> results = new ArrayList<BinaryTreeNode>();
		for(BinaryTreeNode current = root; current.left != null ||
			current.right != null; current = current.left != null?
				current.left : current.right){
			results.add(current);
		}
		return results;
	}

	private static List<BinaryTreeNode> getRightEdge(BinaryTreeNode root) {
		List<BinaryTreeNode> results = new ArrayList<BinaryTreeNode>();
		for(BinaryTreeNode current = root; current.left != null ||
			current.right != null; current = current.right == null ?
				current.left : current.right) {
			results.add(current);
		}
		return results;
	}

	private static List<Integer> printEdgesOfBinaryTree(BinaryTreeNode root) {
		List<BinaryTreeNode> list = getLeftEdge(root);
		for(BinaryTreeNode r : getAllLeafNode(root)) {
			if(!list.contains(r))
				list.add(r);
		}
		List<BinaryTreeNode> right =  getRightEdge(root);
		for(int i = right.size() - 1; i >= 0; i --) {
			BinaryTreeNode r = right.get(i);
			if(!list.contains(r))
				list.add(r);
		}
		List<Integer> results = new ArrayList<Integer>();
		for(BinaryTreeNode n : list) {
			results.add(n.value);
		}
		return results;
	}

	private static void testPrintEdgeOfTree() {
		BinaryTreeNode root = createTree();
		List<Integer> nodes = printEdgesOfBinaryTree(root);
		for(int i : nodes) {
			System.out.print(i + " ");
		}
	}
	private static List<Integer> visitNodeByLayer(BinaryTreeNode root) {
		List<Integer> nums = new ArrayList<Integer>();
		Queue<BinaryTreeNode> queue = new LinkedList<BinaryTreeNode>();
		queue.add(root);
		while(queue.size() != 0) {
			BinaryTreeNode current = queue.remove();
			queue.add(current.left);
			queue.add(current.right);
			nums.add(current.value);
		}
		return nums;
	}
	private static class SpecialNode extends BinaryTreeNode {
		SpecialNode() {
			super(-1);
		}
	}
	
	private static void printZigzagLayers(BinaryTreeNode root) {
		List<List<BinaryTreeNode>> layers = getBinaryTreeLayers(root);
		boolean reverse = false;
		for(List<BinaryTreeNode> l : layers) {
			if(reverse) 
					Collections.reverse(l);
			reverse = !reverse;
			for(BinaryTreeNode n : l) {
				System.out.print(n.value + " ");
			}
			System.out.println();
		}
	}

	private static void testPrintLayersZigzag() {
		BinaryTreeNode root = createTree();
		printZigzagLayers(root);
	}

	private static List<List<BinaryTreeNode>> getBinaryTreeLayers(BinaryTreeNode root) {
		List<List<BinaryTreeNode>> layers = new ArrayList<List<BinaryTreeNode>>();
		Queue<BinaryTreeNode> q = new LinkedList<BinaryTreeNode>();
		q.add(new SpecialNode());
		q.add(root);
		while(q.size() != 0) {
			BinaryTreeNode node = q.remove();
			if(node instanceof SpecialNode) {
				if(q.size() != 0) {
					layers.add(new ArrayList<BinaryTreeNode>());
					q.add(node);
				}
			}else {
				if(node.left != null){
					q.add(node.left);
					node.left.parent = node;
				}
				if(node.right != null) {
					q.add(node.right);
					node.right.parent = node;
				}
				layers.get(layers.size() - 1).add(node);
			}
		}
		return layers;
	}

	private static void prettyPrintBinaryTree(BinaryTreeNode root) {
		List<List<BinaryTreeNode>> layers = getBinaryTreeLayers(root);
		List<List<Integer>> positions = getNodesPositions(layers);	

		String prefix = "";
		String gap = " ";
		String tree = "";
		
		for(int i = layers.size() - 1; i >= 0; i--) {
			StringBuilder sb = new StringBuilder();
			List<BinaryTreeNode> lay  = layers.get(i);
			List<Integer> pos = positions.get(i);
			sb.append(prefix);
			int preP = 0;
			for(int j = 0; j < lay.size(); j ++) {
				int v = lay.get(j).value;
				int p = pos.get(j);
				for(int count = preP; count < p - 1; count ++) {
					sb.append(gap);
					sb.append(" ");
				}
				sb.append(gap);
				sb.append(v);
				preP = p;
			}
	//		prefix = gap.substring(1);
			gap = (gap + gap + "  ").substring(1);
			tree = sb.toString() + "\n" + tree;
		}
		System.out.print(tree);
	}

	private static List<List<Integer>> getNodesPositions(
			List<List<BinaryTreeNode>> layers) {
		List<List<Integer>> positions = new ArrayList<List<Integer>>();
		List<Integer> list = new ArrayList<Integer>();
		list.add(0);
		positions.add(list);	
		for(int i = 1; i < layers.size(); i++) {
			list = new ArrayList<Integer>();
			for(BinaryTreeNode node : layers.get(i)) {
				int preP = positions.get(i - 1).get(layers.
					get(i - 1).indexOf(node.parent));
				int p = preP * 2 + (node == node.parent.left ? 0 : 1);
				list.add(p);
			}
			positions.add(list);
		}
		return positions;
	}
	
	private static void testPrettyPrint() {
		BinaryTreeNode root = createTree();
		prettyPrintBinaryTree(root);
	}
	
	private static void printTreeLayer(BinaryTreeNode node, int layer) {
		if(node == null)
			return;
		if(layer == 1) {
			System.out.print(node.value + " ");
		}
		printTreeLayer(node.left, layer - 1);
		printTreeLayer(node.right, layer - 1);
	}

	private static int getBinaryTreeDepth(BinaryTreeNode root) {
		if(root == null)
			return 0;
		else 
			return Math.max(getBinaryTreeDepth(root.left),
				getBinaryTreeDepth(root.right)) + 1;
	}

	private static void printLayerByDepthFirstSearch(BinaryTreeNode root) {
		for(int i = 1; i <= getBinaryTreeDepth(root); i++) {
			printTreeLayer(root, i);
			System.out.println();
		}
	}

	private static void testPrintLayerByDFS() {
		printLayerByDepthFirstSearch(createTree());
	}	
	
	private static boolean ifTreeBST(BinaryTreeNode root) {
		return ifTreeIsBSTHelper(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	private static boolean ifTreeIsBSTHelper(BinaryTreeNode root, int min, 
			int max) {
		if(root == null)
			return true;
		if(root.value <= min || root.value >= max)
			return false;
		return ifTreeIsBSTHelper(root.left, min, root.value) && 
			ifTreeIsBSTHelper(root.right, root.value, max);
	}

	public static void main(String[] args) {
		//testConvertToLinkedList();
		//testConvertToBalanceTree();
		//testGetLargestBinarySearchTree();
		//testPostOrderTraversal();
		//testPrintEdgeOfTree();
		//testPrettyPrint();
		testPrintLayerByDFS();
	}

}
