package edu.dlf.refactoring.leecode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import edu.dlf.refactoring.leecode.GraphProblems.BinaryTreeNode;

public class CommonProblems {

	private static boolean checkPalindrome(String s) {
		for(int i = 0; i < s.length()/2; i++ ){ 
				if(s.charAt(i) != s.charAt(s.length() - i))
					return false;
		}
		return true;
	}

	private static String expandPalindrome(int position, String s) {
		int left = 0;
		int right = 0;
		if(position % 2 == 1) {
			left = position / 2;
			right = left + 1;
		} else {
			left = position / 2;
			right = left;
		}
		while(left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
			left --;
			right ++;
		}
		if(left < 0 || right >= s.length() || s.charAt(left) != s.charAt(right)) {
			right --;
			left ++;
		}
		return s.substring(left, right + 1);
	}


	private static String findLongestPalindrome(String s) {
		String max = "";
		for(int pos = 0; pos < s.length() * 2 - 1; pos ++)
		{
			String current = expandPalindrome(pos, s);
			if(current.length() > max.length()) 
				max = current;
		}
		return max;
	}
	
	private static void findPalindromeTest() {
		System.out.println(findLongestPalindrome("ihagdnandgeajnag"));
	}
	
	private static String removeCharacter(String s, char c) {
		StringBuilder sb = new StringBuilder();
		for(char cs : s.toCharArray()) {
			if(cs == c)
				continue;
			sb.append(cs);
		}
		return sb.toString();
	}

	private static HashSet<String> allPermutationOfStringRecursive(String s) {
		HashSet<String> set = new HashSet<String>();
		if(s.length() == 0) {
			set.add("");
			return set;
		}
		char c = s.charAt(0);
		HashSet<String> remains = allPermutationOfStringRecursive(s.substring(1));
		for(String r : remains) {
			for(int first = 0; first <= r.length(); first ++) {
				String firstPart = r.substring(0, first);
				String secondPart = r.substring(first);
				set.add(firstPart + c + secondPart);
			}
		}
		return set;
	}
	private static String createSmallestString(char[] cs) {
		List<Character> list = new ArrayList<Character>();
		for(char c : cs) list.add(c);
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for(char c : list)
			sb.append(c);
		return sb.toString();
	}
	
	private static String getNextString(String s) {
		for(int i = s.length() - 2; i >= 0; i --) {
			char after = s.charAt(i + 1);
			char before = s.charAt(i);
			if(Character.compare(before, after) < 0) {
				String first = s.substring(0, i);
				String second = s.substring(i);
				char min = Character.MAX_VALUE;
				int minPosition = 0;
				for(int j = 1; j < second.length(); j ++)
				{
					if(Character.compare(second.charAt(j), before) < 0)
						continue;
					if(Character.compare(second.charAt(j), min) < 0)
					{	
						min = second.charAt(j);
						minPosition = j;
					}
				}
				first += second.charAt(minPosition);
				second = second.substring(0, minPosition) + second.substring
					(minPosition + 1); 
				return first + createSmallestString(second.toCharArray()); 
			}
		}
		return null;
	}
	
	private static void printAllStringPermutation(String s) {
		s = createSmallestString(s.toCharArray());
		int count = 1;
		for(String temp = s; temp != null; temp = getNextString(temp)) {
			System.out.println(temp + " " + count ++);
		}
	}
	private static void printAllStringPermutation2(String s) {
		HashSet<String> set = allPermutationOfStringRecursive(s);
		int count = 1;
		for(String l : set) 
			System.out.println(l + " " + count ++);
	}

	
	private static void testPermutations() {
		printAllStringPermutation("abcdefg");
		
	}
	
	private static int findMissingNumber(int[] nums) {
		int[] first = new int[10];
		int[] second = new int[10];
		boolean isHundred = true;
		for(int i = 0; i < 10; i ++)
			first[i] = second[i] = 0;
		for(int num : nums) {
			if(num == 100) {
				isHundred = false;
				num = 0;
			}
			int f = num % 10;
			int s = num / 10;
			first[f] ++;
			second[s] ++;
		}
		if(isHundred)
			return 100;
		int i, j;
		for(i = 0; i < 10; i++)
			if(first[i] != 10)
				break;
		for(j = 0; j < 10; j ++)
			if(second[j] != 10)
				break;
		return j * 10 + i;
	}
	
	private static void testMissingNumbers() {
		int[] num = new int[99];
		int index = 0;
		for(int i = 1; i <= 100 ; i ++) {
			if(i != 56) {
				num[index] = i;
				index ++;
			}
		}
		System.out.println(findMissingNumber(num));
	}
	
