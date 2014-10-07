import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.Collections;
class LeetCode {

	private static class GNode {
		public final int value;
		public final List<GNode> neighbors = new ArrayList<GNode>();
		public GNode(int value) { this.value = value; }

		public void BFS(Consumer<GNode> v) {
			Queue<GNode> q = new LinkedList<GNode>();
			HashSet<GNode> black = new HashSet<GNode>();
			HashSet<GNode> grey = new HashSet<GNode>();
			q.add(this);
			while(q.size() != 0) {
				GNode n = q.remove();
				v.accept(n);
				for(GNode nei :n.neighbors) {
					if(black.contains(nei) || grey.contains
							(nei)) continue;
					q.add(nei);
					grey.add(nei);
				}
				grey.remove(n);
				black.add(n);
			}
		}

		public void DFS(Consumer<GNode> v) {
			Stack<GNode> s = new Stack<GNode>();
			s.push(this);
			HashSet<GNode> grey = new HashSet<GNode>();
			HashSet<GNode> black = new HashSet<GNode>();
			while(s.size() != 0) {
				GNode n = s.pop();
				v.accept(n);
				boolean nonei = true;
				for(GNode nei : n.neighbors) {
					if(grey.contains(nei) || 
						black.contains(nei)) 
							continue;
					s.push(nei);
					grey.add(nei);
					nonei = false;
				}
				if(nonei) {
					grey.remove(n);
					black.add(n);
				}
			}
		}
	}

	private static GNode cloneGraph(GNode root) {
		Queue<GNode> q = new LinkedList<GNode>();
		Hashtable<GNode, GNode> table = new Hashtable<GNode, GNode>();
		q.add(root);
		GNode nr = new GNode(root.value);
		table.put(root, nr);
		while(q.size() != 0) {
			GNode n = q.remove();
			GNode nn = table.get(n); 
			for(GNode nei : n.neighbors) {
				if(!table.contains(nei)) {
					GNode nnei = new GNode(nei.value);
					nn.neighbors.add(nnei);
					table.put(nei, nnei);
					q.add(nei);	
				} else {
					nn.neighbors.add(table.get(nei));
				}
			}
		}
		return table.get(root);
	}

	private static GNode cloneGraph2(GNode root) {
		Hashtable<GNode, GNode> table = new Hashtable<GNode, GNode>();
		Stack<GNode> s = new Stack<GNode>();
		s.push(root);
		GNode nr = new GNode(root.value);
		table.put(root, nr);
		while(s.size() != 0) {
			GNode n = s.pop();
			GNode nn = table.get(n);
			for(GNode nei : n.neighbors) {
				GNode nnei = null;
				if(!table.contains(nei)) {
					s.push(nei);
					nnei = new GNode(nei.value);
					table.put(nei, nnei);
				} else 
					nnei = table.get(nei);
				nn.neighbors.add(nnei);
			}
		}
		return nr;
	}

	private static void cloneTest() {
		GNode[] nodes = new GNode[5];
		for(int i = 0; i < nodes.length; i ++)
			nodes[i] = new GNode(i);
		nodes[0].neighbors.add(nodes[1]);
		nodes[0].neighbors.add(nodes[2]);
		nodes[1].neighbors.add(nodes[3]);
		nodes[1].neighbors.add(nodes[4]);
		nodes[3].neighbors.add(nodes[2]);
		GNode nr = cloneGraph2(nodes[0]);
		nodes[0].BFS(n -> System.out.println(n.value));
		nr.BFS(n -> System.out.println(n.value));
	}

	private static int getExpand(String s, int p, int min) {
		return 1;	
	}

	private static String palindrome(String s) {
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		for(char c : s.toCharArray()) {
			sb.append(c);
			sb.append('#');
		}
		String ns = sb.toString();
		int[] lens = new int[ns.length()];
		int far = -1;
		int farI = -1;
		for(int i = 0; i < ns.length(); i++) {
			int min = 0;
			if(far > i) {
				int m = 2 * farI - i;
				min = Math.min(lens[m], farI - i);
			}
			lens[i] =  getExpand(ns, i, min);
			if(lens[i] + i > far) {
				far = lens[i] + i;
				farI = i;
			}
		}
		int max = Integer.MIN_VALUE;
		int mi = 0;
		for(int i = 0; i < lens.length; i ++) {
			if(max < lens[i]) {
				max = lens[i];
				mi = i;
			}
		}
		return ns.substring(mi - max, max * 2);	
	}
			
