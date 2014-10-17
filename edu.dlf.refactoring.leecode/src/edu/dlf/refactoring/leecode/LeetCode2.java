import java.util.List;
import java.util.Stack;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.PriorityQueue;

class LeetCode2 {

	private static class Node {
		public Node left;
		public Node right;
		public Node parent;
		public int value;
		public Node(int value) {this.value = value;}
	}


	private Node[] convertDoubleLinkedList(Node root) {
		if(null == root) return null;
		Node l = root.left;
		Node r = root.right;
		Node head = root, tail = root;
		root.right = head;
		head.left = root;
		Node[] left = convertDoubleLinkedList(l);
		if(left != null) {
			left[1].right = head;
			head.left = left[1];
			tail.right = left[0];
			left[0].left = tail;
			head = left[0];
		} 
		Node[] right = convertDoubleLinkedList(r);
		if(right != null) {
			tail.right = right[0];
			right[0].left = tail;
			right[1].right = head;
			head.left = right[1];
			tail = right[1];
		}
		return new Node[]{head, tail};
	}

	private static boolean isBST(Node root, int min, int max) {
		if(null == root) return true;
		return (root.value > min && root.value < max 
			&& isBST(root.left, min, root.value) 
			&& isBST(root.right, root.value, max));
	}

	
	public static void main(String[] args) {

	}
}
