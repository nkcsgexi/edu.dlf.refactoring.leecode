package edu.dlf.refactoring.leecode;

import java.util.HashSet;

public class StringProblems {

	private static String findTheLongestCommonString(String s1, String s2) {
		int matrix[][] = new int[s1.length()][s2.length()];
		for (int row = 0; row < s1.length(); row++) {
			matrix[row][0] = s1.charAt(row) == s2.charAt(0) ? 1 : 0;
		}
		for (int column = 0; column < s2.length(); column++) {
			matrix[0][column] = s1.charAt(0) == s2.charAt(column) ? 1 : 0;
		}
		int maxLength = 0;
		int maxRow = 0;
		for (int row = 1; row < s1.length(); row++) {
			for (int column = 1; column < s2.length(); column++) {
				if (s1.charAt(row) == s2.charAt(column)) {
					matrix[row][column] = matrix[row - 1][column - 1] + 1;
					if (matrix[row][column] > maxLength) {
						maxLength = matrix[row][column];
						maxRow = row;
					}
				} else {
					matrix[row][column] = 0;
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (; maxLength > 0; maxLength--, maxRow--) {
			sb.append(s1.charAt(maxRow));
		}
		return sb.reverse().toString();
	}

	private static String findLongestPalindromic(String s) {
		boolean matrix[][] = new boolean[s.length()][s.length()];
		for (int i = 0; i < s.length(); i++) {
			matrix[i][i] = true;
		}
		for (int i = 0; i < s.length() - 1; i++) {
			matrix[i][i + 1] = s.charAt(i) == s.charAt(i + 1);
		}
		int maxLength = 1;
		String maxString = s.substring(s.length() - 1);

		for (int count = 2; count < s.length(); count++) {
			for (int i = 0; i < s.length() - count; i++) {
				int j = count + i;
				matrix[i][j] = matrix[i + 1][j - 1]
						&& s.charAt(i) == s.charAt(j);
				if (matrix[i][j] && j - i + 1 > maxLength) {
					maxLength = j - i + 1;
					maxString = s.substring(i, j + 1);
				}
			}
		}
		return maxString;
	}

	private static String findLongestNonrepetitiveSubString(String input) {
		int[] charCounts = new int[26];
		for (int i = 0; i < 26; i++) {
			charCounts[i] = 0;
		}
		int max = Integer.MIN_VALUE;
		int maxStart = 0;
		int maxEnd = 0;
		for (int start = 0, end = 0; end < input.length(); end++) {
			char c = input.charAt(end);
			charCounts[c - 'a']++;
			if (charCounts[c - 'a'] == 1)
				continue;
			while (charCounts[c - 'a'] != 1) {
				charCounts[input.charAt(start) - 'a']--;
				start++;
			}
			if (max < end - start + 1) {
				max = end - start + 1;
				maxStart = start;
				maxEnd = end;
			}
		}
		return input.substring(maxStart, maxEnd + 1);
	}

	/* Determine if a string consists of duplicate letters. */
	private static boolean testDuplicate(String s) {
		char[] chs = s.toCharArray();
		int flag = 0;
		for (char c : chs) {
			int position = c - 'a';
			int mask = 1 << position;
			if (((flag >> position) % 2) != 0)
				return true;
			flag = flag | mask;
		}
		return false;
	}

	private static String reverseString(String s) {
		char[] chars = s.toCharArray();
		for (int start = 0, end = chars.length - 1; start < end; start++, end--) {
			char tmp = chars[start];
			chars[start] = chars[end];
			chars[end] = tmp;
		}
		return String.copyValueOf(chars);
	}

	/* Determine if one string is the permutation of another string. */
	private static boolean checkPermutation(String s1, String s2) {
		int[] letters = new int[26];
		for (char c : s1.toCharArray()) {
			letters[c - 'a']++;
		}
		for (char c : s2.toCharArray()) {
			letters[c - 'a']--;
		}
		for (int count : letters) {
			if (count != 0)
				return false;
		}
		return true;
	}

	/* Replace all occurrences of space to 02% */
	private static String replaceSpaces(String input) {
		StringBuilder sb = new StringBuilder();
		for (char c : input.toCharArray()) {
			if (c == ' ') {
				sb.append("02%");
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/* Simple compression algorithm. change aaabbc to a3b2c1. */
	private static String simpleCompress(String input) {
		StringBuilder sb = new StringBuilder();
		for (int pointer = 0; pointer < input.length();) {
			char current = input.charAt(pointer);
			sb.append(current);
			int count = 1;
			pointer++;
			while (pointer < input.length() && input.charAt(pointer) == current) {
				count++;
				pointer++;
			}
			sb.append(count);
		}
		return sb.toString();
	}

	/* Rotate an image by 90 degree to the right. */
	private static int[][] rotateImage(int[][] matrix, int height) {
		for (int layer = 0; layer < (height + 1) / 2; layer++) {
			int offset = height - layer - 1;
			int tmp = matrix[layer][layer];
			matrix[layer][layer] = matrix[offset][layer];
			matrix[offset][layer] = matrix[offset][offset];
			matrix[offset][offset] = matrix[layer][offset];
			matrix[layer][offset] = tmp;

			int copyLength = offset - layer - 1;
			int[] tempArray = new int[copyLength];
			for (int i = 0; i < copyLength; i++) {
				tempArray[i] = matrix[layer + i + 1][layer];
			}
			for (int i = 0; i < copyLength; i++) {
				matrix[layer + i + 1][layer] = matrix[offset][layer + 1 + i];
			}
			for (int i = 0; i < copyLength; i++) {
				matrix[offset][layer + 1 + i] = matrix[offset - 1 - i][offset];
			}
			for (int i = 0; i < copyLength; i++) {
				matrix[offset - 1 - i][offset] = matrix[layer][offset - i - 1];
			}
			for (int i = 0; i < copyLength; i++) {
				matrix[layer][layer + 1 + i] = tempArray[copyLength - i - 1];
			}
		}
		return matrix;
	}

	/*
	 * Write an algorithm such that if an element in an MxN matrix is 0, its
	 * entire row and column is set to 0
	 */
	private static int[][] setZero(int[][] matrix, int weight, int height) {
		HashSet<Integer> zeroRows = new HashSet<Integer>();
		HashSet<Integer> zeroColumns = new HashSet<Integer>();
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < height; column++) {
				if (matrix[row][column] == 0) {
					zeroRows.add(row);
					zeroColumns.add(column);
				}
			}
		}
		for (int row : zeroRows) {
			for (int column = 0; column < weight; column++) {
				matrix[row][column] = 0;
			}
		}
		for (int column : zeroColumns) {
			for (int row = 0; row < height; row++) {
				matrix[row][column] = 0;
			}
		}
		return matrix;
	}

	/*
	 * Assume you have a method isSubstring which checks if one word is a
	 * substring of another. Given two strings, s1 and s2, write code to check
	 * if s2 is a rotation of s1 using only one call to isSubstring (i.e.,
	 * "waterbottle" is a rotation of "erbottlewat").
	 */
	private static boolean checkeRotation(String s1, String s2) {
		if (s1.length() != s2.length())
			return false;
		return (s1 + s1).contains(s2);
	}

	/*
	 * Match an input string to a pattern. The pattern only shows the sequence
	 * of characters. And the input who matches the pattern shall have the
	 * characters in the right order according to the given pattern. For
	 * instance, "abbccd" matches with "abc" and "abcd".
	 */
	private static boolean matchString(String input, String pattern) {
		if (pattern.isEmpty())
			return true;
		String nextPattern = pattern.substring(1);
		char startingChar = pattern.charAt(0);
		for (int index = input.indexOf(startingChar); index != -1
				&& !input.isEmpty(); index = input.indexOf(startingChar)) {
			String nextInput = input.substring(index + 1);
			if (matchString(nextInput, nextPattern)) {
				return true;
			}
			input = nextInput;
		}
		return false;
	}

	private static boolean isSubString(String input, String search) {
		char[] inputChars = input.toCharArray();
		char[] patternChars = search.toCharArray();
		for (int i = 0; i < inputChars.length; i++) {
			int k = i;
			int j = 0;
			for (; j < patternChars.length; j++) {
				if (inputChars[k] != patternChars[j]) {
					break;
				}
			}
			if (j == patternChars.length)
				return true;
		}
		return false;
	}
	
	/* 
	 * Given a set T of characters and a string S, find the minimum window in S 
	 * which will contain all the characters in T in complexity O(n). eg, S = 
	 * “ADOBECODEBANC” T = “ABC” Minimum window is “BANC”.
	 * */
	private static String getMinimumWindow(String input, String target) {
		int[] needFind = new int[26];
		int[] found = new int[26];
		for (int i = 0; i < 26; i++) {
			needFind[i] = found[i] = 0;
		}
		for (char c : target.toCharArray()) {
			needFind[c - 'a'] += 1;
		}
		int minimumWindow = Integer.MAX_VALUE;
		int minStart = 0;
		int minEnd = 0;
		for (int end = 0, start = 0; end < input.length(); end++) {
			char c = input.charAt(end);
			int endIndex = c - 'a';
			if (needFind[endIndex] == 0) {
				continue;
			}
			found[endIndex] += 1;
			if (getNonZeroCount(needFind) == getNonZeroCount(found)) {
				while (needFind[input.charAt(start) - 'a'] == 0
						|| needFind[input.charAt(start) - 'a'] < found[input
								.charAt(start) - 'a']) {
					if(needFind[input.charAt(start) - 'a'] < found[input.
                           charAt(start) - 'a']) {
						found[input.charAt(start) - 'a'] --;
					}
					start++;
					
				}
				if (end - start + 1 < minimumWindow) {
					minimumWindow = end - start + 1;
					minStart = start;
					minEnd = end;
				}
			}
		}
		return input.substring(minStart, minEnd + 1);
	}

	private static int getNonZeroCount(int[] array) {
		int result = 0;
		for (int i : array) {
			if (i > 0)
				result++;
		}
		return result;
	}
	
	private static String getLongestSubstringWithoutDuplicates(String input) {
		int[] counts = new int[26];
		for(int i = 0; i< 26; i ++)
			counts[i] = 0;
		char[] chs = input.toCharArray();
		int longestStart = 0;
		int longestEnd = 0;
		int maxLength = Integer.MIN_VALUE;
		int start = 0;
		for(int end = 0; end < chs.length; end ++) {
			int index = chs[end] - 'a';
			counts[index] ++;
			if(counts[index] == 1) {
				continue;
			} 
			if(end - start > maxLength) {
					maxLength = end - start;
					longestStart = start;
					longestEnd = end - 1;
			}
			for(;chs[start] != chs[end]; start ++){
				counts[chs[start] - 'a'] --;
			}
			counts[index] --;
			start ++;
		}
		String remain = input.substring(start);
		String middle = input.substring(longestStart, longestEnd + 1);
		return remain.length() > middle.length() ? remain : middle;
	}

	

	public static void main(String[] args) {
/*		
		 * System.out.println(findTheLongestCommonString("ABABC", "BABCA"));
		 * System.out.println(findLongestPalindromic("abacdfgdcaba"));
		 * System.out.println(findLongestNonrepetitiveSubString("abdaabd"));
		 * System.out.println(testDuplicate("abb"));
		 * System.out.println(testDuplicate("abc"));
		 * System.out.println(reverseString("fadfasfd"));
		 * System.out.println(checkPermutation("dog", "god"));
		 * System.out.println(checkPermutation("dpg", "god"));
		 * System.out.println(replaceSpaces("dafd "));
		 * System.out.println(simpleCompress("aabbccc"));
		 * System.out.println(matchString("abccde", "ace"));
		 
		System.out.println(getMinimumWindow("ADOBECODEBANC".toLowerCase(),
				"ABC".toLowerCase()));*/
		System.out.println(getLongestSubstringWithoutDuplicates("dfeanfdouiv"));

	}

}
