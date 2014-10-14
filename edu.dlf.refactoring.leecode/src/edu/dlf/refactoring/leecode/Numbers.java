import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;

public class Numbers {
	

	private static int convertRomanNumber(String s) throws Exception{
		if(s.equals("")) return 0;
		HashMap<Character, Integer> map = new LinkedHashMap<Character,
			Integer>();
		map.put('L', 50);
		map.put('X', 10);
		map.put('V', 5);
		map.put('I', 1);
		for(char c : map.keySet()) {
			int i = s.indexOf(c);
			if(i != -1) {
				int before = convertRomanNumber(s.substring(0, i));
				int after = convertRomanNumber(s.substring(i + 1));
				return map.get(c) + after - before;
			}
		}
		throw new Exception("cannot parse");
	}

	private static boolean isNumPalindrome(int num) {
		int div = 1;
		while(num / div >= 10) div *= 10;
		while(num != 0) {
			int r = num % 10;
			int l = num / div;
			if(r != l) return false;
			num = (num % div)/10;
			div /= 10;
		}
		return true;
	}

	private static String nonrepeatingLongest(String s) {
		HashSet<Character> set = new HashSet<Character>();
		int maxLen = Integer.MIN_VALUE;
		int maxStart = 0;
		int start = 0;
		for(int end = 0; end < s.length(); end ++) {
			char c = s.charAt(end);
			if(!set.contains(c)) {
				set.add(c);
				continue;
			}
			if(end - start > maxLen) {
				maxLen = end - start;
				maxStart = start;
			}
			while(s.charAt(start) != c) start ++;
		}
		if(s.length() - start > maxLen) {
			maxLen = s.length() - start;
			maxStart = start;
		}
		return s.substring(maxStart, maxLen);
	}

	private static class Node {
		public Node left;
		public Node right;
		public int value;
		public Node(int value) {this.value = value;}
	}


	private static int[] getSub(int[] data, int s, int e) {
		int[] result = new int[e - s + 1];
		for(int i = 0; i < result.length; i ++)
			result[i] = data[s + i];
		return result;
	}
	private static Node recreateTree(int[] in, int[] pre) {
		if(in.length == 0) return null;
		if(in.length == 1) return new Node(pre[0]);
		int pivot = pre[0];
		int len = in.length;
		Node root = new Node(pivot);
		int i = 0;
		for(; i < len; i ++)
			if(in[i] == pivot)
				break;
		int[] inbefore = getSub(in, 0, i - 1);
		int[] inafter = getSub(in, i + 1, len - 1);
		int[] prebefore = getSub(pre, 1, i);
		int[] preafter = getSub(in, i + 1, len - 1);
		root.left = recreateTree(inbefore, prebefore);
		root.right = recreateTree(inafter, preafter);
		return root;
	}
		
	private static int partition(int[] data, int M) {
		int N = data.length;
		int[][] matrix = new int[M][N];
		for(int i = 0; i < M; i++)
			for(int j = 0; j < N; j ++)
				matrix[i][j] = 0;
		matrix[0][0] = data[0];
		for(int i = 1; i < N - 1; i ++)
			matrix[0][i] = matrix[0][i - 1] + data[i];
		for(int i = 1; i < M; i ++)
			matrix[i][0] = data[0];
		for(int i = 1; i < M; i ++) {
			for(int j = 1; j < N; j ++) {
				int value = Integer.MAX_VALUE;
				for(int k = 0; k <= j; k ++) {
					int rest = matrix[i - 1][k];
					int remain = 0;
					for(int kk = k + 1; kk <= j; kk ++)
						remain += data[kk];
					int max = Math.max(rest, remain);
					value = max < value ? max : value;
				}
				matrix[i][j] = value;
			}
		}
		return matrix[M - 1][N - 1];
	}
	private static int coins(int[] data) {
		int len = data.length;
		int[][] matrix = new int[len][len];
		for(int i = 0; i < len; i ++)
			matrix[i][i] = data[i];
		for(int i = 0; i < len - 1; i ++) 
			matrix[i][i + 1] = Math.max(data[i], data[i + 1]);
		for(int layer = 2; layer < len; layer ++) {
			for(int i = 0; i + layer < len; i ++) {
				int j = i + layer;
				int op1 = data[i] + matrix[i + 2][j];
				int op2 = data[i] + matrix[i + 1][j - 1];
				int op3 = data[j] + matrix[i + 1][j - 1];
				int op4 = data[j] + matrix[i][j - 2];
				matrix[i][j] = Math.max(Math.max(op1, op2),
					Math.max(op3, op4));
			}
		}
		return matrix[0][len - 1];
	}
	private static Iterable<Integer> sliding(int[] data, int K) {
		Queue<Integer> q = new LinkedList<Integer>();
		List<Integer> result = new ArrayList<Integer>();
		int len = data.length;
		for(int i = 0; i < len; i ++) {
			int d = data[i];
			while(q.size() >= K || (q.size() > 0 && q.peek() <= d))
				q.remove();
			q.add(d);
			if(i >= K - 1)
				result.add(q.peek());
		}
		return result;
	}
	private static String palindrome(String s) {
		StringBuilder sb = new StringBuilder();
		sb.append('#');
		for(char c : s.toCharArray())
			sb.append(c + "#");
		String ss = sb.toString();
		int[] lens = new int[ss.length()];
		for(int i = 0; i < lens.length; i ++)
			lens[i] = 0;
		int far = 0;
		int farIndex = 0;
		for(int i = 1; i < lens.length; i ++) {
			int l = 0;
			if(far > i) {
				l = Math.min(far - i,
					lens[farIndex * 2 - i]); 
			}
			int left = i - l - 1;
			int right = i + l + 1;
			while(left > 0 && right < lens.length &&
					ss.charAt(left) == ss.charAt(right)) {
				left --;
				right ++;
			}
			left ++;
			right --;
			if(right > far) {
				far = right;
				farIndex = i;
			}
		}
		int max = Integer.MIN_VALUE;
		int maxIndex = 0;
		for(int i = 0; i < ss.length(); i ++) {
			if(lens[i] > max) {
				max = lens[i];
				maxIndex = i;
			}
		}
		int start = maxIndex - max;
		int len = max * 2 + 1;
		start /= 2;
		len /= 2;
		return s.substring(start, len);
	}





	public static void main(String[] args) {
	}

}
