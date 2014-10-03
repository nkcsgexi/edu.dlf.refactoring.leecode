import java.util.Queue;
import java.util.List;
import java.util.Hashtable;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LinkedIn {

	private static class LinkedNode {
		public final int value;
		public LinkedNode next;
		public LinkedNode(int value) {
			this.value = value;
			this.next = null;
		}
	}

	private static class Node {
		public Node left;
		public Node right;
		public final int value;
		public Node(int value) {this.value = value;}
		
		public static Node clone(Node n) {
			if(null == n) return null;
			Node c = new Node(n.value);
			c.left = clone(n.left);
			c.right = clone(n.right);
			return c;
		}

		public static Node clone2(Node root) {
			Node nr = new Node(root.value);
			Stack<Node> os = new Stack<Node>();
			Stack<Node> ns = new Stack<Node>();
			os.push(root);
			ns.push(nr); 
			while(os.size() != 0) {
				Node on = os.pop();
				Node nn = ns.pop();
				if(null != on.left) {
					os.push(on.left);
					nn.left = new Node(on.left.value);
					ns.push(nn.left);
				}
				if(null != on.right) {
					os.push(on.right);
					nn.right = new Node(on.right.value);
					ns.push(nn.right);
				}
			}
			return nr;
		}

		public static boolean equal(Node n1, Node n2) {
			if(null == n1 && null == n2) return true;
			if(null == n1 || null == n2) return false;
			return n1.value == n2.value && equal(n1.left, n2.left) 
				&& equal(n1.right, n2.right);
		}
	

		public static List<LinkedNode> convert2LinkedList(Node root) {
			List<LinkedNode> results = new ArrayList<LinkedNode>();
			LinkedNode current = new LinkedNode(root.value);
			if(root.left == null && root.right == null) {
				results.add(current);
				return results;
			}
			if(root.left != null) 
				results.addAll(convert2LinkedList(root.left));
			if(root.right != null) 
				results.addAll(convert2LinkedList(root.right));
			HashSet<LinkedNode> toAppend = new HashSet<LinkedNode>();
			for(LinkedNode n : results) {
				while(null != n.next) n = n.next;
				toAppend.add(n);
			}
			for(LinkedNode n : toAppend) n.next = current;
			return results;
		}
	}
	private static void testConvert() {
		Node root = getTree()[0];
		List<LinkedNode> list = Node.convert2LinkedList(root);
		for(LinkedNode n : list) {
			while(null != n) { 
				System.out.print(n.value + " ");
				n = n.next;
			}
			System.out.println();
		}
	}
	

	private static class MultiNode {
		List<MultiNode> children;
		int value;

		private static class NodeIt implements Iterator<Integer> {
			
			private Stack<MultiNode> ns;

			public NodeIt(MultiNode root) {
				this.ns = new Stack<MultiNode>();
				for(MultiNode c = root;;) {
					this.ns.push(c);
					if(c.children != null)
						c = c.children.get(0);
					else 
						break;
				}

					
			}

			@Override
			public boolean hasNext() {
				return ns.size() != 0;
			}

			@Override
			public Integer next() {
				int result = ns.pop().value;
				for(MultiNode c = ns.pop(); ns.size() != 0;) {
					MultiNode p = ns.pop();
					int i = p.children.indexOf(c);
					if(i + 1 < p.children.size()) {
						MultiNode n = p.children.get(i + 1);
						this.ns.push(p);
						for(;n != null; n = getFirstChild(n))
							this.ns.push(n);
						break;
					}
					c = p;
				}
				return result;
			}

			private MultiNode getFirstChild(MultiNode n) {
				return n.children == null ? null : n.children.get(0);
			}
		}
	}

	private static int wordDistance(String[] words, String w1, String w2) {
		Hashtable<String, List<Integer>> table = new Hashtable<String, 
			List<Integer>>();
		for(int i = 0; i < words.length; i++) {
			if(table.containsKey(words[i])) {
				table.get(words[i]).add(i);
			} else {
				List<Integer> l = new ArrayList<Integer>();
				l.add(i);
				table.put(words[i], l);
			}
		}
		List<Integer> l1 = table.get(w1);
		List<Integer> l2 = table.get(w2);
		int min = Integer.MAX_VALUE;
		for(int i1 : l1)
			for(int i2 :l2)
				min = min < Math.abs(i1 - i2) ? min :
					Math.abs(i1 - i2);
		return min;
	}

	private static void addWord(Hashtable<String, List<Integer>> table,
			String word, int i) {
		if(table.contains(word)) {
			table.get(word).add(i);
		} else {
			List<Integer> l = new ArrayList<Integer>();
			l.add(i);
			table.put(word, l);
		}
	}

	private static void removeWord(Hashtable<String, List<Integer>> table,
			String word) {
		if(table.keySet().contains(word)) {
			List<Integer> l = table.get(word);
			if(l.size() > 1) l.remove(0);
			else table.remove(word);
		}
	}
			
	private static int[] getWordSet(String[] words) {
		Hashtable<String, List<Integer>> table = new Hashtable<String,
			List<Integer>>();
		for(int i =0; i < words.length; i ++) 
			addWord(table, words[i], i);
		Hashtable<String, List<Integer>> ct = new Hashtable<String,
			List<Integer>>();
		int min = Integer.MAX_VALUE;
		int ms = 0;
		int me = 0;
		for(int end = 0, start = 0; end < words.length; end ++) {
			boolean found = false;
			addWord(ct, words[end], end);	
			while(ct.keySet().size() == table.keySet().size()) {
				found = true;
				removeWord(ct, words[start ++]);
			}
			if(found && end - start < min) {
				ms = start - 1;
				me = end;
				min = end - start;
			}	
		}
		return new int[] {ms, me};
	}
	

	private static Node[] getTree() {
		Node[] ns = new Node[5];
		for(int i = 0; i < ns.length; i ++)
			ns[i] = new Node(i);
		ns[0].left = ns[1];
		ns[0].right = ns[2];
		ns[1].left = ns[3];
		ns[1].right = ns[4];
		return ns;
	}

	private static void checkCloneTree() {
		Node root = getTree()[0];
		Node cr = Node.clone(root);
		Node cr2 = Node.clone2(root);
		System.out.println(Node.equal(root, cr));
		System.out.println(Node.equal(root, cr2));
	}

	private static void traverseLayer(Node root, BiConsumer<Node, Integer> c) {
		Queue<Node> q = new LinkedList<Node>();
		q.add(root);
		q.add(null);
		int layer = 1;
		while(q.size() > 0) {
			Node n = q.remove();
			if(null == n) {
				layer ++;
				if(q.size() != 0)
					q.add(null);
				continue;
			}
			if(null != n.left) q.add(n.left);
			if(null != n.right) q.add(n.right);
			c.accept(n, layer);
		}
	}

	private static void testLayer() {
		Node root = getTree()[0];
		traverseLayer(root, (n, l) -> System.out.println(l + ":" + n.value));
	}

	private static Node lowestCommonAncestor(Node root, Node a, Node b) {
		if(root == a || root == b || root == null) return root;
		Node l = lowestCommonAncestor(root.left, a, b);
		Node r = lowestCommonAncestor(root.right, a, b);
		return l == null ? r : r == null ? l : root;
	}

	private static void testLCA() {
		Node[] ns = getTree();
		System.out.println(lowestCommonAncestor(ns[0], ns[3], ns[4]).value);
	}
	
	private static int getNested(String s) {
		List<Integer> nums = new ArrayList<Integer>();
		List<Integer> nests = new ArrayList<Integer>();
		int current = 0;
		boolean inNum = false;
		List<Integer> digits = new LinkedList<Integer>();
		for(char c : s.toCharArray()) {
			if(Character.isDigit(c)) {
				digits.add(0, c - '0');
				inNum = true;
			} else {
				if(inNum) {
					int num = 0;
					int base = 1;
					for(int d : digits) {
						num += d * base;
						base *= 10;
					}
					nums.add(num);
					nests.add(current);
					digits.clear();
				}
				if (c== '{') current ++;
				else if( c== '}') current --;
				inNum = false;
			}
		}
		int result = 0;
		for(int i = 0; i < nums.size(); i ++) {
			result += nums.get(i) * nests.get(i);
		}
		return result;
	}

	private static boolean oneWayMap(String a, String b) {
		Hashtable<Character, Character> table = new
			Hashtable<Character, Character>();
		for(int i = 0; i < a.length(); i++) {
			char ca = a.charAt(i);
			char cb = b.charAt(i);
			if(table.keySet().contains(ca)) {
				if(table.get(ca) != cb)
					return false;
			}else
				table.put(ca, cb);
		}
		return true;
	}

	private static boolean canMap(String a, String b) {
		if(null == a || null == b) return false;
		if(a.length() != b.length()) return false;
		return oneWayMap(a, b) && oneWayMap(b, a);
	}

	private static int getMaxAddition(int[] data) {
		int start = 0;
		int max = Integer.MIN_VALUE;
		for(int end = 0, sum = 0; end < data.length; end++) {
			sum += data[end];
			if(sum > max) max = sum;
			if(sum < 0) sum = 0;
		}
		return max;
	}

	private static void testMax() {
		int[] data = {-2,1,-3,4,-1,2,1,-5,4};
		System.out.println(getMaxAddition(data));
		System.out.println(getMaxMul(data));
	}
	
	private static int getMaxMul(int[] data) {
		int[] min = new int[data.length];
		int[] max = new int[data.length];
		min[0] = max[0] = data[0];
		int global = Integer.MIN_VALUE;
		for(int i = 1; i < data.length; i++) {
			int one = min[i - 1] * data[i];
			int two = max[i - 1] * data[i];
			int three = data[i];
			min[i] = Math.min(one, Math.min(two, three));
			max[i] = Math.max(one, Math.max(two, three));
			global = max[i] > global ? max[i] : global;
		}
		return global;
	}

	private static void swap(char[] data, int i, int j) {
		char t = data[i];
		data[i] = data[j];
		data[j] = t;
	}

	private static void permutation(char[] alph, int pos, 
			List<String> result) {
		if(pos == alph.length) {
			result.add(new String(alph));
			return;
		}
		for(int i = pos; i < alph.length; i++) {
			swap(alph, pos, i);
			permutation(alph, pos + 1, result);
			swap(alph, pos, i);
		}
	}

	private static void testPermutation() {
		List<String> result = new ArrayList<String>();
		permutation("abcde".toCharArray(), 0, result);
		int i = 1;
		for(String s : result) System.out.println(s + ":" + i ++);
	}

	private static int[] getMultipleExcept(int[] data) {
		int[] front = new int[data.length];
		int[] back = new int[data.length];
		front[0] = 1;
		back[data.length - 1] = 1;
		for(int i = 1; i < data.length; i++) 
			front[i] = data[i - 1] * front[i - 1];
		for(int i = data.length - 2; i >= 0; i --)
		       	back[i]	= back[i + 1] * data[i + 1];
		for(int i = 0; i < data.length; i ++)
			data[i] = front[i] * back[i];
		return data;
	}

	private static void testMultiExcept() {
		int[] data = {10, 2, 5};
		for(int d : getMultipleExcept(data))
			System.out.println(d);
	}

	private static boolean checkBound(int len, int i) {
		return i >=0 && i < len;
	}
	private static String longestPalindrome(String s) {
		char[] data = new char[s.length() * 2 + 1];
		for(int i = 0; i < s.length(); i++) {
			data[2 * i] = '#';
			data[2 * i + 1] = s.charAt(i);
		}
		data[s.length() * 2] = '#';
		int[] id = new int[data.length];
		int md = 0;
		int mx = 0;
		for(int i = 0; i < data.length; i++) {
			id[i] = mx > i ? Math.min(id[2 * md - i], mx - i) : 1;
			while(checkBound(data.length, id[i] + i) && 
				checkBound(data.length, i - id[i]) &&
				data[id[i] + i] == data[i - id[i]]) 
					id[i] ++;
			if(id[i] + i > mx) {
				md = i;
				mx = id[i] + i;
			}
		}
		mx = 0;
		md = 0;
		for(int i = 0; i < id.length; i++) {
			if(id[i] > mx) {
				mx = id[i];
				md = i;
			}
		}
		StringBuilder sb = new StringBuilder();
		for(int i = md - id[md] + 1; i < md + id[md]; i++) 
			if(data[i] != '#') sb.append(data[i]);
		return sb.toString();
	}
	
	private static void testPalindrome() {
		System.out.println(longestPalindrome("baaba"));
	}

	
	private static int[] searchRange(int[] data, int t) {
		int[] result = new int[2];
		int low = 0; 
		int high = data.length - 1;
		while(low < high) {
			int mid = (low + high) / 2;
			if(data[mid] < t) 
				low = mid + 1;
			else 
				high = mid;
		}
		result[0] = low;
		high = data.length - 1;
		while(low < high) {
			int mid = (low + high) / 2;
			if(data[mid] > t) 
				high = mid;
			else 
				low = mid + 1;
		}
		result[1] = low - 1;
		return result;
	}

	private static void testSearchRange() {
		int[] data = new int[] {2, 3, 3, 3, 10};
		int[] re = searchRange(data, 3);
		System.out.println(strictlySmaller(data, 4) + ":" + 
				strictlyLarger(data, 4));
	}

	private static int strictlyLarger(int[] data, int t) {
		int low = 0;
		int high = data.length - 1;
		while(low < high) {
			int mid = (low + high) / 2;
			if(data[mid] < t) 
				low = mid + 1;
			else 
				high = mid;
		}
		return low;
	}
	private static int strictlySmaller(int[] data, int t) {
		int low = 0;
		int high = data.length - 1;
		while(low < high) {
			int mid = (low + high) / 2;
			if(data[mid] > t) 
				high = mid;
			else
				low = mid + 1;
		}
		return low - 1;
	}
	
	private static class BlockQueue {
		private final Queue<Integer> q = new LinkedList<Integer>();

		public synchronized boolean add(Integer i) {
			q.add(i);
			notify();
			return true;
		}

		public synchronized Integer remove() throws InterruptedException{
			while(q.size() == 0) {
				wait();
			}
			return q.remove();
		}

		public Integer peek() { return q.peek(); }
	}

	private static void coin(int[] options, List<Integer> counts, 
			int amount) {
		if(amount == 0) {
			for(int i : counts) 
				System.out.print(i + " ");
			System.out.println();
			return;
		}
		if(counts.size() == options.length) return;	
		int op = options[counts.size()];
		for (int i = 0; i * op <= amount; i ++) {
			counts.add(i);
			coin(options, counts, amount - op * i);
			counts.remove(counts.size() - 1);
		}

	}

	private static Iterable<String> regular(String text) {
		HashSet<String> set = new HashSet<String>();
		String dot = Pattern.quote(".");
		String noDot = "[^" + dot + "]"; 
		StringBuilder sb = new StringBuilder();
		sb.append(noDot);
		sb.append("(\\d+" + dot);
		sb.append("\\d+" + dot);
		sb.append("\\d+" + dot);
		sb.append("\\d+)");
		sb.append(noDot);
		Pattern pattern = Pattern.compile(sb.toString());
		Matcher m = pattern.matcher(text);
		while(m.find()) {
			String add = m.group(1);
			String[] nums = add.split("\\.");
			boolean isOk = true;
			for(String n : nums) {
				try{
					int num = Integer.parseInt(n);
					if(num < 0 || num > 255) isOk = false;
				} catch(Exception e) {
					isOk = false;
				}
				if(!isOk) break;
			}
			if(isOk) set.add(add);
		}
		return set;
	}

	private static void testRegular() {
		String s = "fadasfd10.10.10000.10fd";
		System.out.println(regular(s).iterator().hasNext());
	}

	private static void testCoin() {
		coin(new int[] {1, 5, 10, 20}, new ArrayList<Integer>(), 100);
	}

	private static int getRandom(int min, int max) {
		int d = (int)(Math.random() * (max - min + 1));
		return min + d;
	}

	private static int[] shuffle(int[] data) {
		for(int i = 0; i < data.length; i++) {
			int j = getRandom(i, data.length - 1);
			int t = data[i];
			data[i] = data[j];
			data[j] = t;
		}
		return data;
	}

	private static void testShuffle() {
		int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		for(int i : shuffle(data)) {
			System.out.print(i + " ");
		}
		System.out.println();
	}

	private static int minMaxStorageProblem(int[] volumns, int machine) {
		int[][] matrix = new int[machine][volumns.length];
		matrix[0][0] = volumns[0];
		for(int i = 1; i < volumns.length; i++)
			matrix[0][i] = matrix[0][i - 1] + volumns[i];
		for(int m = 1; m < machine; m++) {
			for(int i = 0; i < volumns.length; i ++) {
				int min = Integer.MAX_VALUE;
				for(int j = 0; j <= i; j ++) {
					int current = 0;
					for(int  k = j + 1; k <= i; k++)
						current += volumns[k];
					current = Math.max(current, 
							matrix[m - 1][j]);
					min = current < min ? current : min;
				}
				matrix[m][i] = min;
			}
		}
		return matrix[machine - 1][volumns.length - 1];
	}
	private static boolean ifVolumnOk(int[] v, int m, int capacity) {
		int needed = 1;
		int current = 0;
		for(int i : v) {
			if(current + i > capacity) {
				needed ++;
				current = i;
			} else 
				current += i;
			if(needed > m) 
				return false;
		}
		return true;
	}
	private static int storageProblem2(int[] v, int m) {
		int total = 0;
		for(int i : v) total += i;
		int capacity = total;
		for(; ifVolumnOk(v, m, capacity); capacity--);
		return capacity + 1;
		
	}
	private static void testStorage() {
		int[] data = new int[]{3, 5, 3, 4, 6, 7, 8, 1};
		int m = 4;
		System.out.println(minMaxStorageProblem(data, m));
		System.out.println(storageProblem2(data, m));
	}

	public static void main(String[] args) {
		testStorage();
	}
	
}
