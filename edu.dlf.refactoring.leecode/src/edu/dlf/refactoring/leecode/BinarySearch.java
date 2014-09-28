import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.Hashtable;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
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
		private T[] keys;
		private V[] values;
		private int N;
		private int M;

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

		public void remove(T t) {
			int i = searchInternal(0, N - 1, t);
			if(i >= 0 && i < N && 0 == keys[i].compareTo(t)) {
				for(int j = i + 1; j < N; j++) {
					keys[j - 1] = keys[j];
					values[j - 1] = values[j];
				}
				N--;
			}
		}

		public static void test() {
			SearchArray<Integer, Integer> s = new SearchArray<Integer,
				Integer>(10);
			for(int i = 1000; i > 0; i --)
				s.put(i, i);
			for(int i = 2000; i < 3000; i ++)
				s.put(i, i);
			for(int i = 0; i < 1000; i ++)
				s.remove(i);
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

	private static class Sorting {
		
		private static <T extends Comparable> T[] insertion(T[] input) {
			for(int i = 1; i < input.length; i ++) {
				T toInsert = input[i];
				int j;
				for(j = i - 1; j >= 0; j --) {
					if(input[j].compareTo(toInsert) < 0) {
						break;
					}
				}
				j ++;
				for(int k = i; k > j; k --)
					input[k] = input[k - 1];
				input[j] = toInsert;
			}
			return input;
		}

		private static <T extends Comparable> T[] selection(T[] input) {
			for(int i = 0; i < input.length - 1; i ++) {
				T min = input[i];
				int minInd = i;
				for(int j = i + 1; j < input.length; j ++) {
					if(input[j].compareTo(min) < 0) {
						minInd = j;
						min = input[j];
					}
				}
				T t = input[i];
				input[i] = input[minInd];
				input[minInd] = t;
			}
			return input;
		}
	
		private static <T> void swap(T[] input, int i, int j) {
			T t = input[i];
			input[i] = input[j];
			input[j] = t;
		}

		private static <T extends Comparable> void split(T[] input, 
				int s, int e) {
			if(s >= e) return;
			T p = input[e];
			int index = s;
			for(int i = s; i < e; i ++)
				if(input[index].compareTo(p) < 0)
					swap(input, i, index ++);
			swap(input, index, e);
			split(input, s, index - 1);
			split(input, index + 1, e);
		}

		private static <T extends Comparable> T[] qsort(T[] input) {
			split3(input, 0, input.length - 1);
			return input;
		}

		private static <T extends Comparable> void split3(T[] input,
				int l, int h) {
			if(l >= h) return;
			T p = input[l];
			int i = l + 1;
			int high = h;
			int low = l;
			while(i <= high) {
				if(p.compareTo(input[i]) < 0) 
					swap(input, i, high --);
				else if(p.compareTo(input[i]) > 0)
					swap(input, i ++, low ++);
				else i++;
			}
			split3(input, l, low - 1);
			split3(input, high + 1, h);

		}

		private static <T extends Comparable> void merge(T[] input, 
				int s, int m, int e, T max) {
			if(m - s < 1) return;
			if(e - m + 1 < 1) return;
			if(s >= e) return;
			T[] first = (T[]) new Comparable[m - s + 1];
			T[] second = (T[]) new Comparable[e - m + 2];
			first[first.length - 1] = second[second.length - 1] = max;
			for(int i = 0; i < first.length - 1; i++)
				first[i] = input[s + i];
			for(int i = 0; i < second.length - 1; i ++)
				second[i] = input[m + i];
			for(int i = s, fi = 0, si = 0; i <= e; i ++) {
				input[i] = first[fi].compareTo(second[si]) < 0 ?
				       	first[fi++] : second[si ++];
			}
		}
		private static <T extends Comparable> T[] internalMergeSort
				(T[] input, int s, int e) {
			if(s >= e) return input;
			int mid = (s + e)/2;
			internalMergeSort(input, s, mid - 1);
			internalMergeSort(input, mid, e);
			merge(input, s, mid, e, Integer.MAX_VALUE);
			return input;
		}
		private static <T extends Comparable> T[] mergeSort(T[] input) {
			return internalMergeSort(input, 0, input.length - 1);
		}

		private static Integer[] getInput() {
			Integer[] input = new Integer[10];
			for(int i = 0; i < input.length; i ++)
				input[i] = 10 - i;
			return input;
		}
		
		private static void print(Integer[] input) {
			for(int i = 0; i < input.length; i ++)
				System.out.print(input[i] + " ");
			System.out.println();
		}

		public static void test() {
			print(insertion(getInput()));
			print(selection(getInput()));
			print(qsort(getInput()));
		}
	}

	private static class Heap<T extends Comparable> {
		private int N;
		private int M;
		private T[] data;

		public Heap(int M) {
			this.N = 0;
			this.M = M;
			this.data = (T[]) new Comparable[this.M];
		}

		private void swap(int i, int j) {
			T t = data[i];
			data[i] = data[j];
			data[j] = t;
		}

		private void sink(int k) {
			for(int i = k; i * 2 <= this.N;) {
				int left = 2 * i;
				int c = left;
				int right = left + 1;
				if(right <= this.N)
					c = data[left].compareTo(data[right]) < 0 ?
						left : right;
				if(data[c].compareTo(data[i]) > 0) 
					break;
				swap(c, i);
				i = c;
			}	
		}
		private void swim(int k) {
			for(int i = k; i > 1; i /= 2) {
				int parent = i/2;
				if(data[parent].compareTo(data[i]) < 0)
					break;
				swap(parent, i);
			}
		}

		public void add(T t) {
			N ++;
			data[N] = t;
			swim(N);
		}

		public T remove() {
			T re = data[1];
			data[1] = data[N--];
		       	sink(1);
			return re;	
		}
		public static Comparable[] heapSort(Comparable[] data) {
			Heap h = new Heap(data.length * 2);
			for(Comparable c : data) 
				h.add(c);
			for(int i = 0; i < data.length; i ++)
				data[i] = h.remove();
			return data;
		}
		public static void test() {
			Integer[] input = { 2, 1, 8, 9, 6, 5, 0, 3};
			for(Comparable i : heapSort(input))
				System.out.println(i);
		}
	}

	private static class ExpIterator implements Iterator<String> {
		private final char[] ops = {'(', ')', '+', '-', '*', '/'};
		private final String text;
		private int current;
		public ExpIterator(String text) { 
			this.text = text; this.current = 0;
		}
		
		private boolean isOp(char c) {
			for(char o : ops)
				if(o == c) return true;
			return false;
		}

		@Override
		public boolean hasNext() { return current != text.length(); }
		@Override
		public String next() {
			while(Character.isWhitespace(text.charAt(current))) current ++;
			if(isOp(text.charAt(current))) 
				return Character.toString(text.charAt(current ++));
			StringBuilder num = new StringBuilder();
			while(Character.isDigit(text.charAt(current))) {
				num.append(text.charAt(current));
				current ++;
			}
			return num.toString();
		}
	}
			
	private static int expressionEvaluate(String s) {
		Hashtable<String, BiFunction<Integer, Integer, Integer>> 
			funcs = new Hashtable<String, BiFunction<Integer, Integer,
			      Integer>>();
		funcs.put("+", (i, j) -> i + j);
		funcs.put("-", (i, j) -> i - j);
		funcs.put("*", (i, j) -> i * j);
		funcs.put("/", (i, j) -> i / j);
		Stack<Integer> nums = new Stack<Integer>();
		Stack<String> ops = new Stack<String>();
		Iterator<String> it = new ExpIterator(s);
		while(it.hasNext()) {
			String t = it.next();
			if(funcs.keySet().contains(t)) ops.push(t);
			else if(t.equals("("));
			else if(t.equals(")")) {
				int i = nums.pop();
				int j = nums.pop();
				nums.push(funcs.get(ops.pop()).apply(j, i));
			} else{
				nums.push(Integer.parseInt(t));
			}
		}
		return nums.pop();
	}	

	private static void testEvaluation() {
		System.out.println(expressionEvaluate(
					"( 1 + ( ( 2 + 3 ) * ( 4 * 5 ) ) )"));
	}

	private static class UnionFind {
		private final int[] data;
		private int count;	
		public UnionFind(int N) {
			this.data = new int[N];
			this.count = N;
			for(int i = 0; i< data.length; i++)
				this.data[i] = i;
		}

		private int find(int i) {
			List<Integer> list = new ArrayList<Integer>();
			while(data[i] != i) {
				list.add(i); 
				i = data[i];
			}
			for(int j : list) data[j] = i;
			return i;
		}

		public void connect(int i, int j) {
			int ih = find(i);
			int jh = find(j);
			if(ih == jh) return;
			data[ih] = jh;
			count --;
		}
		
		public boolean isConnected(int i, int j) {
			return find(i) == find(j);
		}

		public static void test() {
			int from[] = {4,3,6,9,2,8,5,7,6,1,6};
			int to[] = {3,8,5,4,1,9,0,2,1,0,7};
			UnionFind uf = new UnionFind(10);
			for(int i = 0; i < from.length; i ++)
				uf.connect(from[i], to[i]);
			System.out.println(uf.count);
		}

	}


	public static void main(String[] args) {
		UnionFind.test();
		
	}

}
