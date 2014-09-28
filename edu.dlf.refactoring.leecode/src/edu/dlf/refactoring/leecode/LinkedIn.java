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

	private static Node getTree() {
		Node[] ns = new Node[5];
		for(int i = 0; i < ns.length; i ++)
			ns[i] = new Node(i);
		ns[0].left = ns[1];
		ns[0].right = ns[2];
		ns[1].left = ns[3];
		ns[1].right = ns[4];
		return ns[0];
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
		Node root = getTree();
		traverseLayer(root, (n, l) -> System.out.println(l + ":" + n.value));
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
	
	public static void main(String[] args) {
		System.out.println(canMap("abb", "cdd"));
		System.out.println(getNested("{{1,1},2,{1,1}}"));
		System.out.println(getNested("{1,{4,{6}}}"));
		testLayer();
		testMax();
		testPermutation();
	}
	
}
