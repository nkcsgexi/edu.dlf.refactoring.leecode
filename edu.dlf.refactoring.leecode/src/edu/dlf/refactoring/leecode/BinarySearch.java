import java.util.function.Consumer;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;
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

	private static class ProbeHashtable<T, V> {
		int M;
		int N;
		T[] keys;
		V[] values;
		
		public ProbeHashtable(int M) {
			this.M = M;
			this.keys = (T[]) new Object[M];
			this.values = (V[]) new Object[M];
			this.N = 0;
		}
		
		private class It implements Iterator<Integer> {
			private int start;
			private int current; 
			private boolean begin;
			public It(T k) { 
				this.current = hash(k) % M; 
				this.start = this.current;
				this.begin = false;
			}
			public boolean hasNext() { 
				return !begin || current % M != start; 
			}
			public Integer next() { 
				this.begin = true;
				return this.current ++ % M; 
			} 
		}

		public int size() {
			return N;
		}

		private int hash(T t) {
			return (t.hashCode() & (~(1 << 31))) % M;
		}

		private void resize(int s) {
			ProbeHashtable<T, V> tmp = new ProbeHashtable<T, V>(s);
			for(int i = 0; i < N; i++)
				if(null != keys[i])
					tmp.put(keys[i], values[i]);
			this.M = tmp.M;
			this.N = tmp.N;
			this.keys = tmp.keys;
			this.values = tmp.values;
		}

		public void put(T k, V v) {
			if(N > M/2) resize(M * 2);
			It it = new It(k);
			int i;
			for(i = it.next(); it.hasNext(); i = it.next())
				if(null == keys[i]) 
					break;
			keys[i] = k;
			values[i] = v;
			N ++;
		}

		public V get(T k) {
			It it = new It(k);
			return values[it.next()];
		}

		public void remove(T k) {
			It it = new It(k);
			int i;
			for(i = it.next(); it.hasNext(); i = it.next()) {
				if(keys[i].equals(k)) {
					keys[i] = null;
					values[i] = null;
					it.next();
					break;
				}
			}
			for(;it.hasNext();i = it.next()) {
				if(keys[i] == null) break;
				T rk = keys[i];
				V rv = values[i];
				keys[i] = null;
				values[i] = null;
				N --;
				put(rk, rv);
			}
			N --;
			if(N < M/8)
				resize(M/2);
		}	
		public static void test() {
			ProbeHashtable<Integer, Integer> t = new ProbeHashtable
				<Integer, Integer>(100);
			for(int i =0; i < 100; i++) 
				t.put(i, i);
			System.out.println(t.get(33));
			t.remove(1);
			System.out.println(t.get(1));
		}
	}

	private static class SearchArray<T extends Comparable, V> {
		T[] keys;
		V[] values;
		int N;
		int M;

		public SearchArray(int M) {
			this.keys = (T[]) new Comparable[M];
			this.values = (V[]) new Object[M];
			this.N = 0;
			this.M = M;
		}

		private int searchInternal(int s, int e, T t) {
			if(s >= e) return s;
			int mid = (s + e)/2;
			int c = keys[mid].compareTo(t);
			if(c == 0) return mid;
			if(c > 0) return searchInternal(s, mid -1, t);
			else return searchInternal(mid + 1, e, t);
		}
		private void resize(int s) {
			T[] nks = (T[]) new Comparable[s];
			V[] nvls = (V[]) new Object[s];
			for(int i = 0; i < N; i++) {
				nks[i] = keys[i];
				nvls[i] = values[i];
			}
			keys = nks;
			values = nvls;
			M = s;
		}

		public int rank(T t) {
			return searchInternal(0, N - 1, t);
		}

		public V search(T t) {
			int i = searchInternal(0, N - 1, t);
			if(keys[i].compareTo(t) == 0) return values[i];
			else return null;
		}

		public void put(T t, V v) {
			if(this.N > this.M/2) resize(M * 2);
			int i = searchInternal(0, N - 1, t);
			if(i >= 0 && i < N && keys[i].compareTo(t) == 0) {
				values[i] = v;
				return;
			}
			for(int j = N; j >= i; j --) {
				keys[j + 1] = keys[j];
				values[j + 1] = values[j];
			}
			N ++;
			keys[i] = t;
			values[i] = v;
		}
		public static void test() {
			SearchArray<Integer, Integer> s = new SearchArray<Integer,
				Integer>(10);
			for(int i = 1000; i > 0; i --)
				s.put(i, i);
			for(int i = 2000; i < 3000; i ++)
				s.put(i, i);
			System.out.println(s.search(13));
			System.out.println(s.search(76));
			System.out.println(s.search(-1));
			System.out.println(s.search(0));
			System.out.println(s.search(1000));
			System.out.println(s.search(1001));
			System.out.println(s.rank(2000));	
			System.out.println(s.rank(1100));	
			System.out.println(s.rank(1239));	
			System.out.println(s.rank(2032));
		}
	}




	public static void main(String[] args) {
		SearchArray.test();
	}

}