 	private static boolean regularMatch(String s, String p) {
		if(s.equals("") && p.equals("")) return true;
		char c = p.charAt(0);
		if(p.charAt(1) == '+')
			return s.charAt(0) == c && 
				regularMatch(s.substring(1), 
					c + "*" + p.substring(2));
		else if(p.charAt(1) == '*') {
			String np = p.substring(2);
			int count = 0; 
			for(; s.charAt(count) == c; count++); 
			for(int i = 0; i <= count; i++)
				if(regularMatch(s.substring(count), np)) 
					return true;
			return false;
				
		}else 
			return s.charAt(0) == c && regularMatch(
					s.substring(1), p.substring(1));
	}
		
	private static int painterProblem(int[] data, int K) {
		int N = data.length;
		int[][] matrix = new int[K][N];
		matrix[0][0] = data[0];
		for(int i = 1; i < N; i++)
			matrix[0][i] = matrix[0][i - 1] + data[i];
		for(int i = 1; i < K; i ++) {
			for(int j = 0; j < N; j ++) {
				int min = Integer.MAX_VALUE;
				for(int m = 0; m <= j; m ++) {
					int remain = 0;
					for(int o = m + 1; o <= j; o ++)
						remain += data[o];
					int nm = Math.max(remain, matrix[i - 1][m]);
					min = nm < min ? nm : min;
				}
				matrix[i][j] = min;
			}
		}
		return matrix[K - 1][N - 1];
	}	

	private static boolean painterChecker(int[] data, int K, int max) {
		int needed = 1;
		int current = 0;
		for(int d : data) {
			if(current + d <= max) {
				current += d;
				continue;
			}
			current = d;
			needed ++;
			if(needed > K)
				return false;
		}
		return true;
	}
	
	private static void testPaint() {
		int[] data = { 2, 3, 4, 4, 5, 1, 6, 8};
		System.out.println(painterProblem(data, 3));
	}

	private static class LinkedNode {
		public LinkedNode next;
		public int value;
		public LinkedNode(int value) {this.value = value;}
	}
	
	private static LinkedNode insertToCircle(LinkedNode n, int v) {
		if(null == n) {
		       LinkedNode node = new LinkedNode(v);
		       node.next = node;
		       return node;
		}
		LinkedNode start = n;
		LinkedNode pre = start;
		LinkedNode current = null;
		do {
			current = pre.next;
			if(pre.value <= v && v <= current.value) break;
			if(pre.value > current.value && (v >= pre.value ||
					v <= current.value)) break;
			pre = current;
		}while(pre.next != start);
		LinkedNode nn = new LinkedNode(v);
		pre.next = nn;
		nn.next = current;
		return n;
	}
		
	private static int reverseBit(int num, int i, int j) {
		int m1 = (num >> i) & 1;
		int m2 = (num >> j) & 1;
		if((m1 ^ m2) == 1) {
			num ^= (1 << i) | (1 << j);
		}
		return num;
	}
	private static int reverseBit(int num) {
		num = ((0x55555555 & num) << 1) | ((0xAAAAAAAA & num) >> 1);
		num = ((0x33333333 & num) << 2) | ((0xCCCCCCCC & num) >> 2);
		num = ((0x0f0f0f0f & num) << 4) | ((0xf0f0f0f0 & num) >> 4);
		num = ((0x00ff00ff & num) << 8) | ((0xff00ff00 & num) >> 8);
		num = ((0x0000ffff & num) << 16) | ((0xffff0000 & num) >> 16);
		return num;
	}

	private static class Node {
		public Node left;
		public Node right;
		public int value;
		public Node parent;
		public Node(int value) {this.value = value;}
	}




	private static class LCA {
		
		private static Node treeNoParent(Node root, Node p, Node q) {
			if(null == root || null == p || null == q) 
				return null;
			if(root == p || root == q) return root;
			Node left = treeNoParent(root.left, p, q);
			Node right = treeNoParent(root.right, p, q);
			return left == null ? right : (right == null ? left : 
				root);
		}

