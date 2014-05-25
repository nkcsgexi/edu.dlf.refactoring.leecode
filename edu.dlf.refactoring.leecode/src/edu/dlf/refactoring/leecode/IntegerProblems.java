package edu.dlf.refactoring.leecode;

public class IntegerProblems {

	private static int getDigitAt(int num, int pos) {
		int base = 1;
		for(int i = 0; i < pos; i ++, base *= 10);
		return (num/base) % 10;
	}
	
	private static boolean determinePalindromeRecursive(int num, int high, 
			int low) {
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
	
	/*
	 * There are two sorted arrays A and B of size m and n respectively. 
	 * Find the median of the two sorted arrays. The overall run time complexity 
	 * should be O(log (m+n)).
	 * */
	private static double getTheMedianOfTwoSortedArrays(int[] input1, int[] input2) {
		if(input1.length == 0)
			return getMedian(input2);
		else if(input2.length == 0)
			return getMedian(input1);
		
		if(input1.length == 1 || input2.length == 1) {
			int num = input1.length == 1? input1[0] : input2[0];
			int[] input = input1.length == 1? input2 : input1;
			return calculateSpecialCase(num, input);
		}
		
		if(compareMedian(input1, input2) == 0) {
			return getMedian(input1);
		}
		int trim = Math.min(input1.length/2, input2.length/2);
		int[] trimEnd = compareMedian(input1, input2) > 0 ? input1 : input2;
		int[] trimStart = trimEnd == input1 ? input2 : input1;
		trimEnd = getSubarray(trimEnd, 0, trimEnd.length - trim);
		trimStart = getSubarray(trimStart, trim, trimStart.length);
		return getTheMedianOfTwoSortedArrays(trimEnd, trimStart);
	}
	
	private static double calculateSpecialCase(int num, int[] input) {
		int[] newInput = new int[input.length +1];
		boolean used = false;
		for(int i = 0; i < newInput.length; i ++) {
			if(input[i] < num)
				newInput[i] = input[i];
			if(input[i]>= num && used == false) {
				used = true;
				newInput[i] = num;
				newInput[i + 1] = input[i];
				i ++;
			}
		}
		return getMedian(newInput);
	}
	
	private static int getIndex(int num, int[] input, int start, int end) {
		if(start == end)
			return start;
		int median = (end + start)/2;
		if(input[median] == num)
			return median;
		if(input[median] > num) {
			return getIndex(num, input, start, median - 1);
		} else
			return getIndex(num, input, median + 1, end);
	}

	private static int[] getSubarray(int[] input, int start, int end) {
		int[] results = new int[end - start];
		for(int i = 0; i < results.length; i ++)
			results[i] = input[start + i];
		return results;
	}
	
	private static double getMedian(int[] input) {
		return getMedian2(input) / 2.0;
	}

	private static int compareMedian(int[] input1, int[] input2) {
		return getMedian2(input1) - getMedian2(input2);
	}

	private static int getMedian2(int[] input) {
		return input.length % 2 == 1 ? input[input.length/2] * 2 : 
			input[input.length/2 - 1] + input[input.length/2];
	}

	/*
	 * Given two sorted arrays A, B of size m and n respectively. Find the k-th 
	 * smallest element in the union of A and B. You can assume that there are 
	 * no duplicate elements.
	 * */
	private static int getKthSmallestNumber(int[] m, int[] n, int k) {
		if(m.length == 0) 
			return n[k - 1];
		if(n.length == 0)
			return m[k - 1];
		if(k == 1) {
			return Math.min(m[0], n[0]);
		}
		int middleM = m[m.length/2];
		int position = - binarySearch(n, middleM, 0, n.length - 1);
		int split;
		if(n[position] < middleM) {
			position ++;
		}	
		split = m.length/2 + position + 1;
		if(split == k)
			return m[m.length/2];
		if(split < k)
			return getKthSmallestNumber(getSubarray(m, m.length/2 + 1, m.length), 
				getSubarray(n, position, n.length), k - split);
		else 
			return getKthSmallestNumber(getSubarray(m, 0, m.length/2), 
				getSubarray(n, 0, position), k); 
	}
	
	private static int binarySearch(int[] input, int target, int start, int end) {
		if(start == end) {
			return input[start] == target ? start : - start;
		}
		int middle = (start + end)/2;
		if(target == input[middle]) return middle;
		if(target > input[middle]) {
			return binarySearch(input, target, middle + 1, end);
		} else {
			return binarySearch(input, target, start, middle - 1);
		}
	}
	
	/*
	 * A long array A[] is given to you. There is a sliding window of size w 
	 * which is moving from the very left of the array to the very right. You 
	 * can only see the w numbers in the window. Each time the sliding window 
	 * moves rightwards by one position.
	 * */
	public static int[] getMaximumInSlidingWindow(int[] input, int window) {
		int[] results = new int[input.length - window + 1];
		DoubleQueue queue = new DoubleQueue(window);
		for(int i = 0; i < window; i ++) {
			while(!queue.empty() && input[queue.getTail()] < input[i] ) {
				queue.removeTail();
			}
			queue.insertTail(i);
		}
		for(int end = window; end < input.length; end ++) {
			results[end - window] = input[queue.getHead()];
			while(!queue.empty() && input[queue.getTail()] < input[end]) {
				queue.removeTail();
			}
			queue.insertTail(end);
			while(!queue.empty() && queue.getHead() <= end - window)
				queue.removeHead();
		}
		results[input.length - window] = input[queue.getHead()];
		return results;
	}
	
	private static class DoubleQueue{
		
		private final int[] element;
		private int head;
		private int tail;

		public DoubleQueue(int size) {
			this.element = new int[size];
			this.head = 0;
			this.tail = 0;
		}		
		public boolean empty() {
			return size() == 0;
		}
		public int size() {
			return tail >= head ? tail - head : tail + (element.length - head);
		}		
		public int getHead() {
			assert size() > 0;
			return element[head];
		}		
		public int getTail() {
			assert size() > 0;
			return element[tail - 1];
		}		
		public void insertHead(int value) {
			assert size() < element.length;
			int index = head > 0 ? head - 1 : element.length - 1;
			element[index] = value;
			this.head = index;
		}
		public void insertTail(int value) {
			assert size() < element.length;
			int index = tail < element.length ? tail : 0;
			element[index] = value;
			this.tail = index + 1;
		}
		public void removeHead() {
			assert size() > 0;
			this.head = (this.head + 1) % element.length;
		}
		public void removeTail() {
			assert size() > 0;
			this.tail = tail > 0 ? tail - 1 : element.length;
		}
	}
	
	private static void printArray(int input[]) {
		for(int i : input) {
			System.out.println(i);
		}
	}
	
	public static void main(String args[]) {
		System.out.println(isPalindrome(0));
		System.out.println(isPalindrome(101));
		System.out.println(isPalindrome(101101));
		int[] m = new int[]{1, 2, 4, 8, 9, 10};
		int[] n = new int[]{3, 5, 6, 7};
		int[] a = new int[]{1, 3, -1, -3, 5, 3, 6, 7};
		System.out.println(getTheMedianOfTwoSortedArrays(m, n));
		System.out.println(getKthSmallestNumber(m, n, 10));
		printArray(getMaximumInSlidingWindow(a, 3));
	}
}
