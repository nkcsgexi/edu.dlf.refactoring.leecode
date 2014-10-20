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
	private static int[] subarray(int[] a, int s, int e) {
		int[] re = new int[e - s + 1];
		for(int i = 0; i < re.length; i ++)
			re[i] = a[s + i];
		return re;
	}

	private static int getKth(int[] a, int[] b, int k) {
		int al = a.length;
		int bl = b.length;
		int i = (int) (((double) al / (al + bl)) * (k - 1));
		int j = k - 1 - i;
		int ai = i == al ? Integer.MAX_VALUE : a[i];
		int ai1 = i == 0 ? Integer.MIN_VALUE : a[i - 1];
		int bj = j == bl ? Integer.MAX_VALUE : b[j];
		int bj1 = j == 0 ? Integer.MIN_VALUE : b[j - 1];
		if(ai >= bj1 && ai <= bj)
			return ai;
		if(bj >= ai1 && bj <= ai)
			return bj;
		if(ai > bj)
			return getKth(subarray(a, i + 1, al - 1), b,
					k - i - 1);
		else
			return getKth(a, subarray(b, j + 1, bl - 1),
					j - j - 1);
	}



	public static void main(String[] args) {

	}
}
