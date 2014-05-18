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
		for(int i = 0; i< 1000; i ++) {
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
	}
	
}
