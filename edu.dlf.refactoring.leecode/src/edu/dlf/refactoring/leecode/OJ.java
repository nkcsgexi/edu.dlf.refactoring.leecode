import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class OJ {

	private static int maxSumSubarray(int[] a) {
		int maxEnd = 0;
		int max = Integer.MIN_VALUE;
		for(int i : a) {
			maxEnd += i;
			max = maxEnd > max ? maxEnd : max;
			maxEnd = Math.max(0, maxEnd);
		}
		return max;
	}

	private static void subset(int[] a, List<Integer> s, int i) {
		if(i == a.length) {
			for(int j : a) 
				System.out.print(j + " ");
			return;
		}
		List<Integer> cs = new ArrayList<Integer>();
		cs.addAll(s);
		cs.add(a[i]);
		subset(a, s, i + 1);
		subset(a, cs, i + 1);
	}

	

	private static boolean wordBreak(String t, HashSet<String> dict) {
		int len = t.length();
		boolean breakable[] = new boolean[len + 1];
		breakable[0] = dict.contains(t.substring(0, 1));
		for(int i = 1; i < len; i ++) {
			int l = i + 1;
			String self = t.substring(0, l);
			if(dict.contains(self)) {
				breakable[i] = true;
				continue;
			}
			breakable[i] = false;
			for(int fl = 1; fl < i; fl ++) {
				String second = t.substring(fl, l - fl);
				if(breakable[fl - 1] && dict.contains(second)){
					breakable[i] = true;
					break;
				}
			}
		}
		return breakable[len - 1];
	}

	private static int wordLadder(String start, String end, HashSet<String> 
			dict) {
		HashSet<String> black = new HashSet<String>();
		HashSet<Character> letters = new HashSet<Character>();
		for(char c = 'a'; c <= 'z'; c ++) letters.add(c);
		Queue<String> q = new LinkedList<String>();
		Queue<String> q2 = new LinkedList<String>();
		q.add(start);
		int step = 0;
		while(q.size() != 0) {
			step ++;
			while(q.size() != 0) {
				String s = q.remove();
				for(int i = 0; i < s.length(); i ++) {
					String first = s.substring(0, i);
					String second = i < s.length() - 1 ? s.
						substring(i + 1) : "";
					for(char c : letters) {
						if(c == s.charAt(i))
							continue;
						String rep = first + c + second;
						if(dict.contains(rep) && 
							!black.contains(rep)) {
							q2.add(rep);
							black.add(rep);
						}
						if(rep.equals(end))
							return step;
					}
				}
			}
			Queue<String> t = q;
			q = q2;
			q2 = t;

		}
		return -1;
	}
			

			
	


	public static void main(String[] args) {
	}
}
