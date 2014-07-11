package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

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
	
	private static int getIntFromDigits(List<Integer> digit) {
		int result = 0;
		int base = 1;
		for(int i = digit.size() - 1; i >= 0; i --) {
			result += digit.get(i) * base;
			base *= 10;
		}
		return result;
	}

	private static int getLeastNumberAfterRemovingKDigit(int number, int k) {
		if(k == 0)
			return number;
		List<Integer> digitList = new ArrayList<Integer>();
		while(number != 0) {
			int digit = number % 10;
			number = number / 10;
			digitList.add(0, digit);
		}
		int selectedPosition = -1;
		for(int i = 0; i < digitList.size() - 1; i ++) {
			if(digitList.get(i) > digitList.get(i + 1)) {
				selectedPosition = i;
				break;	
			}
		}
		if(selectedPosition == -1) selectedPosition = digitList.size() - 1;
		digitList.remove(selectedPosition);
		return getLeastNumberAfterRemovingKDigit(getIntFromDigits
			(digitList), k - 1);
	}
 	
	private static List<String> getAllPermutations(
			String input) {
		List<String> results = new ArrayList<String>();
		if(input.length() == 1 || input.length() == 0) {
			results.add(input);
			return results;
		}
		String head = input.substring(0, 1);
		String remain = input.substring(1);
		for(String s : getAllPermutations(remain)) {
			for(int i = 0; i <= s.length(); i ++) {
				String prefix = s.substring(0, i);
				String postfix = s.substring(i);
				results.add(prefix + head + postfix); 
			}
		}
		return results;
	}
	private static void printAllPermutation(String input) {
		List<String> list = getAllPermutations(input);
		Collections.sort(list);
		for(String s : list) {
			System.out.println(s);
		}
	}

	private static boolean ifPermutationExist(String s1, String s2) {
		int[] counts = new int[26];
		int[] actualCounts = new int[26];
		int length = s1.length();
		
		for(int i = 0; i < 26; i ++) {
			counts[i] = 0;
			actualCounts[i] = 0;	
		}
		for(char c : s1.toCharArray()) {
			counts[c - 'a'] ++;
		}	
		for(int start = 0; start < s2.length() - length + 1; start ++) {
			if(start == 0) {
				String sub = s2.substring(start, length);
				for(char c : sub.toCharArray()) {
					actualCounts[c - 'a'] ++;
				}
				if(checkArrayEq(counts, actualCounts))
					return true;
			
			}else {
				actualCounts[s2.charAt(start + length - 1) - 'a'] ++;
				actualCounts[s2.charAt(start - 1) - 'a'] --;
				if(checkArrayEq(actualCounts, counts))
					return true;
			}			
		}
		return false;
	}

	private static boolean checkArrayEq(int[] array1, int[] array2) {
		if(array1.length != array2.length)
			return false;
		int index = 0;
		for(;index < array1.length; index++) {
			if(array1[index] != array2[index]) {
				break;
			}
		}
		if(index == array1.length)
			return true;
		else 
			return false;
	}

	/* http://www.careercup.com/question?id=19300678 */
	private static List<String> getAllLetters(String s) {
		List<String> results = new ArrayList<String>();
		if(s.length() == 0) {
			results.add("");
			return results;
		}
		char c = convertInt2char(s.substring(0, 1));
		List<String> remains = getAllLetters(s.substring(1));
		for(String r : remains) {
			results.add(c + r);
		}
		if(s.length() > 1 && checkIntegerBound(s.substring(0, 2))) {
			c = convertInt2char(s.substring(0, 2));
			remains = getAllLetters(s.substring(2));
			for(String r : remains)
				results.add(c + r);
		}
		return results;
	}

	private static char convertInt2char(String num) {
		int i = Integer.parseInt(num);
		return (char)(i + 'a' -1);
	}
	private static boolean checkIntegerBound(String num) {
		int i = Integer.parseInt(num);
		return i > 0 && i < 27;
	}

	private static boolean determineInterleaving(String s1, String s2, 
			String target) {
		boolean match1 = false;
		boolean match2 = false;
		if(target.length() == 0)
			return true;
		if(s1.length() > 0 && target.charAt(0) == s1.charAt(0))
		{	
			match1 = determineInterleaving(s1.substring(1), s2,
				target.substring(1));
			if(match1)
				return true;
		}
		if(s2.length() > 0 && target.charAt(0) == s2.charAt(0)) {
			match2 = determineInterleaving(s1, s2.substring(1),
				target.substring(1));
			if(match2)
				return match2;
		}
		return false;
	}
	
	private static boolean simpleRegularExpressionMatching(String pattern, 
			String match) {
		if(pattern.length() == 0 && match.length() == 0)
			return true;
		if(pattern.length() == 0 || match.length() == 0)
			return false;
		if(pattern.charAt(pattern.length()-1) != '*')
			return pattern.charAt(pattern.length() - 1) == match.charAt
				(match.length()-1) && simpleRegularExpressionMatching
					(pattern.substring(0, pattern.length() - 1),
					 	match.substring(0, match.length() - 1));
		char expected = pattern.charAt(pattern.length() - 2);
		String newPattern = pattern.substring(0, pattern.length() - 2);
		int end = match.length() - 1;
		for(; match.charAt(end) == expected;end --) {
			String newMatch = match.substring(0, end);
			if(simpleRegularExpressionMatching(newPattern, newMatch))
				return true;
		}
		String newMatch = match.substring(0, end + 1);
		return simpleRegularExpressionMatching(newPattern, newMatch);
	}
	
	public static class WordMember implements Comparable<WordMember> {
		String self;
		String appendix;
		public WordMember(String self, String appendix) {
			this.self = self;
			this.appendix = appendix;
		}
		
		@Override
		public int compareTo(WordMember other) {
			return (self + appendix).compareTo(other.self + other.appendix);
		}
	}

	
	private static String combineStringsToMinimum(List<String> words) {
		Collections.sort(words);
		List<WordMember> members = new ArrayList<WordMember>();
		for(int i = 0; i < words.size() - 1; i ++) {
			String current = words.get(i);
			String next = words.get(i + 1);
			if(!next.startsWith(current)) {
				members.add(new WordMember(current, ""));
			}else {
				members.add(new WordMember(current, next));
			}
		}
		members.add(new WordMember(words.get(words.size()-1), ""));
		Collections.sort(members);
		StringBuilder sb = new StringBuilder();
		for(WordMember m : members) {
			sb.append(m.self);
		}
		return sb.toString();
	}
	private static void testCombineStrings() {
		List<String> list = new ArrayList<String>();
		list.add("jibw");
		list.add("ji");
		list.add("jp");
		list.add("bw");
		list.add("jibw");
		System.out.println(combineStringsToMinimum(list));
	}
	private static boolean checkNonZeroPosition(int[] a, int[] b) {
		if(a.length != b.length) return false;
		for(int i = 0; i < a.length; i ++) {
			if(a[i] == 0 && b[i] == 0)
				continue;
			if(a[i] != 0 && b[i] != 0)
				continue;
			return false;
		}
		return true;
	}

	private static String getMinimumSubstringContainsAllChars(String s) {
		int[] current = new int[26];
		int[] required = new int[26];
		for(char c : s.toCharArray()) {
			required[c - 'a'] = 1;
		}
		int maxLength = Integer.MIN_VALUE;
		int maxStart = 0;
		for(int start = 0, end = 0; end < s.length(); end ++) {
			current[s.charAt(end) - 'a'] ++;
			boolean found = false;
			for(; checkNonZeroPosition(current, required); start++) {
				current[s.charAt(start) - 'a'] --;
				found = true;
			}
			if(found && end - start > maxLength) {
				maxLength = end - start + 2 ;
				maxStart = start - 1;
			}
		}
		return s.substring(maxStart, maxStart + maxLength);
	}
	
	private static void testMinimumSubstringContainsAllCharacters() {
		System.out.println(getMinimumSubstringContainsAllChars("ADOBECODEBANC".
			toLowerCase()));
	}

	private static boolean internalMatch(char[] s, char[] p, int fast) {	
		for(int offset = 0; offset < p.length; offset ++) {
			if(fast + offset >= s.length || s[fast + offset] != p[offset])
				return false;
		}
		return true;
	}

	private static String replaceStringPattern(char[] s, char[] p, String r) {
		StringBuilder sb = new StringBuilder();
		for(int fast = 0, slow = 0; slow != s.length; ) {
			boolean matched = false;
			while(internalMatch(s, p, fast)) { 
				fast = fast + p.length;
				slow = fast;
				matched = true;
			}
			if(matched) {
				sb.append(r);
			}else {
				sb.append(s[slow]);
				slow ++;
				fast ++;
			}
		}
		return sb.toString();
	}

	private static void internalTestReplacingString(String s, String p) {
		System.out.println(replaceStringPattern(s.toCharArray(),
			p.toCharArray(), "X"));
	}

	private static void testReplacingString() {
		internalTestReplacingString("aabbaabbaaabbbaaabc", "aaabb");
		internalTestReplacingString("abcdeffdfegabcabcab", "abc");
		
	}
	
	private static class CharCount implements Comparable<CharCount>{
		char c;
		int count;
		@Override
		public int compareTo(CharCount other) {
			return Character.compare(other.c, c);
		}
	}

	private static String rearrangeCharacters(String s, int d) {
		char[] chars = s.toCharArray();
		PriorityQueue<CharCount> queue = new PriorityQueue<CharCount>();
		HashMap<Character, CharCount> map = new HashMap<Character, CharCount>();
		for(char c : chars) {
			CharCount current = null;
			if(map.containsKey(c)) 
				current = map.get(c);
			else {
				current = new CharCount();
				current.c = c;
				current.count = 0;
				map.put(c, current);
			}
			current.count += 1;
		}
		for(CharCount c : map.values()) {
			queue.add(c);
		}
		StringBuilder sb = new StringBuilder();
		Queue<Character> forbiddenList = new LinkedList<Character>();
		List<CharCount> removed = new ArrayList<CharCount>();
		while(queue.size() != 0) {
			removed.clear();
			while(queue.size() != 0 && forbiddenList.contains(queue.peek().c)) {
					removed.add(queue.remove());	
			}
			if(queue.size() == 0) {
				return "";
			}else {
				sb.append(queue.peek().c);
				forbiddenList.add(queue.peek().c);
				queue.peek().count -= 1;
				if(queue.peek().count == 0)
					queue.remove();
				queue.addAll(removed);	
			}
			while(forbiddenList.size() >= d)
				forbiddenList.remove();
		}
		return sb.toString();
	}
	
	private static void testRearrangeChars() {
		System.out.println(rearrangeCharacters("abb", 2));
	}
			
	private static String rotateString(String s, int k) {
		s = reverseString(s);
		String first = smartReverseString(s.substring(0, k));
		String second = smartReverseString(s.substring(k));
		return first + second;
	}		
			
	private static String smartReverseString(String st) {
		char[] s = st.toCharArray();
		for(int i = 0; i < s.length/2; i ++){
			int j = s.length - 1 -i;
			s[i] = (char) (s[i] ^ s[j]);
			s[j] = (char) (s[i] ^ s[j]);
			s[i] = (char) (s[i] ^ s[j]);
		}
		return String.valueOf(s);
	}

	private static void testRotateString() {
		String s = "abcdefghijk";
		System.out.println(rotateString(s, 3));
	}

	private static List<Integer> LZWEncoding(String input) {
		List<Integer> results = new ArrayList<Integer>();
		HashMap<String, Integer> dict = new HashMap<String, Integer>();
		dict.put("#", 0);
		for(char c = 'a'; c <= 'z'; c++)
			dict.put(Character.toString(c), c - 'a' + 1);
		int end = input.length();
		int nextValue = 27;
		int start = 0;
		while(input.charAt(start) != '#') {
			int next = 0;
			for(int l = 1; l + start <= end; l ++) {
				String sub = input.substring(start, start + l);	
				if(!dict.containsKey(sub)) {
					dict.put(sub, nextValue);
					nextValue ++;
					start = l + start - 1;
					break;
				} else{
					next = dict.get(sub);
				}
			}
			results.add(next);
		}
		results.add(0);
		return results;
	}

	private static String LZWDecoding(List<Integer> code) {
		StringBuilder sb = new StringBuilder();
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(0, "#");
		for(char c = 'a'; c <= 'z'; c ++)
			map.put(c - 'a' + 1, Character.toString(c));
		int nextKey = 27;
		String prefix = null;
		for(int start = 0; code.get(start) != 0; start++) {
			String value = map.get(code.get(start));
			sb.append(value);
			if(prefix != null) {
				map.put(nextKey ++, prefix + value.charAt(0));
				prefix = value;
			}else
				prefix = value;
		}
		sb.append("#");
		return sb.toString();
	}

	private static void testLZWEncoding() {
		String o = "TOBEORNOTTOBEORTOBEORNOT#".toLowerCase();
		List<Integer> codes = LZWEncoding(o);
		for(int c : codes) {
			System.out.print(c + " ");
		}
		System.out.println();
		String s = LZWDecoding(codes);
		System.out.println(o);
		System.out.println(s);
	}


	public static void main(String[] args) {
		//testMinimumSubstringContainsAllCharacters();	
		testLZWEncoding();
	}
}