		private static Node searchTreeNoParent(Node root, Node p, 
				Node q) {
			if(root == null || p == null || q == null) return null;
			if(root.value > Math.max(p.value, q.value))
				return searchTreeNoParent(root.left, p, q);
			else if(root.value < Math.min(p.value, q.value))
				return searchTreeNoParent(root.right, p, q);
			else return root;
		}
		private static int getHeight(Node p) {
			int h = 1;
			while(p.parent != null) {
				h ++;
				p = p.parent;
			}
			return h;
		}
		private static Node treeParent(Node p, Node q) {
			int h1 = getHeight(p);
			int h2 = getHeight(q);
			if(h1 < h2) {
				int t = h2;
				h2 = h1;
				h1 = t;
				Node tn = p;
				p = q;
				q = tn;
			}
			while(h1 > h2) {
				p = p.parent;
				h1 --;
			}
			while(p != q) {
				p = p.parent;
				q = q.parent;
			}
			return p;
		}

	}

	private static void inorder(Node n) {
		if(null == n) return;
		inorder(n.left);
		System.out.println(n.value);
		inorder(n.right);
	}
	private static void preorder(Node n) {
		if(null == n) return;
		System.out.println(n.value);
		preorder(n.left);
		preorder(n.right);
	}

	private static void postorder(Node n) {
		if(null == n) return;
		postorder(n.left);
		postorder(n.right);
		System.out.println(n.value);
	}

	private static int[] subarray(int[] data, int start, int end) {
		int[] result = new int[end - start + 1];
		for(int i = 0; i < result.length; i ++)
			result[i] = data[start + i];
		return result;
	}

	private static Node recreateNode(int[] in, int[] pre) {
		if(null == in || null == pre || in.length == 0 || pre.length == 0)
			return null;
		int len = in.length;
		int divider = pre[0];
		int inDivider = 0;
		for(int i = 0; i < len; i++)
			if(divider == in[i])
				inDivider = i;
		Node root = new Node(divider);
		root.left = recreateNode(subarray(in, 0, inDivider - 1), 
			subarray(pre, 1, inDivider));
		root.right = recreateNode(subarray(in, inDivider + 1, len - 1),
			subarray(pre, inDivider + 1, len - 1));
		return root;
	}

	private static Node recreateNode2(int[] in, int[] post) {
		if(null == in || null == post || in.length == 0 ||
			post.length == 0 || in.length != post.length)
				return null;
		int len = post.length;
		int d = post[len - 1];
		int div = 0;
		for(int i = 0; i < len; i ++)
			if(in[i] == d) div = i;
		Node root = new Node(d);
		root.left = recreateNode2(subarray(in, 0, div - 1),
			subarray(post, 0, div - 1));
		root.right = recreateNode2(subarray(in, div + 1, len - 1),
			subarray(post, div, len - 2));
		return root;
	}

	private static void testRecreateNode() {
		int[] pre = {7,10,4,3,1,2,8,11};
		int[] in = {4,10,3,1,7,11,8,2};
		int[] post = {4, 1, 3, 10, 11, 8, 2, 7};
		Node root1 = recreateNode(in, pre);
		Node root2 = recreateNode2(in, post);
		System.out.println("root1");
		preorder(root1);
		inorder(root1);
		System.out.println("root2");
		postorder(root2);
		inorder(root2);
	}
		
