package edu.dlf.refactoring.leecode;

public class BinaryProblems {

	private static int countOneBit(int input) {
		int count = 0;
		for (; input != 0; input = input >>> 1) {
			count += input & 1;
		}
		return count;
	}

	private static int countZeroBit(int input) {
		return countOneBit(~input);
	}

	private static boolean testIfPow4(int input) {
		if (countOneBit(input) == 1) {
			int base = 0x55555555;
			return (base & input) != 0;
		}
		return false;
	}

	private static boolean isPowTwo(int num) {
		return (num & (num - 1)) == 0;
	}

	private static void xorIsNegate() {
		for (int i = 0; i < 1000; i++) {
			if ((i ^ -1) != ~i) {
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
		for (int i = 3; i > 0; i = i << 1, count++)
			;
		return count + 2;
	}

	private static int reverseBits(int input) {
		int result = 0;
		for (int position = 0; position < 32; position++) {
			if (getBitAtPosition(position, input) == 1) {
				result = result | (1 << (31 - position));
			}
		}
		return result;
	}

	private static void reserseBitsTest() {
		for (int i = -1; i < 1000; i++) {
			String before = makeUp32bits(Integer.toBinaryString(i));
			String after = makeUp32bits(Integer.toBinaryString(reverseBits(i)));
			System.out.println("before:" + before);
			System.out.println("after:" + after);
		}
	}

	private static String makeUp32bits(String s) {
		while (s.length() < 32) {
			s = '0' + s;
		}
		return s;
	}

	private static void printIntegerInBinary(int num) {
		StringBuilder sb = new StringBuilder();
		while (num != 0) {
			sb.insert(0, num & 1);
			num = num >> 1;
		}
		System.out.println(sb.toString());
	}

	/*
	 * You are given two 32-bit numbers, N and M, and two bit positions, i and
	 * j. Write a method to set all bits between i and j in N equal to M (e.g.,
	 * M becomes a substring of N located at i and starting at j). EXAMPLE:
	 * Input: N = 10000000000, M = 10101, i = 2, j = 6 Output: N = 10001010100
	 */
	private static int setBits(int M, int N, int i, int j) {
		int mask = 1;
		for (int k = 0; k < j - i; k++) {
			mask = (mask << 1 | 1);
		}
		mask = mask << i;
		int setter = M << i;
		return (N & (~mask) | setter);
	}

	/*
	 * Given a (decimal - e.g. 3.72) number that is passed in as a string, print
	 * the binary representation. If the number can not be represented
	 * accurately in binary, print “ERROR"
	 */
	private static void printNumber(String s) {
		double remain = Double.parseDouble("0."
				+ s.substring(s.indexOf('.') + 1));
		StringBuilder sb = new StringBuilder();
		while (remain > 0) {
			if (remain >= 0.5) {
				sb.append(1);
				remain = (remain - 0.5) * 2;
			} else {
				sb.append(0);
				remain *= 2;
			}
		}
		if (remain < 0) {
			System.out.println("error");
			return;
		}
		System.out.println(sb.toString());
	}

	/*
	 * Given an integer, print the next smallest and next largest number that
	 * have the same number of 1 bits in their binary representation
	 */
	private static void printNextNumbers(final int num) {
		int[] index = findFirstZeroWithTailingOnes(num);
		int nextSmallest = setMask(num, index[0], index[1]);
		System.out.println(Integer.toBinaryString(num));
		System.out.println(Integer.toBinaryString(nextSmallest));
		index = findFirstOneWithTailingZero(num);
		int nextLargest = setMask(num, index[1], index[0]);
		System.out.println(Integer.toBinaryString(nextLargest));
	}

	private static int[] findFirstZeroWithTailingOnes(int num) {
		boolean hasOne = false;
		int oneIndex = 0;
		int i = 0;
		while (num != 0) {
			if ((num & 1) == 1) {
				hasOne = true;
				oneIndex = i;
			} else if (hasOne) {
				return new int[] { i, oneIndex };
			}
			num = num >> 1;
			i++;
		}
		return new int[] { i, i - 1 };
	}

	private static int[] findFirstOneWithTailingZero(int num) {
		boolean hasZero = false;
		int zeroIndex = 0;
		int i = 0;
		while (num != 0) {
			if ((num & 1) == 0) {
				hasZero = true;
				zeroIndex = i;
			} else if (hasZero) {
				return new int[] { i, zeroIndex };
			}
			num = num >> 1;
			i++;
		}
		return null;
	}

	private static int setMask(int num, int zero2One, int one2Zero) {
		int mask = 1 << zero2One;
		num = num | mask;
		mask = ~(1 << one2Zero);
		num = num & mask;
		return num;
	}

	/*
	 * Write a function to determine the number of bits required to convert
	 * integer A to integer B. Input: 31, 14 Output: 2
	 */
	private static int getConvertBit(int num1, int num2) {
		int count = 0;
		for (int i = num1 ^ num2; i != 0; i = i >> 1) {
			if ((i & 1) == 1) {
				count++;
			}
		}
		return count;
	}

	/*
	 * Write a program to swap odd and even bits in an integer with as few
	 * instructions as possible (e.g., bit 0 and bit 1 are swapped, bit 2 and
	 * bit 3 are swapped, etc).
	 */
	private static int swap(int num) {
		return ((0x55555555 & num) << 1) | ((0xaaaaaaaa & num) >> 1);
	}

	/*
	 * An array A[1...n] contains all the integers from 0 to n except for one
	 * number which is missing. In this problem, we cannot access an entire
	 * integer in A with a single operation. The elements of A are represented
	 * in binary, and the only operation we can use to access them is �fetch
	 * the jth bit of A[i]�, which takes constant time. Write code to find the
	 * missing integer. Can you do it in O(n) time?
	 */
	private static int getMissingNumber(int n) {
		int highest, num;
		for (highest = 0, num = n; num != 0; num = num >> 1, highest++)
			;
		int result = 0;
		for (int bit = 0; bit <= highest; bit++) {
			if (getCountOneOneBitAtTheList(n, bit) != getCountOfOneAtBit(n + 1,
					bit)) {
				result += 1 << bit;
			}
		}
		return result;
	}

	private static int getCountOfOneAtBit(int n, int bit) {
		int base = (1 << bit);
		int count = 0;
		boolean flagZero = true;
		while (n - base >= 0) {
			if (!flagZero)
				count += base;
			flagZero = !flagZero;
			n -= base;
		}
		if (!flagZero)
			count += n;
		return count;
	}

	private static int getCountOneOneBitAtTheList(int n, int bit) {
		int count = 0;
		for (int i = 0; i < n; i++) {
			if (getJBitOfNumI(i, bit) == 1)
				count++;
		}
		return count;
	}

	private static int getJBitOfNumI(int i, int j) {
		int[] numbers = new int[] { 1, 3, 2, 4, 5, 7, 0 };
		int num = numbers[i];
		return (num >> j) & 1;
	}

	/* Implement a ? b : c without using any conditions. */
	private static int implementCondition(boolean a, int b, int c) {
		int[] pool = new int[] { b, c };
		int index = Boolean.toString(a).indexOf("false") + 1;
		return pool[index];
	}

	private static int[] conver2GrayBinary(int[] digits) {
		int num = 0;
		for (int i = digits.length - 1, base = 1; i >= 0; i--, base *= 2) {
			num += digits[i] * base;
		}
		int[] newDs = new int[digits.length];
		for (int i = newDs.length - 1, unitLength = 4; i >= 0; i--, unitLength *= 2) {
			int position = num % unitLength;
			if (position >= unitLength / 4 && position < unitLength / 4 * 3)
				newDs[i] = 1;
			else
				newDs[i] = 0;
		}
		return newDs;
	}

	private static void testGray() {
		printDigits(conver2GrayBinary(new int[] { 0, 0, 0, 0 }));
		printDigits(conver2GrayBinary(new int[] { 0, 0, 0, 1 }));
		printDigits(conver2GrayBinary(new int[] { 0, 0, 1, 0 }));
		printDigits(conver2GrayBinary(new int[] { 0, 0, 1, 1 }));
		printDigits(conver2GrayBinary(new int[] { 0, 1, 0, 0 }));
		printDigits(conver2GrayBinary(new int[] { 0, 1, 0, 1 }));
		printDigits(conver2GrayBinary(new int[] { 0, 1, 1, 0 }));
		printDigits(conver2GrayBinary(new int[] { 0, 1, 1, 1 }));
		printDigits(conver2GrayBinary(new int[] { 1, 0, 0, 0 }));
		printDigits(conver2GrayBinary(new int[] { 1, 0, 0, 1 }));
		printDigits(conver2GrayBinary(new int[] { 1, 0, 1, 0 }));
		printDigits(conver2GrayBinary(new int[] { 1, 0, 1, 1 }));
		printDigits(conver2GrayBinary(new int[] { 1, 1, 0, 0 }));
		printDigits(conver2GrayBinary(new int[] { 1, 1, 0, 1 }));
		printDigits(conver2GrayBinary(new int[] { 1, 1, 1, 0 }));
		printDigits(conver2GrayBinary(new int[] { 1, 1, 1, 1 }));
	}

	private static void printDigits(int[] digits) {
		for (int d : digits)
			System.out.print(d);
		System.out.println();
	}

	private static int fetchBit(int i, int j) {
		return 1;
	}

	private static int getNumberOfZeros(int count, int pos) {
		int circle = 1 << (pos + 1);
		int first = (count / circle) * (circle >> 1);
		int second = (count % circle) > (circle >> 1) ? circle >> 1 : count
				% circle;
		return first + second;
	}

	private static int getMissingValue(int n) {
		int result = 0;
		for (int position = 0; n >> position != 0; position++) {
			int numberOfOnes = (n + 1) - getNumberOfZeros(n + 1, position);
			int currentOnes = 0;
			for (int i = 0; i < n; i++) {
				if (fetchBit(i, position) == 1)
					currentOnes++;
			}
			if (numberOfOnes == currentOnes)
				result = result << 1;
			else
				result = result << 1 | 1;
		}
		return result;
	}

	public static void main(String[] args) {
		testGray();
	}

	private static void printDoubleInBinary(double value) {
		System.out.println(Long.toBinaryString(Double
				.doubleToRawLongBits(value)));

	}

}