	private static HashSet<Integer> findDuplicateNumbers(List<Integer> nums) {
		HashSet<Integer> set = new HashSet<Integer>();
		HashSet<Integer> results = new HashSet<Integer>();
		for(int n : nums) {
			int before = set.size();
			set.add(n);
			if(set.size() == before)
				results.add(n);
		}
		return results;
	}
	
	private static void testDuplicate() {
		List<Integer> input = new ArrayList<Integer>();
		for(int i = 0; i < 100; i++) 
			input.add(i);
		for(int i = 11; i < 20; i++)
			input.add(i);
		HashSet<Integer> set = findDuplicateNumbers(input);
		for(int i : set) 
			System.out.println(i);
		
	}
	
	private HashSet<Integer> getDiff(List<Integer> list1, List<Integer> list2) {
		HashSet<Integer> set = new HashSet<Integer>(list2);
		HashSet<Integer> results = new HashSet<Integer>();
		for(Integer i : list1) {
			if(!set.contains(i))
				results.add(i);
		}
		return results;
	}
	
	private static boolean checkPalindrome(int num) {
		List<Integer> digits = new ArrayList<Integer>();
		while(num != 0) {
			digits.add(0, num % 10);
			num /= 10;
		}
		for(int i = 0, j = digits.size() - 1; i < j; i ++, j--) {
			if(digits.get(i) != digits.get(j))
				return false;
		}
		return true;
	}
	
	private static void testCheckPalindrome() {
		System.out.println(checkPalindrome(12121));
		System.out.println(checkPalindrome(12131));
	}
	
	private static boolean checkArmstrong(int num) {
		List<Integer> digits = new ArrayList<Integer>();
		int original = num;
		while(num != 0) {
			digits.add(0, num % 10);
			num /= 10;
		}
		int add = 0;
		for(int i : digits) {
			add += i * i * i;
		}
		return add == original;
		
	}
	
	private static void testCheckArmstrong() {
		System.out.println(checkArmstrong(371));
	}
	
	private static List<Integer> getArmstrongNumbers() {
		int[] base = new int[10];
		for(int i = 0; i < 10; i ++) {
			base[i] = i * i * i;
		}
		List<Integer> results = new ArrayList<Integer>();
		for(int i = 0; i < 10; i ++)
			for(int j = i; j < 10; j ++) 
				for(int k = j; k < 10; k ++)
				{
					int add = base[i] + base[j] + base[k];
					int original = add;
					List<Integer> digits = new ArrayList<Integer>();
					for(int d = 0; d < 3; d ++, add /= 10)
						digits.add(0, add % 10);
					List<Integer> shouldBe = new ArrayList<Integer>();
					shouldBe.add(i);
					shouldBe.add(j);
					shouldBe.add(k);
					Collections.sort(shouldBe);
					Collections.sort(digits);
					int d;
					for(d = 0; d < 3; d ++){
						if(shouldBe.get(d) != digits.get(d))
							break;
					}
					if(d == 3) {
						results.add(original); 
					}
				}
		return results;
	}
	private static void testGetAllArmstrong() {
		List<Integer> nums = getArmstrongNumbers();
		for(int n : nums) {
			System.out.println(n);
		}
		
	}
	private int findSecondMax(int[] input) {
	    int max = Integer.MIN_VALUE;
	    int second = Integer.MIN_VALUE;
	    for(int i = 0; i < input.length; i ++) {
	        if(input[i] >= max) {
	            second = max;
	            max = input[i];
	        } else if (input[i] > second) {
	            second = input[i];
	        }
	    }
	    return second;
	}

	private static void printWaysToClimb(int remain, List<Integer> prefix) {
	    if(remain == 0) {
	        for(int i : prefix) 
	            System.out.print(i + " ");
	        
	        System.out.println();
	        return;
	    }
	    List<Integer> next = new ArrayList<Integer>(prefix);
	    next.add(1);
	    printWaysToClimb(remain - 1, next);
	    if(remain > 1) {
	        next = new ArrayList<Integer>(prefix);
	        next.add(2);
	        printWaysToClimb(remain - 2, next);    
	    }
	}
	
	private static double pow(int n, double base) {
		if(n < 0) {
			return 1/pow(-n, base);
		}
		if(n == 1) 
			return base;
		if(n % 2 == 0) {
			return pow(n/2, base) * pow(n/2, base);
		} else {
			return pow(n/2, base) * pow(n/2, base) * base;
		}
	}
	
	private static void testPow() {
		System.out.println(-3/2);
		System.out.println(pow(-3, 2.0));
	}
	            

