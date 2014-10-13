import java.util.List;
import java.util.HashMap;
import java.util.Stack;
public class LGraph{

	private static class GNode {
		public int value;
		public List<GNode> neighbors = new ArrayList<GNode>();
		public GNode(int value) {this.value = value;}
	}

	private static class Node {
		public Node left;
		public Node right;
		public int value;
		public Node nextRight;
		public Node parent;
		public Node(int value) {this.value = value;}
	}

	private static GNode cloneGraph(GNode node) {
		GNode root = new GNode(node.value);
		HashMap<GNode, GNode> map = new HashMap<GNode, GNode>();
		map.put(node, root);
		Stack<GNode> s = new Stack<GNode>();
		s.push(node);
		while(s.size() != 0) {
			GNode n = s.pop();
			GNode nn = map.get(n);
			for(GNode nei : n.neighbors) {
				if(map.containsKey(nei)) {
					nn.neighbors.add(map.get(nei));
				} else{
					GNode nnei = new GNode(nei.value);
					nn.neighbors.add(nnei);
					map.put(nei, nnei);
					s.push(nei);
				}
			}
		}
		return root;
	}

	private static boolean match(String t, String p) {
		if(t.equals("") && p.equals("")) return true;
		if(t.equals("") || p.equals("")) return false;
		char c = p.charAt(0);
		if(t.length() > 1) {
			String np = p.substring(2);
			if(t.charAt(1) == '+') 
				return t.charAt(0) == p.charAt(0) && 
					match(t.substring(1), c + "*" + np);
			if(t.charAt(1) == '*') {
				while(true) {
					if(match(t, np)) return true;
					if(t.length() > 0 && t.charAt(0) == c)
						t = t.substring(1);
					else
						break;
				}
				return false;
			}
		}
		return t.charAt(0) == p.charAt(0) && match(t.substring(1), 
				p.substring(1));
	}

	private static int reverseBit(int num) {
		num = ((num & 0x55555555) << 1) | ((num & 0xaaaaaaaa) >> 1);
		num = ((num & 0x33333333) << 2) | ((num & 0xcccccccc) >> 2);
		num = ((num & 0xf0f0f0f0) >> 4) | ((num & 0x0f0f0f0f) << 4);
		num = ((num & 0xff00ff00) >> 8) | ((num & 0x00ff00ff) << 8);
		num = ((num & 0xffff0000) >> 16) | ((num & 0x0000ffff) << 16);
		return num;
	}
	private static Node LCA(Node a, Node b, Node root) {
		if(null == root) return null;
		if(root == a || root == b) return root;
		Node l = LCA(a, b, root.left);
		Node r = LCA(a, b, root.right);
		return l == null ? r : (r == null ? l : root);
	}
	private static int height(Node n) {
		if(null == n) return 1;
		return Math.max(height(n.left), height(n.right)) + 1;
	}

	private static Node LCA2(Node a, Node b) {
		int h1 = height(a);
		int h2 = height(b);
		if(h1 > h2) {
			int t = h1;
			h1 = h2;
			h2 = t;
			Node tn = a;
			a = b;
			b = tn;
		}
		for(int i = h2 - h1; i > 0; i --) 
			b = b.parent;
		while(a != b) {
			a = a.parent;
			b = b.parent;
		}
		return a;
	}

	private static class Ele implements Comparable<Ele>{
		public int d;
		public int i;
		public Ele(int d, int i) {this.d = d; this.i = i;}
		@Override
		public int compareTo(ELe that) {
			return Integer.compare(this.d, that.d);
		}
	}
	private int maxDistance(int[] data) {
		int len = data.length;
		Ele[] elements = new Ele[len];
		for(int i = 0; i < len; i ++)
			elements[i] = new Ele(data[i], i);
		Arrays.sort(elements);
		int min = elements[0].i;
		int max = Integer.MIN_VALUE;
		for(int i = 1; i < len; i ++) {
			Ele e = elements[i];
			max = e.i - min > max ? e.i - min : max;
			min = e.i < min ? e.i : min;
		}
		return max;
	}
			
		

			

	public static void main(String[] args) {
	}
}
