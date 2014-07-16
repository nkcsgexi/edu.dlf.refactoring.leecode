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
	public static void main(String[] args) {
		testPermutations();
	}
}
