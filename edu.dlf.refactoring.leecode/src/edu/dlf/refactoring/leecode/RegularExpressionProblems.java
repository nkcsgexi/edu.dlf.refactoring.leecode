package edu.dlf.refactoring.leecode;

public class RegularExpressionProblems {

	private static boolean isMatch(String pattern, String s) {
		if(pattern.length() == 0)
			return s.length() == 0;
		if(pattern.length() == 1) {
			if(s.length() != 1)
				return false;
			else
				return pattern.equals(s) || pattern.equals(".");
		}
		
		if(pattern.charAt(1) != '*') {
			return (pattern.charAt(0) == '.' || s.charAt(0) == pattern.charAt(0)) 
				&& isMatch(pattern.substring(1), s.substring(1));
		}
		String newPattern = pattern.substring(2);
		String news;
		for(news = s; news.length() != 0 && (news.charAt(0) == pattern.charAt(0) || 
				pattern.charAt(0) == '.'); news = news.substring(1)) {
			if(isMatch(newPattern, news)) {
				return true;
			}
		}
		return isMatch(newPattern, news);
	}
	
	public static void main(String args[]) {
		System.out.println(isMatch("a", "aa"));
		System.out.println(isMatch("aa", "aa"));
		System.out.println(isMatch("aa", "aaa"));
		System.out.println(isMatch("a*", "aa"));
		System.out.println(isMatch(".*", "aa"));
		System.out.println(isMatch(".*", "ab"));
		System.out.println(isMatch("a*b","aab"));
		System.out.println(isMatch("c*a*b","aab"));
	}
	
}
