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
	
	public static void main(String[] args) {
		testMissingNumbers();
	}
}
