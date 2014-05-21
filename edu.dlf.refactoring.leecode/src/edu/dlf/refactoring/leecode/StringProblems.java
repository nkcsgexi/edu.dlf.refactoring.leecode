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
	
	
	
	public static void main(String[] args) {
		System.out.println(findTheLongestCommonString("ABABC", "BABCA"));
		System.out.println(findLongestPalindromic("abacdfgdcaba"));
		System.out.println(findLongestNonrepetitiveSubString("abdaabd"));
	}
	
	
}
