import java.util.Queue;
import java.util.List;
import java.util.Hashtable;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.ArrayList;
import java.util.LinkedList;
public class LinkedIn {

	private static class Node {
		public Node left;
		public Node right;
		public final int value;
		public Node(int value) {this.value = value;}
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
		System.out.println(re[0] + ":" + re[1]);
	}

	public static void main(String[] args) {
		System.out.println(canMap("abb", "cdd"));
		System.out.println(getNested("{{1,1},2,{1,1}}"));
		System.out.println(getNested("{1,{4,{6}}}"));
		testLayer();
		testMax();
		testPermutation();
		testLCA();
		testMultiExcept();
		testPalindrome();
		testSearchRange();
	}
	
}
