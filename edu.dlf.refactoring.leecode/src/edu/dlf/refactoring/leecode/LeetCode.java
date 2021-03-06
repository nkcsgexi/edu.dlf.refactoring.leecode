import java.util.List;
import java.util.Arrays;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Stack;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Map.Entry;
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

	private static GNode getGraph() {
		GNode[] nodes = new GNode[5];
		for(int i = 0; i < nodes.length; i ++)
			nodes[i] = new GNode(i);
		nodes[0].neighbors.add(nodes[1]);
		nodes[0].neighbors.add(nodes[2]);
		nodes[1].neighbors.add(nodes[3]);
		nodes[1].neighbors.add(nodes[4]);
		nodes[3].neighbors.add(nodes[2]);
		return nodes[0];	
	}

	private static void cloneTest() {
		GNode r = getGraph();
		GNode nr = cloneGraph2(r);
		r.BFS(n -> System.out.println(n.value));
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
		public void print() {
			for(LinkedNode n = this; n != null; n = n.next) {
				System.out.print(n.value);
			}
			System.out.println();
		}
		public static LinkedNode createList() {
			LinkedNode head = new LinkedNode(0);
			LinkedNode pre = head;
			for(int i = 1; i < 10; i ++) {
				LinkedNode nn = new LinkedNode(i);
				pre.next = nn;
				pre = nn;
			}
			return head;
		}

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
		public Node nextRight;
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
	
	private static int findKthSmallestTwoSortedArray(int[] a1, int[] a2, 
			int k) {
		int i = 0;
		int j = 0;
		while(i + j != k - 1) {
			int[] a = i == a1.length ? a2 : (a2.length == j ? a1 :
					(a1[i] < a2[j] ? a1 : a2));
			int t = a == a1 ? i ++ : j ++;
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
				common.add(a1[i]);
				i ++;
				j ++;
			} 
			else {
				int t = a1[i] > a2[j] ? j ++ : i ++;
			}
		}	
		return common;
	}
	private static void testSortedArray() {
		int[] a1 = {1, 3, 4, 6, 7, 8, 9};
		int[] a2 = {2, 4, 5, 6, 9, 10, 12};
		System.out.println(findKthSmallestTwoSortedArray(a1, a2, 5));
		for(int i : findIntersectionTwoSortedArray(a1, a2)) 
			System.out.println(i);
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

	private static String minConcatenateWords(String[] words) {
		List<String> l = new ArrayList<String>();
		for(String s : words) l.add(s);
		Comparator<String> c = new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return (s1 + s2).compareTo(s2 + s1);
			}
		};
		Collections.sort(l, c);
		StringBuilder sb = new StringBuilder();
		for(String s : l) 
			sb.append(s);
		return sb.toString();
	}

	private static void concatenateWordsTest() {
		String[] words = {"ji", "jibw", "jijibw"};
		System.out.println(minConcatenateWords(words));
	}

	private static boolean squareAdd(int n) {
		Hashtable<Integer, Integer> table = new Hashtable<Integer,
			Integer>();
		int max = 2147483647;
		int maxKey = (int)(Math.sqrt(max));
		for(int k = 0; k <= maxKey; k ++) {
			table.put(k * k, k);
		}
		for(int i = 0; i <= (int)(Math.sqrt(n/2)); i++) {
			int m = n - i * i;
			if(table.contains(m))
				return true;
		}
		return false;
	}

	private static void testSquare() {
		int[] in = {1233, 8833, 10100, 5882353};
		for(int i : in)
			System.out.println(squareAdd(i));
	}

	private static int copyA(int step, int buffer, int count) {
		if(step <= 0) return count;
		int p1 = copyA(step - 1, buffer, count + 1);
		int p2 = copyA(step - 2, count, count);
		int p3 = copyA(step - 1, buffer, buffer + count);
		return Math.max(p1, Math.max(p2, p3));
	}

	private static void testCopy() {
		System.out.println(copyA(12, 0,0));
		System.out.println(copyA(13, 0,0));
		System.out.println(copyA(14, 0,0));
	}

	private static class MinStack {
		private final Stack<Integer> dataS = new Stack<Integer>();
		private final Stack<Integer> minS = new Stack<Integer>();

		public Integer peek() {
			return dataS.peek();
		}
		public void push(int d) {
			int min = minS.size() == 0 ? d : 
				(minS.peek() < d ? minS.peek() : d);
			dataS.push(d);
			minS.push(min);
		}

		public Integer pop() {
			minS.pop();
			return dataS.pop();
		}

		public Integer getMin() {
			return minS.peek();
		}

		public int size() {
			return dataS.size();
		}

		private static void test() {
			MinStack s = new MinStack();
			for(int i = 100; i > 0; i --) 
				s.push(i);
			while(s.size() != 0) {
				System.out.println(s.getMin());
				s.pop();
			}
		}
	}
	private static String getMinSub(String s, String t) {
		HashSet<Character> set = new HashSet<Character>();
		HashMap<Character, Integer> table = new HashMap<Character,
			Integer>();
		for(char c : t.toCharArray()) {
			set.add(c);
		}
		int start = 0;
		int end = 0;
		int min = Integer.MAX_VALUE;
		int minS = 0;
		for(; end < s.length(); end ++) {
			char c = s.charAt(end);
			if(!set.contains(c)) continue;
			if(table.containsKey(c)) {
				table.put(c, table.get(c) + 1);
			} else {
				table.put(c, 1);
				if(table.size() == set.size()) {
					for(;; start++) {
						char cs = s.charAt(start);
						if(!table.containsKey(cs)) continue;
						if(table.get(cs) == 1) break;
						table.put(cs, table.get(cs) - 1);
					}
					if(end - start + 1 < min) {
						min = end - start + 1;
						minS = start;
					}
					table.remove(s.charAt(start));
					start ++;
				}
			}
		}
		return s.substring(minS, minS + min);
	}

	private static void testMin() {
		String s = "ADOBECODEBANC";
		String t = "ABC";
		System.out.println(getMinSub(s, t));
	}
	
	private static int bestStack(int[] data) {
		int[] min = new int[data.length + 1];
		min[0] = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for(int i = 0; i < data.length; i ++) {
			max = data[i] - min[i] > max ? data[i] - min[i] : max;
			min[i + 1] = Math.min(data[i], min[i]);
		}
		return max;
	}

	private static void swap(char[] cs, int i, int j) {
		char c = cs[i];
		cs[i] = cs[j];
		cs[j] = c;
	}

	private static void permutation(char[] s, int i) {
		if(s.length == i + 1) {
			System.out.println(new String(s));
			return;
		}
		for(int j = i; j < s.length; j ++) {
			swap(s, i, j);
			permutation(s, i + 1);
			swap(s, i, j);
		}
	}		
	
	private static int uniquePaths(int i, int j, int m, int n) {
		if(i == m && j == n) return 1;
		if(i > m || j > n) return 0;
		return uniquePaths(i + 1, j, m, n) + uniquePaths(i, j + 1,
			m, n);
	}

	private static int uniquePaths2(int m, int n) {
		int[][] matrix = new int[m][n];
		for(int i = 0; i < n; i ++)
			matrix[m - 1][i] = 1;
		for(int i = 0; i < m; i ++)
			matrix[i][n - 1] = 1;
		for(int i = m - 2; i >= 0; i --)
			for(int j = n - 2; j >= 0; j --)
				matrix[i][j] = matrix[i + 1][j] + 
					matrix[i][j + 1];
		return matrix[0][0];
	}

	
	private static void testUnique() {
		System.out.println(uniquePaths2(3, 7));
	}
	private static void postVisitNoRecursion(Node n, Consumer<Node> consumer) {
		HashSet<Node> black = new HashSet<Node>();
		Stack<Node> s = new Stack<Node>();
		s.push(n);
		while(s.size() != 0) {
			Node p = s.peek();
			boolean finished = true;
			if(p.right != null && !black.contains(p.right)) {
				finished = false;
				s.push(p.right);
			}
			if(p.left != null && !black.contains(p.left)) {
				finished = false;
				s.push(p.left);
			}
			if(finished) {
				s.pop();
				black.add(p);
				consumer.accept(p);
			}
		}
	}

	private static Node createTree() {
		Node[] ns = new Node[5];
		for(int i = 0; i < ns.length; i ++)
			ns[i] = new Node(i);
		ns[0].left = ns[1];
		ns[0].right = ns[2];
		ns[1].left = ns[3];
		ns[1].right = ns[4];
		return ns[0];
	}

	private static void testPost() {
		Node n = createTree();
		postVisitNoRecursion(n, i -> System.out.println(i.value));
	}

	private static int toInt(char c) {
		return c - 'a';
	}

	private static Iterable<Integer> substringMatch(String s, String p) {
		int row = 26;
		int column = p.length() + 1;
		int[][] matrix = new int[row][column];
		for(int i = 0; i < row; i ++) matrix[i][0] = 0;
		matrix[toInt(p.charAt(0))][0] = 1;
		int k = 0;
		for(int i = 1; i < column; i ++) {
			for(int j = 0; j < row; j ++)
				matrix[j][i] = matrix[j][k];
			if(i < p.length()) {
				matrix[toInt(p.charAt(i))][i] = i + 1;
				k = matrix[toInt(p.charAt(i))][k];
			}
		}
		int cs = 0;
		List<Integer> results = new ArrayList<Integer>();
		for(int i = 0; i < s.length(); i ++) {
			cs = matrix[toInt(s.charAt(i))][cs];
			if(cs == column - 1) {
				results.add(i - p.length() + 1);
			}
		}
		return results;
	}
		
	private static void testSubstring() {
		for(int i : substringMatch("hfjdkashfjd", "hfj"))
			System.out.println(i);
	}

	private static int match(char[] s, int ss, char[] t) {
		int len = t.length;
		int matches = 0;
		for(int i = ss; i < s.length; i += len) {
			for(int j = 0; j < len; j ++)
				if(s[j + i] != t[j])
					return matches;
			matches ++;
		}
		return matches;
	}

	private static String replace(char[] s, char[] t, char[] d) {
		StringBuilder sb = new StringBuilder();
		int end = s.length;
		for(int start = 0; start < end; start ++) {
			int m = match(s, start, t);
			if(m == 0) {
				sb.append(s[start]);
				continue;
			}
			sb.append(new String(d));
			start += t.length * m - 1;
		}
		return sb.toString();
	}

	private static void testReplace() {
		String s = "abcdeffdfegabcabc";
		String t = "abc";
		String d = "X";
		System.out.println(replace(s.toCharArray(), t.toCharArray(),
			d.toCharArray()));
	}

	private static int rand7() {return 1;}
	private static int rejectSampling() {
		int num = 0;
		do{
			int i = rand7();
			int j = rand7();
			num = (i - 1) * 7 + j;
		}while(num > 40);
		return (num - 1) % 10 + 1;
	}

	private static boolean searchMatrix(int[][] m, int t) {
		int row = m.length;
		int column = m[0].length;
		for(int i = 0, j = column - 1; i < row && j >= 0;) {
			int v = m[i][j];
			if(v == t) return true;
			if(v > t) i --;
			if(v < t) j ++;
		}
		return false;
	}

	private static String convertExcelNumber(int num) {
		int base = 26;
		HashMap<Integer, Character> map = new HashMap<Integer, Character>();
		char c = 'a';
		for(int i = 0; i < 26; i ++)
			map.put(i, c ++);
		StringBuilder sb = new StringBuilder();
		sb.append(map.get(num % base));
		num /= base;
		while(num != 0) {
			sb.insert(0, map.get((num - 1) % base));
			num = (num - 1) / base;
		}
		return sb.toString();
	}

	private static void testExcel() {
		for(int i = 1; i < 100; i++)
			System.out.println(i + " " + convertExcelNumber(i));
	}

	private static void serializeTree(Node root, List<String> tokens) {
		if(null == root) return;
		tokens.add(Integer.toString(root.value));
		serializeTree(root.left, tokens);
		tokens.add("#");
		serializeTree(root.right, tokens);
	}

	private static Node deserializeTree(Iterator<String> tokens) {
		if(!tokens.hasNext()) return null;
		String t = tokens.next();
		if(t.equals("#")) return null;
		Node n = new Node(Integer.parseInt(t));
		n.left = deserializeTree(tokens);
		n.right = deserializeTree(tokens);
		return n;
	}
		
	private static void testSerialize() {
		Node tree = createTree();
		preorder(tree);
		List<String> tokens = new ArrayList<String>();
		serializeTree(tree, tokens);
		Node t2 = deserializeTree(tokens.iterator());
		preorder(t2);
	}

	private static void visitByLayer(Node root, Consumer<Node> consumer) {
		Queue<Node> currentLayer = new LinkedList<Node>();
		Queue<Node> nextLayer = new LinkedList<Node>();
		currentLayer.add(root);
		do{
			while(currentLayer.size() != 0) {
				Node c = currentLayer.remove();
				consumer.accept(c);
				if(c.left != null) nextLayer.add(c.left);
				if(c.right != null) nextLayer.add(c.right);
			}
			Queue<Node> t = nextLayer;
			nextLayer = currentLayer;
			currentLayer = t;
		}while(currentLayer.size() != 0);
	}
	private static void testLayer() {
		Node root = createTree();
		visitByLayer(root, r -> System.out.println(r.value));
	}

	private static void zigzag(Node root) {
		Stack<Node> current = new Stack<Node>();
		Stack<Node> next = new Stack<Node>();
		current.push(root);
		boolean leftFirst = true;
		while(current.size() != 0) {
			while(current.size() != 0) {
				Node n = current.pop();
				System.out.print(n.value + " ");
				if(leftFirst) {
					if(n.left != null) next.push(n.left);
					if(n.right != null) next.push(n.right);
				} else {
					if(n.right != null) next.push(n.right);
					if(n.left != null) next.push(n.left);
				}
			}
			System.out.println();
			leftFirst = !leftFirst;
			Stack<Node> t = current;
			current = next;
			next = t;
		}
	}

	private static void testZig() {
		Node root = createTree();
		zigzag(root);
	}
	private static void populateNextRight(Node root) {
		if(null == root) return;
		if(root.left != null) root.left = root.right;
		if(root.right != null) root.right = root.nextRight == null ?
			null : root.nextRight.left;
		populateNextRight(root.right);
		populateNextRight(root.left);	
	}

	private static void inorderVisit(Node root, Consumer<Node> consumer) {
		HashSet<Node> set = new HashSet<Node>();
		Stack<Node> s = new Stack<Node>();
		s.push(root);
		while(s.size() != 0) {
			Node n = s.peek();
			if(null == n.left || set.contains(n.left)) {
				consumer.accept(n);
				set.add(s.pop());
				if(null != n.right)
					s.push(n.right);
				continue;
			}
			if(null != n.left) 
				s.push(n.left);
		}
	}
	private static void testInorder() {
		Node root = createTree();
		inorderVisit(root, n -> System.out.println(n.value));
	}
	private static boolean hasLoop(LinkedNode n) {
		LinkedNode fast = n;
		LinkedNode slow = n;
		while(fast != null && fast.next != null) {
			fast = fast.next.next;
			slow = slow.next;
			if(fast == slow) 
				return true;
		}
		return false;
	}

	private static LinkedNode[] splitList(LinkedNode n) {
		LinkedNode fast = n;
		LinkedNode slow = n;
		LinkedNode before = null;
		while(null != fast) {
			before = slow;
			fast = fast.next == null ? fast.next.next : null;
			slow = slow.next;
		}
		before.next = null;
		return new LinkedNode[]{n, slow};
	}

	private static int oneBitCount(int num) {
		int count = 0;
		while(num != 0) {
			if((num & 1) != 0)
				count ++;
			num >>= 1;
		}
		return count;
	}

	private static LinkedNode reverse(LinkedNode node) {
		LinkedNode pre = null;
		LinkedNode cur = node;
		while(cur != null) {
			LinkedNode next = cur.next;
			cur.next = pre;
			pre = cur;
			cur = next;
		}
		return pre;
	}

	private static LinkedNode reverse2(LinkedNode node) {
		if(node == null) return null;
		LinkedNode rest = reverse2(node.next);
		node.next = null;
		LinkedNode n = rest;
		for(; n != null && n.next != null; n = n.next);
		if(n == null) return node;
		n.next = node;
		return rest;
	}

	private static void testList() {
		LinkedNode root = LinkedNode.createList();
		root.print();
		reverse(root).print();
	}

	private static int getDepth(Node node) {
		if(null == node) return 0;
		int max = Math.max(getDepth(node.left), getDepth(node.right));
		max ++;
		return max;
	}

	private static class CharCount implements Comparable<CharCount>{
		public char c;
		public int count;
		public CharCount(char c, int count) {
			this.c = c;
			this.count = count;
		}
		@Override
		public int compareTo(CharCount that) {
			return Integer.compare(that.count, this.count);
		}
	}

	private static char[] rearrange(char[] data, int dis) {
		PriorityQueue<CharCount> queue = new PriorityQueue<CharCount>();
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		for(char c : data) {
			if(map.containsKey(c)) map.put(c, map.get(c) + 1);
			else map.put(c, 1);
		}
		for(Entry<Character, Integer> e : map.entrySet()) 
			queue.add(new CharCount(e.getKey(), e.getValue()));
		int len = data.length;
		char[] results = new char[len];
		boolean[] used = new boolean[len];
	       	for(int i = 0; i < len; i ++) used[i] = false;	
		while(queue.size() != 0) {
			CharCount cc = queue.remove();
			char c = cc.c;
			int count = cc.count;
			int start = 0;
			for(int i = 0; i < count; i++) {
				while(start < len && used[start]) start ++;
				if(start >= len) return null;
				used[start] = true;
				results[start] = c;
				start += dis;
			}
		}
		return results;
	}
	private static void testRearrange() {
		String s = new String(rearrange("abb".toCharArray(), 2));
		System.out.println(s);
	}
	private static void printMatrix(int[][] m) {
		int row = m.length;
		int column = m[0].length;
		for(int offset = 0; ; offset ++) {
			int sr = offset;
			int sc = offset;
			int er = row - 1 - offset;
			int ec = column - 1 - offset;
			if(sr > er || sc > ec) return;
			for(int c = sc; c <= ec; c++) 
				System.out.print(m[sr][c]);
			for(int r = sr + 1; r <= ec; r ++)
				System.out.print(m[r][ec]);
			for(int c = ec - 1; c >= sc; c --) 
				System.out.print(m[er][c]);
			for(int r = er - 1; r > sr; r --)
				System.out.print(m[r][sc]);
			System.out.println();
		}
	}
	private static void testPrint() {
		int[][] m = new int[3][3];
		m[0] = new int[]{1, 2, 3};
		m[1] = new int[]{4, 5, 6};
		m[2] = new int[]{7, 8, 9};
		printMatrix(m);
	}

	private static void findPrime(int N) {
		for(int i = 3; i < N; i++) {
			boolean isPrime = true;
			int fac = (int)Math.sqrt(i);
			for(int j = 2; j <= fac; j ++) {
				if(i % j == 0) {
					isPrime = false;
					break;
				}
			}
			if(isPrime)
				System.out.println(i);
		}
	}		
		
	private static void multiplication(int[] data) {
		int len = data.length;
		int[] before = new int[len];
		int[] after = new int[len];
		before[0] = 1;
		after[len - 1] = 1;
		for(int i = 0; i < len - 1; i ++)
			before[i + 1] = before[i] * data[i];
		for(int i = len - 2; i >= 0; i --)
			after[i] = after[i + 1] * data[i + 1];
		for(int i = 0; i < len; i ++)
			System.out.print(before[i] * after[i] + " ");
	}
	
	private static void testMulti() {
		multiplication(new int[]{4, 3, 2, 1, 2});
	}	

	private static void revert(char[] s, int start, int end) {
		for(int i = start; i <= (start + end) / 2; i ++) {
			int j = end + start - i;
			char c = s[i];
			s[i] = s[j];
			s[j] = c;
		}
	}

	private static String rotate(char[] s, int k) {
		int len = s.length;
		revert(s, 0, len - k - 1);
		revert(s, len - k, len - 1);
		revert(s, 0, len - 1);
		return new String(s);
	}

	private static void testRotate() {
		String s= "abcdefgh";
		System.out.println(rotate(s.toCharArray(), 3));
	}
	private static int searchRotate(int[] data, int t) {
		int s = 0;
		int e = data.length;
		while(s <= e) {
			int mid = (s + e) / 2;
			if(data[mid] == t) return mid;
			if(data[mid] >= data[s]) {
				if(t < data[mid] && t >= data[s])
					e = mid - 1;
				else 
					s = mid + 1;
			} else{
				if(t > data[mid] && t <= data[e]) 
					s = mid + 1;
				else
					e = mid - 1;
			}
		}
		return -1;
	}
	
	private static Iterable<int[]> findTuple(int[] data, int s, int e, int t) {
		List<int[]> results = new ArrayList<int[]>();
		while(s < e) {
			int sum = data[s] + data[e];
			if(sum == t) {
				results.add(new int[]{data[s], data[e]});
				s ++;
				e --;
				continue;
			}
			if(sum < t) s ++;
			else e --;
		}
		return results;
	}

	private static Iterable<int[]> findTriple(int[] data, int t) {
		Arrays.sort(data);
		List<int[]> results = new ArrayList<int[]>();
		for(int i = 0; i < data.length; i ++) {
			int first = data[i];
			int remain = t - first;
			for(int[] pair : findTuple(data, i + 1, data.length - 1,
					remain))
				results.add(new int[]{first, pair[0], pair[1]});
		}
		return results;
	}

	private static void testTriple() {
		int[] d = {-1, 0, 1, 2, -1, -4};
		for(int[] t : findTriple(d, 0)) {
			for(int a : t) 
				System.out.print(a + " ");
			System.out.println();
		}
	}
	public static void main(String[] args) {
		testTriple();
	}
}
