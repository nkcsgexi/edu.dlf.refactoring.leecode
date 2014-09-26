import java.util.function.Consumer;
import java.util.Queue;
import java.util.LinkedList;
public class BinarySearch{

	private static class BinaryNode<T extends BinaryNode, V> {
		public T left;
		public T right;
		public int key;
		public V value;
		public BinaryNode(int key, V value) {
			this.key = key;
			this.value = value;
		}

		public void inorderVisit(Consumer<BinaryNode> c) {
			inorderInternal(this, c);
		}

		public int size() {
			return internalSize(this);
		}
		public int depth() {
			return internalDepth(this);	
		}

		public V search(int k) {
			BinaryNode current = this;
			while(null != current) {
				int c = current.key - k;
				if(c == 0) return (V) current.value;
				else if(c > 0) current = current.left;
				else current = current.right;
			}
			return null;
		}
	
		private int internalDepth(BinaryNode n) {
			if(null == n) return -1;
			return Math.max(internalDepth(n.left), internalDepth(n.
						right)) + 1;
		}
		private int internalSize(BinaryNode n) {
			if(null == n) return 0;
			return 1 + internalSize(n.left) + internalSize(n.right);
		}

		private void inorderInternal(BinaryNode t, Consumer
				<BinaryNode> c) {
			if(null == t) return;
			inorderInternal(t.left, c);
			c.accept(t);
			inorderInternal(t.right, c);
		}
	}
	public static class BinarySearchTree{
		private static class Node extends BinaryNode<Node, Integer> {
			public Node(int key, int value) {
				super(key, value);
			}
		}
		private Node root;
		
		public void put(int k, int v) {
			this.root = internalPut(this.root, k, v);	
		}

		public int select(int s) {
			return internalSelect(root, s).key;
		}

		private Node internalSelect(Node n, int s) {
			if(null == n) return n;
			int current = size(n.left);
			if(s == current) return n;
			else if(s > current) 
				return internalSelect(n.right, s - current - 1);
			else 
				return internalSelect(n.left, s);

		}

		public int rank(int k) {
			return internalRank(root, k);

		}
		private int internalRank(Node n, int k) {
			if(null == n) return 0;
			if(n.key >= k) return internalRank(n.left, k);
			else return size(n.left) + internalRank(n.right, k) + 1;
		}	

		private int size(Node n) {
			if(null == n) return 0;
			return n.size();
		}
		private Node internalPut(Node n, int k, int v) {
			if(null == n) return new Node(k, v);
			if(n.key > k) n.left = internalPut(n.left, k, v);
			else if(n.key < k) n.right = internalPut(n.right, k, v);
			else n.value = v;
			return n;
		}

		public void delete(int k) {
			this.root = internalDelete(this.root, k);
		}
		private Node internalDelete(Node n, int k) {
			if(null == n) return n;
			if(n.key > k)
			       	n.left = internalDelete(n.left, k);
			else if (n.key < k) 
				n.right = internalDelete(n.right, k);
			else {
				if(null == n.left) return n.right;
				if(null == n.right) return n.left;
				Node m = min(n.right);
				m.left = n.left;
				m.right = deleteMin(n.right);
				n = m;
			}
			return n;
		}

		private Node deleteMin(Node n) {
			if(n.left == null) return n.right;
			n.left = deleteMin(n.left);
			return n;
		}

		private Node min(Node n) {
			while(null != n && null != n.left) n = n.left;
			return n;
		}

		public Iterable<Integer> collectRange(int min, int max) {
			Queue<Integer> q = new LinkedList<Integer>();
			collectInternal(this.root, min, max, q);
			return q;
		}
		private void collectInternal(Node n, int min, int max,
				Queue<Integer> q) {
			if(null == n) return;
			boolean left = true;
			boolean right = true;
			if(n.key >= min || n.key <=max) {
				q.add(n.key);
			}
			if(n.key <= min) left = false;
			if(n.key >= max) right = false;
			if(left) collectInternal(n.left, min, max, q);
			if(right) collectInternal(n.right, min, max, q);
		}

		public static void test() {
			BinarySearchTree t = new BinarySearchTree();
			for(int i = 0; i < 1000; i++)
				t.put(i, i);
			System.out.println(t.root.depth());
			System.out.println(t.root.search(322));
			System.out.println(t.rank(322));
			t.delete(322);
			System.out.println(t.root.search(322));
			System.out.println(t.root.search(311));
		}
	}



	public static class RedBlackTree {
		public static enum Color{
			Red, Black
		}
		private static class Node extends BinaryNode<Node, Integer>{
			public Color color;
			public Node(int key, int value) {
				super(key, value);
				this.color = Color.Red;
			}
		}

		private static Node rotateLeft(Node n) {
			Node right = n.right;
			n.right = right.left;
			right.left = n;
			right.color = n.color;
			n.color = Color.Red;
			return right;
		}

		private static Node rotateRight(Node n) {
			Node left = n.left;
			n.left = left.right;
			left.right = n;
			left.color = n.color;
			n.color = Color.Red;
			return left;
		}
		private static boolean isRed(Node n) {
			if(null == n) return false;
			return n.color == Color.Red;
		}
		private static void flipColors(Node n) {
			Color t = n.left.color;
			n.left.color = n.right.color = n.color;
			n.color = t;
		}

		private static Node insert(Node n, int key, int value) {
			if(null == n)
				return new Node(key, value);
			if(n.key < key)
				n.right = insert(n.right, key, value);
			else if(n.key > key)
				n.left = insert(n.left, key, value);
			else 
				n.value = value;
			if(isRed(n.right) && !isRed(n.left)) n = rotateLeft(n);
			if(isRed(n.left) && isRed(n.left.left)) n = rotateRight(n);
			if(isRed(n.left) && isRed(n.right)) flipColors(n);
			return n;
		}

		private Node root;
		public void insert(int key, int value) {
			this.root = insert(root, key, value);
			this.root.color = Color.Black;
		}
		public static void test() {
			RedBlackTree t = new RedBlackTree();
			for(int i =0; i < 1000; i ++)
				t.insert(i,i);
			System.out.println(t.root.search(322));
		}
	}
	public static void main(String[] args) {
		BinarySearchTree.test();
	}

}