	private static int maxMoney(int[] data, int start, int end) {
		if(start == end) return data[start];
		if(start > end) return 0;
		int p1 = data[start] + maxMoney(data, start + 2, end);
		int p2 = data[start] + maxMoney(data, start + 1, end - 1);
		int p3 = data[end] + maxMoney(data, start, end - 2);
		int p4 = data[end] + maxMoney(data, start + 1, end - 1);
		return Math.max(Math.max(p1, p2), Math.max(p3, p4));
	}
	private static int maxMoney(int[] data) {
		int len = data.length;
		int[][] matrix = new int[len][len];
		for(int i = 0; i < len; i++) matrix[i][i] = data[i];
		for(int i = 0; i < len - 1; i++) matrix[i][i + 1] =
			Math.max(data[i], data[i + 1]);

		for(int diff = 2; diff < len; diff ++) {
			for(int i = 0; i + diff < len; i ++) {
				int j = i + diff;
				int p1 = data[i] + matrix[i + 2][j];
				int p2 = data[i] + matrix[i + 1][j - 1];
				int p3 = data[j] + matrix[i][j - 2];
				int p4 = data[j] + matrix[i + 1][j - 1];
				matrix[i][j] = Math.max(Math.max(p1, p2), 
						Math.max(p3, p4));
			}
		}
		return matrix[0][len - 1];
	}
	private static void testMaxMoney() {
		int[] data = { 3, 2, 2, 3, 1, 2 };
		System.out.println(maxMoney(data, 0, data.length - 1));
		System.out.println(maxMoney(data));
	}

	private static int[] slidingWindowMax(int[] data, int ws) {
		int len = data.length;
		int[] res = new int[len - ws + 1];
		LinkedList<Integer> q = new LinkedList<Integer>();
		for(int i = 0; i < ws; i ++){
			q.add(data[i]);
			while(q.peek() < data[i]) q.remove();
		}
		int index = 0;
		for(int end = ws; end < len; end ++) {
			res[index ++] = q.peek();
			while(q.size() >= ws) q.remove();
			q.add(data[end]);
			while(q.peek() < data[end]) q.remove();
		}
		res[index] = q.peek();
		return res;
	}

	private static void testSlidingWin() {
		int[] data = {1, 3, -1, -3, 5, 3, 6, 7};
		for(int i : slidingWindowMax(data, 3)) 
			System.out.println(i);
	}
	
	private static void findKthSmallestTwoSortedArray(int[] a1, int[] a2) {
		int i = 0;
		int j = 0;
		while(i + j != k - 1) {
			int[] a = i == a1.length ? a2 : (a2.length == j ? a1 :
					(a1[i] < a2[j] ? a1 : a2));
			a == a1 ? i ++ : j ++;
		}
		return Math.min(a1[i], a2[j]);
	}	
	private static Iterable<Integer> findIntersectionTwoSortedArray
			(int[] a1, int[] a2) {
		int i = 0;
		int j = 0;
		List<Integer> common = new ArrayList<Integer>();
		while(i < a1.length && j < a2.length) {
			if(a1[i] == a2[j]) {
				list.add(a1[i]);
				i ++;
				j ++;
			} 
			else {
				a1[i] > a2[j] ? j ++ : i ++;
			}
		}	
		return common;
	}
	
	
	private static class Ele implements Comparable<Ele> {
		public int data;
		public int index;
		public Ele(int data, int index) {
			this.data = data;
			this.index = index;
		}
		@Override
		public int compareTo(Ele that) {
			return Integer.compare(this.data, that.data);
		}
	}


	private static int distanceMax(int[] data) {
		List<Ele> list = new ArrayList<Ele>();
		for(int i = 0; i < data.length; i ++)
			list.add(new Ele(data[i], i));
		Collections.sort(list);
		int result = Integer.MIN_VALUE;
		int min = list.get(0).index;
		for(int i = 1; i < list.size(); i ++) {
			int m = list.get(i).index;
			result = m - min > result ? m - min : result;
			min = m < min ? m : min;
		}
		return result;
	}

	private static void testDistance() {
		int[] data = {4, 3, 5, 2, 1, 3, 2, 3};
		System.out.println(distanceMax(data));
	}

	private static String getLongestStringNoDup(String s) {
		HashSet<Character> set = new HashSet<Character>();
		int start = 0;
		int max = Integer.MIN_VALUE;
		int maxStart = 0;
		for(int end = 0; end < s.length(); end ++) {
			if(set.contains(s.charAt(end))) {
				int len = end - start;
				if(len > max) {
					max = len;
					maxStart = start;
				}
				while(s.charAt(start) != s.charAt(end)) start ++;
				start ++;
			} else
				set.add(s.charAt(end));
		}
		return s.substring(maxStart, max);
	}

	private static void testNodup() {
		System.out.println(getLongestStringNoDup("abcabcbb"));
	}


	public static void main(String[] args) {
		testSlidingWin();
	}
}
