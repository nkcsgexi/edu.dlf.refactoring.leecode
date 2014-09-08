import java.io.BufferedReader; 
import java.io.FileReader; 
import java.util.ArrayList; 
import java.util.Collections; 
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.Collections;
import java.util.function.*;

class Main {

	private static boolean isPow2(int num) {
		return 0 == (num & (num - 1));
	}

	private static void testPow2() {
		for(int i = 1; i < 256; i ++) {
			if(isPow2(i)) {
				System.out.println(i);
			}
		}
	}

	private static String decodeInteger(int value) {
		int digit = 1;
		int base = 26;
		int preValue = value;
		while(value > 0) {
			preValue = value;
			value -= base;
			base *= 26;
		}
		preValue --;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < digit; i ++, preValue /= 26) {
			char d = (char)('a' + (preValue % 26));
			sb.append(preValue == 0 ? 'a' : d);
		}
		return sb.reverse().toString();

	}

	private static int balancedIndex(int[] input) {
		int sum = 0;
		for(int i = 0; i < input.length; i++) sum += input[i];
		int left = 0;
		for(int i = 1; i < input.length - 1; i++) {
			left += input[i - 1];
			int right = sum - left - input[i];
			int leftCount = i;
			int rightCount = input.length - leftCount - 1;
			if(left * rightCount == right * leftCount) 
				return i;
		}
		return -1;
	}



	private static void testBalancedIndex() {
		int input[] = {-7, 1, 5, 2, -4, 3, 0};
		System.out.println(balancedIndex(input));
	}

	private static class BinaryTreeNode{
		public int value;
		public BinaryTreeNode left;
		public BinaryTreeNode right;

		public BinaryTreeNode(int value) {
			this.value = value;
			this.left = null;
			this.right = null;
		}
		
		public void InorderVisit(Consumer<BinaryTreeNode> func) {
			if(null != left) left.InorderVisit(func);
			func.accept(this);
			if(null != right) right.InorderVisit(func);
		}
		
		public void PostorderVisit(Consumer<BinaryTreeNode> func) {
			if(null != left) left.PostorderVisit(func);
			if(null != right) right.PostorderVisit(func);
			func.accept(this);
		}
		public void PreorderVisit(Consumer<BinaryTreeNode> func) {
			func.accept(this);
			if(null != left) left.PreorderVisit(func);
			if(null != right) right.PreorderVisit(func);
		}
		public BinaryTreeNode rotateLeft() {
			if(null == right) return this;
			BinaryTreeNode originalRight = right;
			right = originalRight.left;
			originalRight.left = this;
			return originalRight;
		}

		public BinaryTreeNode rotateRight() {
			if(null == left) return this;
			BinaryTreeNode originalLeft = left;
			left = left.right;
			originalLeft.right = this;
			return originalLeft;
		}

		public boolean isBinarySearchTree() {
			final List<Integer> values = new ArrayList<Integer>();
			int pre = Integer.MIN_VALUE;
			InorderVisit(n -> {
				values.add(n.value);
			});
			for(int i : values) {
				if(i < pre) return false;
				pre = i;
			}
			return true;
		}

		public boolean isSubTree(final BinaryTreeNode root) {
			List<Integer> box = new ArrayList<Integer>();
			InorderVisit(n -> {if(isTreeSame(n, root)) box.add(1);});
			return box.size() != 0;
		}

		public static boolean isTreeSame(BinaryTreeNode root1, 
				BinaryTreeNode root2) {
			if(root1 == null && root2 == null) return true;
			if(root1 == null || root2 == null) return false;
			if(root1.value != root2.value) return false;
			return isTreeSame(root1.left, root2.left) && 
				isTreeSame(root1.right, root2.right);
		}
			
	}

	

	private static BinaryTreeNode createTreeHelper(int[] input, int start, 
			int end) {
		if(start > end) return null;
		if(start == end) return new BinaryTreeNode(input[start]);
		int middle = (start + end) / 2;
		BinaryTreeNode left = createTreeHelper(input, start, middle -1);
		BinaryTreeNode right = createTreeHelper(input, middle + 1, end);
		BinaryTreeNode mid = new BinaryTreeNode(middle);
		mid.left = left;
		mid.right = right;
		return mid;
	}

	private static BinaryTreeNode createTree() {
		int[] input = new int[100];
		for(int i = 0; i < 100; i ++) input[i] = i + 1;
		return createTreeHelper(input, 0, input.length - 1);
	}	
	
	private static int[] findAbnormal(int[] num) {
		List<Integer> abs = new ArrayList<Integer>();
		for(int i = 1; i < num.length; i++) 
			if(num[i] < num[i - 1]) abs.add(i);
		if(abs.size() == 1)
			return new int[] {abs.get(0) - 1, abs.get(0)};
		else
			return new int[] {abs.get(0) - 1, abs.get(1)};
	}


	private static void swapAbnormalBinaryTreeNode(BinaryTreeNode root) {
		List<BinaryTreeNode> nodes = new ArrayList<BinaryTreeNode>();
		root.InorderVisit(n -> nodes.add(n));
		int[] values = new int[nodes.size()];
		for(int i = 0; i < values.length; i ++)
			values[i] = nodes.get(i).value;
		int[] swaps = findAbnormal(values);
		BinaryTreeNode n1 = nodes.get(swaps[0]);
		BinaryTreeNode n2 = nodes.get(swaps[1]);
		int temp = n1.value;
		n1.value = n2.value;
		n2.value = temp;
	}

	private static List<Integer> cloneList(List<Integer> list) {
		List<Integer> result = new ArrayList<Integer>();
		result.addAll(list);
		return result;
	}

	private static void printPathsInBinarySearchTree(BinaryTreeNode root, 
			int sum, int originalSum, List<Integer> prefix) {
		if(root == null)
			return;
		if(originalSum == sum) {
			for(int i = 0; i < prefix.size(); i ++)
				System.out.print(prefix.get(i) + " ");
			System.out.println();
		}
		sum += root.value;
		List<Integer> newPre = cloneList(prefix);
		newPre.add(root.value);
		printPathsInBinarySearchTree(root.left, sum, originalSum, newPre);
		printPathsInBinarySearchTree(root.right, sum, originalSum, newPre);
		printPathsInBinarySearchTree(root.left, 0, originalSum, new 
			ArrayList<Integer>());
		printPathsInBinarySearchTree(root.right, 0, originalSum, new 
			ArrayList<Integer>());
	}
		
	private static void testPrintAllPaths() {
		BinaryTreeNode root = createTree();
		printPathsInBinarySearchTree(root, 0, 45, new ArrayList<Integer>());
	}
	
	private static List<Integer> compressString(String s) {
		List<Integer> results = new ArrayList<Integer>();
		HashMap<String, Integer> dic = new HashMap<String, Integer>();
		for(char c = 'a'; c <= 'z'; c++)
			dic.put(Character.toString(c), (int)(c - 'a' + 1));
		int nextValue = 27;
		int start = 0;
		for(int end = 0; end < s.length(); end ++) {
			String sub = s.substring(start, end + 1);
			if(dic.containsKey(sub))
				continue;
			int code = dic.get(sub.substring(0, sub.length() - 1));
			results.add(code);
			dic.put(sub, nextValue ++);
			start = end;
		}
		results.add(dic.get(s.substring(start)));
		return results;
	}

	private static String decompressString(List<Integer> codes) {
		StringBuilder result = new StringBuilder();
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		for(char c = 'a'; c <= 'z'; c ++) 
			map.put((int)(c - 'a' + 1), Character.toString(c));
		int nextValue = 27;
		String prefix = null;
		for(int i = 0; i < codes.size(); i ++) {
			String code = map.get(codes.get(i));
			if(null != prefix) 
				map.put(nextValue++, prefix + code.charAt(0));
			result.append(code);
			prefix = code;
		}
		return result.toString();
	}
	private static void stringCompressionTest() {
		String s = "TOBEORNOTTOBEORTOBEORNOT".toLowerCase();
		List<Integer> l = compressString(s);
		String ds = decompressString(l);
		System.out.println(s.equals(ds));
	}
	private static class LinkedNode {
		public int value;
		public LinkedNode next;
		public void printLinkedList() {
			for(LinkedNode start = this; start != null; start =
				start.next) {
				System.out.print(start.value + " ");
			}
			System.out.println();
		}
		public static LinkedNode createLinkedList(int[] input) {
			if(input.length == 0)
				return null;
			LinkedNode head = new LinkedNode();
			head.value = input[0];
			LinkedNode current = head;
			for(int i = 1; i < input.length; i ++, current = 
					current.next) {
				LinkedNode node = new LinkedNode();
				node.value = input[i];
				current.next = node;
			}
			return head;
		}

		public LinkedNode reverse() {
			LinkedNode root = null;
			for(LinkedNode before = this, after = next;
				before != null && after != null;) {
				LinkedNode nextAfter = after.next;
				after.next = before;
				before = after;
				after = nextAfter;
				root = before;
			}
			this.next = null;
			return root;
		}
		public void removeDuplicate() {
			HashSet<Integer> set = new HashSet<Integer>();
			for(LinkedNode current = this, previous = null; 
				current != null; 
				previous = current, current = current.next) {
				if(set.contains(current.value)) {
					previous.next = current.next;
					continue;
				}
				set.add(current.value);
			}
		}
					
	}

	private static LinkedNode createLinkedList() {
		LinkedNode head = new LinkedNode();
		head.value = 1;
		LinkedNode current = head;
		for(int i = 2; i < 100; i ++, current = current.next) {
			LinkedNode newNode = new LinkedNode();
			newNode.value = i;
			current.next = newNode;
		}
		return head;
	}

	private static void testReverseList() {
		LinkedNode root = createLinkedList();
		root.printLinkedList();
		root = root.reverse();
		root.printLinkedList();
	}
			
	
	private static int flipBit(int i) {
		return i ^ (~0);
	}
	private static int setBit(int num, int pos, boolean one) {
		int mask = 1 << pos;
		if(one) return num | mask;
		else return num & (~mask);
	}

	private static int reverseBit(int num) {
		for(int j = 0; j < 16; j ++) {
			int up = (((1 << j) & num) == 0) ? 0 : 1;
			int low = (((1 << (31 - j)) & num) == 0) ? 0 : 1;
			if(up == low) continue;
			num = setBit(num, j, low == 1);
			num = setBit(num, 31 - j, up == 1);
		}
		return num;
	}
	private static void testReverseBit() {
		int i = 36;
		printIntegerBinary(i);
		printIntegerBinary(reverseBit(i));
	}

	private static void printIntegerBinary(int num) {
		int bit[] = new int[32];
		for(int i = 31; i >= 0; i--) {
			bit[i] = num % 2;
			num >>= 1;
		}
		for(int i = 0; i < 32; i ++) 
			System.out.print(bit[i]);
		System.out.println();
	}

	private static void testRotation() {
		BinaryTreeNode root = createTree();
		for(int i = 0; i < 5; i ++) {
			System.out.println(root.isBinarySearchTree());	
			root = root.rotateLeft();
		}
		for(int i = 0; i < 5; i ++) {
			System.out.println(root.isBinarySearchTree());
			root = root.rotateRight();
		}
	}

	private static String removeLetters(String s1, String s2) {
		HashSet<Character> set = new HashSet<Character>();
		for(char c : s2.toCharArray()) set.add(c);
		StringBuilder sb = new StringBuilder();
		for(char c : s1.toCharArray()) {
			if(!set.contains(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private static void testRemoveLetters() {
		String s1 = "abcdefeg";
		String s2 = "ab";
		System.out.println(s1);
		System.out.println(removeLetters(s1, s2));
	}
	
	private static boolean binarySearch(int[] input, int s, int e, int target) {
		if(e < s) return false;
		int m = (s + e)/2;
		if(input[m] == target) return true;
		return binarySearch(input, s, m - 1, target) ||
			binarySearch(input, m + 1, e, target);
	}

	private static boolean searchOnShiftedSortedArray(int[] input, int begin,
			int end, int tar) {
		if(begin > end) return false;
		int mid = (begin + end) / 2;
		if(input[mid] == tar) return true;
		if(input[mid] > input[begin])
			return binarySearch(input, begin, mid - 1, tar) ||
				searchOnShiftedSortedArray(input, mid + 1, end,
					tar);
		else 
			return searchOnShiftedSortedArray(input, begin, mid - 1, 
				tar) || binarySearch(input, mid + 1, end, tar);
	}

	private static void testBinarySearch() {
		int size = 100;
		int[] input = new int[size];
		for(int i = 0; i < size; i ++)
			input[i] = i;
		for(int i = 0; i < size; i++)
			System.out.println(binarySearch(input, 0, size - 1, i));
		System.out.println("Start non-existing elements.");
		for(int i = 0; i < 10; i++)
			System.out.println(binarySearch(input, 0, size - 1, 
				101 + i));
	}
	private static String convertThreeDigit(int[] d) {
		String number[] = {"One", "Two", "Three", "Four", "Five", "Six",
			"Seven", "Eight", "Nine"};
		String teens[] = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen",
			"Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
		String tens[] = {"Twenty", "Thirty", "Forty", "Fifty", "Sixty", 
			"Seventy", "Eighty", "Ninety"};	
		StringBuilder sb = new StringBuilder();
		if(d[0] != 0) sb.append(number[d[0] - 1] + " Hundred and ");
		if(d[1] == 1) {
			sb.append(teens[d[2]]);
			return sb.toString();
		}else if(d[1] != 0)
			sb.append(tens[d[1] - 2] + " ");
		if(d[2] != 0)
			sb.append(number[d[2] - 1]);
		return sb.toString();
	}


	private static String convertIntegerToDescription(int num) {
		List<Integer> ds = new ArrayList<Integer>();
		while(0 != num) {
			ds.add(0, num % 10);
			num /= 10;
		}
		String posts[] = {"", "Thousand", "Million"};
		StringBuilder sb = new StringBuilder();
		int pIn = 0;
		for(int i = ds.size() - 1; i >= 0 ; i -= 3) {
			int[] d = new int[3];
			d[2] = ds.get(i);
			d[1] = i >= 1 ? ds.get(i - 1) : 0;
			d[0] = i >= 2 ? ds.get(i - 2) : 0;
			sb.insert(0, convertThreeDigit(d) + " " + posts[pIn++] 
				+ " ");
		}
		return sb.toString();
	}
	
	private static void testCheckConvert() {
		System.out.println(convertIntegerToDescription(1137));
	}

	private static void testRemoveDuplicates() {
		LinkedNode head = LinkedNode.createLinkedList(new int[]
			{1, 2, 3, 4, 5, 5, 6, 7, 8});
		head.removeDuplicate();
		head.printLinkedList();
	}

	private static class MazeMove {
		private final int[][] maze;
		private final int row;
		private final int column;
		private final String[] move = {"Move left", "Move up", "Move right", 
			"Move down"};
		private final List<Integer> tried;
		private final BooleanSupplier[] suppliers;

		public MazeMove(int[][] maze, int row, int column) {
			this.maze = maze;
			this.row = row;
			this.column = column;
			this.tried = new ArrayList<Integer>();
			this.maze[row][column] = 1;
			this.suppliers = new BooleanSupplier[] {
				() -> {return maze[row][column - 1] == 0;},
				() -> {return maze[row - 1][column] == 0;},
				() -> {return maze[row][column + 1] == 0;},
				() -> {return maze[row + 1][column] == 0;}
			};
		}
		private int getNextMove() {
			for(int i = 0; i < 4; i++) 
				if(!tried.contains(i) && suppliers[i].
					getAsBoolean())
					return i;
			return -1;
		}

		public boolean canMove() {
			return getNextMove() != -1;
		}

		public MazeMove move() {
			int next = getNextMove();
			tried.add(next);
			switch(next) {
				case 0:
					return new MazeMove(maze, row, column - 1);
				case 1:
					return new MazeMove(maze, row - 1, column);
				case 2:
					return new MazeMove(maze, row, column + 1);
				case 3: 
					return new MazeMove(maze, row + 1, column);
				default:
					return null;
			}
		}

		public boolean arrived(int row, int column) {
			return this.row == row && this.column == column;
		}

		public void clean() {
			maze[row][column] = 0;
		}
		public String toString() {
			return tried.size() > 0 ? move[tried.get(tried.size() - 1)] 
			       : "Destination";	
		}
	}
	
	private static String findPath(int[][] maze, int srow, int scolumn,
			int eRow, int eColumn) {
		Stack<MazeMove> allMoves = new Stack<MazeMove>();
		allMoves.push(new MazeMove(maze, srow, scolumn));
		while(!allMoves.peek().arrived(eRow, eColumn)) {
			while(allMoves.size() > 0 && !allMoves.peek().canMove()) {
				allMoves.pop().clean();
			}
			if(allMoves.size() == 0) return "No path\n";
			allMoves.push(allMoves.peek().move());
		}
		StringBuilder sb = new StringBuilder();
		for(MazeMove m : allMoves) sb.append(m + "\n");
		return sb.toString();
	}		

	private static void addRing(int[][] maze) {
		int row = maze.length;
		int column = maze[0].length;
		for(int i = 0; i < column; i++)
			maze[0][i] = maze[row -1][i] = 1;
		for(int i = 0; i < row; i ++)
			maze[i][0] = maze[i][column - 1] = 1;
	}

	private static void printMaze(int[][] maze) {
		int row = maze.length;
		int column = maze[0].length;
		for(int i = 0; i < row; i ++) {
			for(int j = 0; j < column; j ++) {
				System.out.print(maze[i][j] + " ");
			}
			System.out.println();
		}
	}

	private static void testMazeMove() {
		int size = 20;
		int[][] maze = new int[size][size];
		addRing(maze);
		for(int i = 1; i < size - 2; i ++)
			maze[i][i] = 1;
		printMaze(maze);
		System.out.print(findPath(maze, 2, 1, size - 2, size - 2));
	}

	private static boolean isColumnOk(int c, Stack<Integer> preC) {
		if(preC.contains(c)) return false;
		int r = preC.size();
		for(int rr = 0; rr < preC.size(); rr++) {
			int cc = preC.get(rr);
			if(cc - c == rr - r || cc - c == r - rr)
				return false;
		}
		return true;
	}

	private static int getNextColumn(int min, int max, Stack<Integer> preC) {
		for(int i = min; i < max; i++)
			if(isColumnOk(i, preC)) return i;
		return -1;
	}

	private static void printBoard(Stack<Integer> cs, int count) {
		System.out.println("Solution:");
		for(int i : cs) {
			for(int j = 0; j < i; j ++)
				System.out.print(0 + " ");
			System.out.print(1 + " ");
			for(int j = i + 1; j < count; j ++)
			       System.out.print(0 + " ");
			System.out.println();
		}
	}

	private static void calculateQueens() {
		int count = 10;
		Stack<Integer> cs = new Stack<Integer>();
		cs.push(0);
		while(true) {
			int i;
			for(i = 0; i < count; i ++) {
				if(isColumnOk(i, cs)) {
					cs.push(i);
					break;
				}
			}
			if(cs.size() == count) {
				printBoard(cs, count);				
			}
			if(i == count) {
				int next = -1;
				while(cs.size() > 0 && -1 == (next = 
					getNextColumn(cs.pop() + 1, count, cs)));
				if(next == -1) 
					return;
				else 
					cs.push(next);
			}
		}
	}

	private static BinaryTreeNode mergeHeap(BinaryTreeNode root1, 
		BinaryTreeNode root2) {
			if(root1 == null && root2 == null) return null;
			if(root1 == null || root2 == null) return root1 == null ?
				root2 : root1;	
			BinaryTreeNode small = root1.value < root2.value ?
				root1 : root2;
			BinaryTreeNode large = root1 == small ? root2 : root1;
			BinaryTreeNode temp = small.left;
			small.left = small.right;
			small.right = mergeHeap(large, temp);
			return small;
	}
	
	private static BinaryTreeNode removeFromMinHeap(BinaryTreeNode root) {
		return root == null ? null : mergeHeap(root.left, root.right);
	}

	private static BinaryTreeNode insertHeap(BinaryTreeNode root, int value) {
		BinaryTreeNode node = new BinaryTreeNode(value);
		node.value = value;
		return mergeHeap(root, node);
	}

	private static void testHeap() {
		BinaryTreeNode root = new BinaryTreeNode(11);
		for(int i = 10; i > 0; i --)
			root = insertHeap(root, i);
		while(null != root) {
			System.out.println(root.value);
			root = removeFromMinHeap(root);
		}
	}
	
	private static int getCharCount(String input, char c) {
		int sum = 0;
		for(int i = 0; i < input.length() && input.charAt(i) == c; i++) 
			sum ++;
		return sum;
	}

	private static boolean regularMatch(String reg, String input) {
		if(reg.length() == 0) return input.length() == 0;
		if(reg.length() > 1 && (reg.charAt(1) == '*'
				|| reg.charAt(1) == '+')) {
			String newReg = reg.substring(2);
			int sum = getCharCount(input, reg.charAt(0));
			int start = reg.charAt(1) == '*' ? 0 : 1;
			for(int i = start; i <= sum; i++) {
				if(regularMatch(newReg, input.substring(i)))
						return true;
			}
			return false;
		} 
		return reg.charAt(0) == input.charAt(0) && 
			regularMatch(reg.substring(1), input.substring(1));
	}

	private static void testRegularMatch() {
		System.out.println(regularMatch("abc", "abc"));
		System.out.println(regularMatch("ab*c", "abc"));
		System.out.println(regularMatch("ab+c", "abc"));
		System.out.println(regularMatch("ab+c", "abbc"));
		System.out.println(regularMatch("ab*c", "abbbc"));
		System.out.println(regularMatch("abc", "abcd"));
		System.out.println(regularMatch("ab+c", "ac"));
		System.out.println(regularMatch("ab*c", "ac"));
	}
	
	private static int getSplitPoint(int a, int b) {
		int s = Math.max(a, b);
		if(s != -1) return s;
		return s == a ? b : a;
	}
	

	private static int calculateFormular(String form) {
		int minus = form.lastIndexOf("-");
		int plus = form.lastIndexOf("+");
		int split = getSplitPoint(minus, plus);
		if(split == -1) {
			int mul = form.lastIndexOf("*");
			int div = form.lastIndexOf("/");
			split = getSplitPoint(mul, div);
		}
		if(split == -1)
			return Integer.parseInt(form.trim());
		int first = calculateFormular(form.substring(0, split));
		int second = calculateFormular(form.substring(split + 1));
		switch(form.charAt(split)) {
			case '+': return first + second;
			case '-': return first - second;
			case '*': return first * second;
			case '/': return first / second;
			default: return 0;
		}
	}

	private static void testCalculateForm() {
		System.out.println(calculateFormular("1 + 3 + 2"));
		System.out.println(calculateFormular("1 + 3 * 2"));
		System.out.println(calculateFormular("1 + 3 / 2"));
		System.out.println(calculateFormular("1 * 3 + 2"));
		System.out.println(calculateFormular("1 * 3 + 2 * 3 - 3 / 1"));
	}
			
	private static class DoubleLinkedNode<T> {
		public T value;
		public DoubleLinkedNode<T> before;
		public DoubleLinkedNode<T> after;
		public String toString() { return value.toString();}
	}

	private static class DoubleLinkedList<T> {
		private DoubleLinkedNode<T> head;
		private DoubleLinkedNode<T> tail;

		public DoubleLinkedList() {
			this.head = this.tail = null;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			for(DoubleLinkedNode<T> current = head; current != null;
					current = current.after) {
				sb.append(current.value + "<->");
			}
			return sb.toString();
		}

		public DoubleLinkedNode<T> add2Head(DoubleLinkedNode<T> node) {
			if(null == head) head = tail = node;
			else {
				node.after = head;
				node.after.before = node;
				head = node;
			}
			return node;
		}

		public DoubleLinkedNode<T> add2Tail(DoubleLinkedNode<T> node) {
			if(null == tail) tail = head = node;
			else {
				tail.after = node;
				node.before = tail;
				tail = node;
			}
			return node;
		}
		public DoubleLinkedNode<T> removeTail() {
			DoubleLinkedNode<T> temp = tail;
			if(null == tail) return temp;
			if(head == tail) { head = tail = null; return temp; }
			tail.before.after = null;
			tail = tail.before;
			return temp;
		}

		public void move2Head(DoubleLinkedNode<T> node) {
			if(node == head || head == tail) return;
			node.before.after = node.after;
			if(null != node.after) node.after.before = node.before;
			else tail = node.before;
			node.after = head;			
			head.before = node;
			head = node;
			head.before = null;
		}
	
	}


	private static class SizedHashtable<K, V> {
		private final HashMap<K, DoubleLinkedNode<KVPair>> table;
		private final DoubleLinkedList<KVPair> accessed;
		private final int capacity;

		private class KVPair {
			public K key;
			public V value;
			public String toString() {return key.toString();}
		}	

		public SizedHashtable(int capacity) {
			this.capacity = capacity;
			this.table = new HashMap<K, DoubleLinkedNode<KVPair>>();
			this.accessed = new DoubleLinkedList<KVPair>();
		}

		public V getValue(K key) {
			if(!table.containsKey(key)) return null;
			DoubleLinkedNode<KVPair> node = table.get(key);
			accessed.move2Head(node);
			System.out.println("accessed " + key);
			System.out.println(accessed);
			return node.value.value;	
		}

		public void put(K k, V v) {
			if(table.containsKey(k)) return;
			if(table.size() == this.capacity) {
				DoubleLinkedNode<KVPair> tail = accessed.
					removeTail();
				table.remove(tail.value.key);
				System.out.println("removed " + tail.value.key);
			}
			DoubleLinkedNode<KVPair> node = new DoubleLinkedNode
				<KVPair>();
			node.value = new KVPair();
			node.value.key = k;
			node.value.value = v;
			accessed.add2Head(node);
			table.put(k, node);
			System.out.println("put " + k);
		}
	}
	private static void testSizedHashtable() {
		SizedHashtable<Integer, Integer> table = new SizedHashtable
			<Integer, Integer>(3);
		table.put(1, 2);
		table.put(2, 3);
		table.put(3, 5);
		table.getValue(1);
		table.put(4, 2);
	}
	private static boolean isPalindrome(String s) {
		for(int i = 0; i < s.length()/2; i++)
			if(s.charAt(i) != s.charAt(s.length() - 1 -i)) return false;
		return true;
	}

	private static int minSplitString2Palindromes(String s) {
		int[][] matrix = new int[s.length()][s.length()];
		for(int i = 0; i < s.length(); i++) matrix[i][i] = 0;
		for(int gap = 1; gap < s.length(); gap++) {
			for(int row = 0; row < s.length() - gap; row ++) {
				int column = row + gap;
				if(isPalindrome(s.substring(row, column + 1)))
					matrix[row][column] = 0;
				else {
					int min = Integer.MAX_VALUE;
					for(int middle = row; middle < column; 
							middle ++) {
						int m = matrix[row][middle] +
							matrix[middle + 1][column] 
								+ 1;
						min = m < min ? m : min;
					}
					matrix[row][column] = min;
				}
			}
		}
		return matrix[0][s.length() - 1];
	}

	private static void testSplit2Palindrome() {
		System.out.println(minSplitString2Palindromes("abba"));
		System.out.println(minSplitString2Palindromes("abbac"));
		System.out.println(minSplitString2Palindromes("dabbac"));
	}	
	public static void main(String[] args) {
		testSplit2Palindrome();
	}
}
