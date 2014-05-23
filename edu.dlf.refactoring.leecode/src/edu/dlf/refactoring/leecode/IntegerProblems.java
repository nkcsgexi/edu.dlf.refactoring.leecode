package edu.dlf.refactoring.leecode;

public class IntegerProblems {

	private static int getDigitAt(int num, int pos) {
		int base = 1;
		for(int i = 0; i < pos; i ++, base *= 10);
		return (num/base) % 10;
	}
	
	private static boolean determinePalindromeRecursive(int num, int high, int low) {
		if(low == high) 
			return true;
		if(low > high)
			return false;
		if(low == high - 1 && getDigitAt(num, high) == getDigitAt(num, low))
			return true;
		if(getDigitAt(num, high) == getDigitAt(num, low)) 
			return determinePalindromeRecursive(num, high - 1, low + 1);
		else 
			return false;
	}
	
	private static boolean determinePalindrome(int num) {
		if(num < 0)
			return false;
		int high, base;
		for(high = 0, base = 10; num/base !=0 ;high ++, base *= 10);
		if(high == 0)
			return true;
		return determinePalindromeRecursive(num, high, 0);
	}
	
	// this is the optimum answer
	private static boolean isPalindrome(int num) {
		if(num < 0)
			return false;
		int div = 1; 
		while(num/div >= 10) {
			div *= 10;
		}
		while(num != 0) {
			int h = num / div;
			int l = num % 10;
			if(h != l)
				return false;
			num = (num % div)/10;
			div = div/100;
		}
		return true;
	}
	
	/*
		Given an array of non-negative integers A and a positive integer k, 
		we want to: Divide A into k or fewer partitions,
		such that the maximum sum over all the partitions is minimized.
	*/
	private static int decideSchedule(int[] tasks, int workers) {
		int[][] matrix = new int[workers][tasks.length];
		for(int i = 0; i < workers; i ++) {
			matrix[i][0] = tasks[0];
		}
		for(int i = 1; i < tasks.length; i ++) {
			matrix[0][i] = matrix[0][i - 1] + tasks[i];
		}
		
		for(int i = 1; i < workers; i++) {
			for(int j = 1; j < tasks.length; j ++) {
				int best = Integer.MAX_VALUE;
				for(int middle = 0; middle < j; middle ++) {
					best = Math.min(best, Math.max(matrix[i - 1][middle], 
						(matrix[0][j] - matrix[0][middle])));
				}
				matrix[i][j] = best;
			}
		}
		return matrix[workers - 1][tasks.length - 1];
	}
	
	public static void main(String args[]) {
		System.out.println(isPalindrome(0));
		System.out.println(isPalindrome(101));
		System.out.println(isPalindrome(101101));
	}
}
