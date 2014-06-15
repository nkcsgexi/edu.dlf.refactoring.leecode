package edu.dlf.refactoring.leecode;

public class BinaryProblems {

	private static int countOneBit(int input) {
		int count = 0;
		for(;input != 0; input = input >>> 1) {
			count += input & 1;
		}
		return count;		
	}
	
	private static int countZeroBit(int input) {
		return countOneBit(~input);
	}
	
	private static boolean testIfPow4(int input) {
		if(countOneBit(input) == 1) {
			int base = 0x55555555;
			return (base & input) != 0;
		}
		return false;
	}
	
	private static void xorIsNegate() {
		for(int i = 0; i < 1000; i ++) {
			if((i^-1) != ~i) {
				System.out.println("Not right");
				return;
			}
		}
		System.out.println("Right!");
	}
	
	private static int getBitAtPosition(int position, int input) {
		return (input >>> position) & 1;
	}
	
	private static int getIntLength() {
		int count = 0;
		for(int i = 3; i > 0;i = i << 1, count ++);
		return count + 2;
	}
	
	private static int reverseBits(int input) {
		int result = 0;
		for(int position = 0; position < 32; position ++) {
			if(getBitAtPosition(position, input) == 1) {
				result = result | (1 << (31 - position));
			}
		}
		return result;
	}
	
	private static void reserseBitsTest() {
		for(int i = -1; i < 1000; i ++) {
			String before = makeUp32bits(Integer.toBinaryString(i));
			String after = makeUp32bits(Integer.toBinaryString(reverseBits(i)));
			System.out.println("before:" + before);
			System.out.println("after:" + after);
		}
	}
	
	private static String makeUp32bits(String s) {
		while(s.length() < 32) {
			s = '0' + s;
		}
		return s;
	}
	
	private static void printIntegerInBinary(int num) {
		StringBuilder sb = new StringBuilder();
		while(num != 0) {
			sb.insert(0, num & 1);
			num = num >> 1;
		}
		System.out.println(sb.toString());
	}
	
	/*
	 * You are given two 32-bit numbers, N and M, and two bit positions, i and j. 
	 * Write a method to set all bits between i and j in N equal to M (e.g., M 
	 * becomes a substring of N located at i and starting at j). EXAMPLE: 
	 * Input: N = 10000000000, M = 10101, i = 2, j = 6 Output: N = 10001010100
	 * */
	private static int setBits(int M, int N, int i, int j ) {
		int mask = 1;
		for(int k = 0; k < j - i; k ++ ){
			mask = (mask << 1 | 1);
		}
		mask = mask << i;
		int setter = M << i;
		return (N & (~mask) | setter);
	}
	
	/* Given a (decimal - e.g. 3.72) number that is passed in as a string, print 
	 * the binary representation. If the number can not be represented accurately 
	 * in binary, print “ERROR"*/
	private static void printNumber(String s) {
		double remain = Double.parseDouble("0." + s.substring(s.indexOf('.') + 1));	
		StringBuilder sb = new StringBuilder();
		while(remain > 0) {
			if(remain >= 0.5) {
				sb.append(1);
				remain = (remain - 0.5) * 2;
			}
			else {
				sb.append(0);
				remain *= 2;
			}
		}
		if(remain < 0) {
			System.out.println("error");
			return;
		}
		System.out.println(sb.toString());
	}
	
	public static void main(String[] args) {
		//System.out.println(countOneBit(1));
		//System.out.println(countOneBit(13)); //1100
		xorIsNegate();
		System.out.println(Integer.toBinaryString(-1));
		System.out.println(countZeroBit(1));
		System.out.println(countZeroBit(13));
		System.out.println(testIfPow4(1));
		System.out.println(testIfPow4(4));
		System.out.println(testIfPow4(16));
		System.out.println(testIfPow4(8));
		System.out.println(testIfPow4(17));
		System.out.println(testIfPow4(256));
		reserseBitsTest();
		printIntegerInBinary(setBits(Integer.parseInt("10101", 2), Integer.
			parseInt("10000000000", 2), 2, 6));

		printNumber("0.75");
	}

	private static void printDoubleInBinary(double value) {
		System.out.println(Long.toBinaryString(Double.doubleToRawLongBits(value)));
	}
	
}