	private static BinaryTreeNode getLowestCommonAncestor1(BinaryTreeNode root,
			BinaryTreeNode n1, BinaryTreeNode n2) {
		if(root == n1 || root == n2 || root == null)
			return root;
		BinaryTreeNode left = getLowestCommonAncestor1(root.left, n1, n2);
		BinaryTreeNode right = getLowestCommonAncestor1(root.right, n1, n2);
		if(left != null && right != null)
			return root;
		return left == null ? right : left;
	}

	private static BinaryTreeNode getLCA2(BinaryTreeNode root, BinaryTreeNode n1,
			BinaryTreeNode n2){
		HashSet<BinaryTreeNode> visited = new HashSet<BinaryTreeNode>();
		while(n1 != null || n2 != null) {
			if(n1 != null) {
				if(visited.contains(n1.parent)) {
					return n1.parent;
				}
				visited.add(n1.parent);
				n1 = n1.parent;
			}
			if(n2 != null) {
				if(visited.contains(n2.parent)) {
					return n2.parent;
				}
				visited.add(n2.parent);
				n2 = n2.parent;
			}
		}
		return null;
	}

	private static int getDepth(BinaryTreeNode n1) {
		int depth = 0;
		while(n1 != null) {
			n1 = n1.parent;
			depth ++;
		}
		return depth;
	}

	private static BinaryTreeNode getLCA3(BinaryTreeNode root, BinaryTreeNode n1,
			BinaryTreeNode n2) {
		int d1 = getDepth(n1);
		int d2 = getDepth(n2);
		if(d1 > d2) {
			BinaryTreeNode tmp = n1;
			n1 = n2;
			n2 = tmp;
		}
		int dDiff = Math.abs(d1 - d2);
		for(int i = 0; i < dDiff; i ++)
			n2 = n2.parent;
		while(n1 != null && n2 != null) {
			if(n1 == n2) return n1;
			n1 = n1.parent;
			n2 = n2.parent;
		}
		return null;
	}

	private static class RandomGenerator{
		final long a = 0x5DEECE66DL;
		final long c = 0xBL;
		final long m = (1 << 48) - 1;
		long seed;
		
		public RandomGenerator(long seed) {
			this.seed = seed;
		}

		public long next() {
			seed = (seed * a + c) & m;
			return seed;
		}
	}

	private static RandomGenerator random = new RandomGenerator(6);
	private static List<Integer>[] matchNutsBolts(List<Integer> nuts, 
			List<Integer> bolts) {
		int count = nuts.size();
		if(count == 0) {
			List<Integer>[] results = (List<Integer>[]) Array.newInstance
					(ArrayList.class, 2);
			results[0] = new ArrayList<Integer>();
			results[1] = new ArrayList<Integer>();
			return results;
		}
		int pivot = nuts.get((int)random.next() % count); 
		int map = 0;
		List<Integer> preBolts = new ArrayList<Integer>();
		List<Integer> postBolts = new ArrayList<Integer>();
		for(int i = 0; i < bolts.size(); i ++) {
			if(pivot == bolts.get(i)) {
				map = bolts.get(i);
			} else if(pivot > bolts.get(i)) {
				preBolts.add(bolts.get(i));
			} else {
				postBolts.add(bolts.get(i));
			}
		}
		List<Integer> preNuts = new ArrayList<Integer>();
		List<Integer> postNuts = new ArrayList<Integer>();
		for(int i = 0; i < nuts.size(); i ++) {
			if(map < nuts.get(i)) {
				postNuts.add(nuts.get(i));
			} else if(map > nuts.get(i)) {
				preNuts.add(nuts.get(i));
			}
		}
		List<Integer>[] results1 = matchNutsBolts(preNuts, preBolts);
		List<Integer>[] results2 = matchNutsBolts(postNuts, postBolts);
		results1[0].add(pivot);
		results1[0].addAll(results2[0]);
		results1[1].add(map);
		results1[1].addAll(results2[1]);
		return results1;
	}
	
	private static void testNutsBolts() {
		List<Integer> nuts = new ArrayList<Integer>();
		List<Integer> bolts = new ArrayList<Integer>();
		for (int i = 0; i < 100; i ++) {
			nuts.add(i);
			bolts.add(0, i);
		}
		List<Integer>[] results = matchNutsBolts(nuts, bolts);
		for(int i = 0; i < 100; i ++) {
			System.out.println(results[0].get(i) + " " + results[1].get(i));
		}
	}
	private static int maxSubarray(int[] nums) {
		int max = Integer.MIN_VALUE;
		// the max value of the summation when ending at a value.
		int endingHere = 0;
		for(int i = 0; i < nums.length; i ++) {
			if(endingHere < 0) {
				endingHere = nums[i];
			} else {
				endingHere += nums[i];
			}
			if(max < endingHere) max = endingHere;
		}
		return max;
	}


	public static void main(String[] args) {
		testNutsBolts();
	}
}











