package edu.dlf.refactoring.leecode;


public class StringProblems {

	private static String findTheLongestCommonString(String s1, String s2) {
		int matrix[][] = new int[s1.length()][s2.length()];
		for(int row = 0; row < s1.length(); row ++) {
			matrix[row][0] = s1.charAt(row) == s2.charAt(0) ? 1 : 0;	
		}
		for(int column = 0; column < s2.length(); column ++) {
			matrix[0][column] = s1.charAt(0) == s2.charAt(column) ? 1 : 0;
		}
		int maxLength = 0;
		int maxRow = 0;
		for(int row = 1 ; row < s1.length(); row ++){
			for(int column = 1; column < s2.length(); column ++) {
				if(s1.charAt(row) == s2.charAt(column)) {
					matrix[row][column] = matrix[row-1][column-1]+1;
					if(matrix[row][column] > maxLength) {
						maxLength = matrix[row][column];
						maxRow = row;
					}
				} else {
					matrix[row][column] = 0;
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for(;maxLength > 0;maxLength --, maxRow --) {
			sb.append(s1.charAt(maxRow));
		}
		return sb.reverse().toString();
	}
	
	
	private static String findLongestPalindromic(String s) {
		boolean matrix[][] = new boolean[s.length()][s.length()];
		for(int i = 0; i < s.length(); i ++) {
			matrix[i][i] = true;
		}
		for(int i = 0; i < s.length() - 1; i ++) {
			matrix[i][i+1] = s.charAt(i) == s.charAt(i + 1);
		}
		int maxLength = 1;
		String maxString = s.substring(s.length() - 1);
		
		for(int count = 2; count < s.length(); count ++) {
			for(int i = 0; i < s.length() - count; i ++) {
				int j = count + i;
				matrix[i][j] = matrix[i+1][j-1] && s.charAt(i) == s.charAt(j);
				if(matrix[i][j] && j - i + 1 > maxLength) {
					maxLength = j - i + 1;
					maxString = s.substring(i, j + 1);
				}
			}
		}
		return maxString;
	}
	
	private static String findLongestNonrepetitiveSubString(String input) {
		int[] charCounts = new int[26];
		for(int i = 0; i < 26; i ++) {
			charCounts[i] = 0;
		}
		int max = Integer.MIN_VALUE;
		int maxStart = 0;
		int maxEnd = 0;
		for(int start = 0, end = 0; end < input.length(); end ++) {
			char c = input.charAt(end);
			charCounts[c - 'a'] ++;
			if(charCounts[c - 'a'] == 1) continue;
			while(charCounts[c - 'a'] != 1) {
				charCounts[input.charAt(start) - 'a'] --;
				start ++;
			}
			if(max < end - start + 1) {
				max = end - start + 1;
				maxStart = start;
				maxEnd = end;
			}
		}
		return input.substring(maxStart, maxEnd + 1);
	}
	
	/* Determine if a string consists of duplicate letters.*/
	private static boolean testDuplicate(String s) {
		char[] chs = s.toCharArray();
		int flag = 0;
		for(char c : chs) {
			int position = c - 'a';
			int mask = 1 << position;
			if(((flag >> position) % 2) != 0)
				return true;
			flag = flag | mask;
		}
		return false;
	}
	
	private static String reverseString(String s) {
		char[] chars = s.toCharArray();
		for(int start = 0, end = chars.length -1; start < end; start ++, end --) {
			char tmp = chars[start];
			chars[start] = chars[end];
			chars[end] = tmp;
		}
		return String.copyValueOf(chars);
	}
	
	/*Determine if one string is the permutation of another string.*/
	private static boolean checkPermutation(String s1, String s2) {
		int[] letters = new int[26];
		for(char c : s1.toCharArray()) {
			letters[c - 'a'] ++;
		}
		for(char c : s2.toCharArray()) {
			letters[c - 'a']--;
		}
		for(int count : letters) {
			if(count != 0)
				return false;
		}
		return true;
	}
	
	/*Replace all occurrences of space to 02%*/
	private static String replaceSpaces(String input) {
		StringBuilder sb = new StringBuilder();
		for(char c : input.toCharArray()) {
			if(c == ' ') {
				sb.append("02%");
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	/*Simple compression algorithm. change aaabbc to a3b2c1.*/
	private static String simpleCompress(String input) {
		StringBuilder sb = new StringBuilder();
		for(int pointer = 0; pointer < input.length();) {
			char current = input.charAt(pointer);
			sb.append(current);
			int count = 1;
			pointer ++;
			while(pointer < input.length() && input.charAt(pointer) == 
					current) {
				count ++;
				pointer ++;
			}
			sb.append(count);
		}
		return sb.toString();
	}
	
	/* Rotate an image by 90 degree to the right. */
	private static int[][] rotateImage(int[][] matrix, int height) {
		for(int layer = 0; layer < (height + 1)/2; layer ++) {
			int offset = height-layer-1;
			int tmp = matrix[layer][layer];
			matrix[layer][layer] = matrix[offset][layer];
			matrix[offset][layer] = matrix[offset][offset];
			matrix[offset][offset] = matrix[layer][offset];
			matrix[layer][offset] = tmp;
			
			int copyLength = offset - layer - 1;
			int[] tempArray = new int[copyLength];
			for(int i = 0; i < copyLength; i++) {
				tempArray[i] = matrix[layer+i+1][layer]; 
			}
			for(int i = 0; i < copyLength; i++) {
				matrix[layer+i+1][layer] = matrix[offset][layer+1+i]; 
			}
			for(int i = 0; i < copyLength; i++) {
				matrix[offset][layer+1+i] = matrix[offset-1-i][offset]; 
			}
			for(int i = 0; i< copyLength; i++) {
				matrix[offset-1-i][offset] = matrix[layer][offset-i-1];
			}
			for(int i = 0; i < copyLength; i++) {
				matrix[layer][layer+1+i] = tempArray[copyLength-i-1];
			}	
		}
		return matrix;
	}
	
	public static void main(String[] args) {
		System.out.println(findTheLongestCommonString("ABABC", "BABCA"));
		System.out.println(findLongestPalindromic("abacdfgdcaba"));
		System.out.println(findLongestNonrepetitiveSubString("abdaabd"));
		System.out.println(testDuplicate("abb"));
		System.out.println(testDuplicate("abc"));
		System.out.println(reverseString("fadfasfd"));
		System.out.println(checkPermutation("dog", "god"));
		System.out.println(checkPermutation("dpg", "god"));
		System.out.println(replaceSpaces("dafd "));
		System.out.println(simpleCompress("aabbccc"));
	}
	
	
}
