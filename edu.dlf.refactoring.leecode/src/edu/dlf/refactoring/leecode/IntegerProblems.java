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
	
	
	
	
	
	public static void main(String args[]) {
		System.out.println(isPalindrome(0));
		System.out.println(isPalindrome(101));
		System.out.println(isPalindrome(101101));
	}
}
