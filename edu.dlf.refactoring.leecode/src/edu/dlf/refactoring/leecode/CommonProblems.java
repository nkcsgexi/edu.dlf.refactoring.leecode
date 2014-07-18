package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
		System.out.println(pow(-3, 2.0));
	}
	            

	
	public static void main(String[] args) {
		testPow();
	}
}











