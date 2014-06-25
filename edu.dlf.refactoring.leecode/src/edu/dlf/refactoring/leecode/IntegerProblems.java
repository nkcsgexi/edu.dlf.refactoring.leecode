package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

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
	
	private static void calculateAllSquareAdd(int number, List<Integer> previous) {
		if(number == 0) {
			for(int factor : previous) {
				System.out.print(factor + " ");
			}
			System.out.println();
			return;
		}
		int base = 1;
		for(; base * base <= number; base ++);
		for(int start = base - 1; start > 0; start --) {
			previous.add(start);
			calculateAllSquareAdd(number - start * start, previous);
			previous.remove(previous.size() - 1);
		}
	}
	
	private static void printTwoSquareAdd(int number) {
		int base = 1;
		for(; base * base < number; base ++)
		for(int first = base - 1; first > 0; first --) {
			int second = number - first * first;
			for(int i = 1; i < second - 1; i ++) {
				if(i * i == second){
					System.out.println(first + " " + i);
					break;
				}
			}
		}
	}

	private static int printMaxNumberOfA(int count, int remainSteps) {
		int max = 0;
		for(int inputPart = 1; inputPart < remainSteps; inputPart ++) {
			int current = copyPastePart(inputPart, remainSteps - inputPart);
			max = current > max ? max : current;
		}	
		return max;
	}

	private static int copyPastePart(int count, int steps) {
		int maxCount = 0;
		for(int current = 4; current <= steps; current ++) {
			int times = current - 3;
			int max = Math.max(times * current, copyPastePart(current * times,
				steps - current));
			maxCount = max > maxCount ? max : maxCount;
		}
		return maxCount;
	}
	/* 
	 * print all the way that multiple integer can be added up to get the given result
	 * do not print duplicate. For instance: 7 = 2 + 2 +3. The duplicate of 3 + 2 + 2 is
	 * not allowed.
	 */
	private static void printAllAddsUp(int target, List<Integer> candidates, 
			List<Integer> previous) {
		if(target == 0)	{
			for(int i : previous) {
				System.out.print(i + " ");
			}
			System.out.println();
			return;
		}
		if(candidates.size() == 0)
			return;
		int value = candidates.get(0);
		for(int current = 0; value * current <= target; current ++) {
			int newTarget = target - value * current;
			int candidate = candidates.remove(0);
			previous.add(current);
			printAllAddsUp(newTarget, candidates, previous);
			previous.remove(previous.size() - 1);
			candidates.add(0, candidate);
		}
	}
	
	private static List<Integer> convertArray2List(int[] array) {
		List<Integer> result = new ArrayList<Integer>();
		for(int i : array) {
			result.add(i);
		}
		return result;
	}

	private static class IntegerPair{
		Integer first;
		Integer second;
		public IntegerPair(Integer first, Integer second) {
			this.first = first;
			this.second = second;
		}
	}

	/* Find the arithmetic number list in a given set of numbers.*/
	private static List<Integer> getLongestArithmicList(List<Integer> allNumbers) {
		Hashtable<Integer, List<IntegerPair>> table = new Hashtable<Integer,
			List<IntegerPair>>();
		for(int i = 0; i < allNumbers.size() - 1; i++) {
			int first = allNumbers.get(i);
			for(int j = i + 1; j < allNumbers.size(); j ++) {
				int second = allNumbers.get(j);
				int gap = second - first;
				IntegerPair pair = new IntegerPair(i, j);
				List<IntegerPair> list = table.get(gap);
				if(list == null){
					list = new ArrayList<IntegerPair>();
					table.put(gap, list);
				}
				list.add(pair);
			}
		}
		Collection<List<IntegerPair>> allLists = table.values();
		int maxLength = Integer.MIN_VALUE;
		List<Integer> maxIndexList = new ArrayList<Integer>();
		List<Integer> index = new ArrayList<Integer>();
		for(List<IntegerPair> list : allLists) {
			int length = 2;
			index.clear();
			for(int i = 0; i < list.size() - 1; i ++) {
				if(list.get(i).second == list.get(i+1).first) {
					length ++;
					if(index.size() == 0) {
						index.add(list.get(i).first);
						index.add(list.get(i).second);
					}
					index.add(list.get(i + 1).second);
				} else {
					length = 2;
					index.clear();
					index.add(list.get(i).first);
					index.add(list.get(i).second);
				}
				if(length > maxLength) {
					maxLength = length;
					maxIndexList.clear();
					maxIndexList.addAll(index);
				}
			}
		}
		return maxIndexList;
	}

	/* Find the longest consecutive number list in a given set of numbers.*/
	private static int getLongestConsecutiveNumbers(List<Integer> numbers) {
		Hashtable<Integer, Integer> set = new Hashtable<Integer, Integer>();
		for(int num : numbers)
			set.put(num, num);
		int longest = Integer.MIN_VALUE;
		while(set.size() != 0) {
			int length = 1;
			int head = set.keys().nextElement();
			set.remove(head);
			int before = head - 1;
			while(set.remove(before) != null) {
				before --;
				length ++;
			}
			int after = head + 1;
			while(set.remove(after) != null) {
				after ++;
				length ++;
			}
			if(length > longest) {
				longest = length;
			}
		}
		return longest;
	}

	private static class PositionValuePair implements Comparable<PositionValuePair>{
		int position;
		int value;
		public PositionValuePair(int position, int value) {
			this.position = position;
			this.value = value;
		}
		@Override
		public int compareTo(PositionValuePair o) {
			return value - o.value;
		}
	}
	private static int getNextNumberByRearrangingDigits(int number) {
		int originalNumber = number;
		int result = 0;
		Queue<PositionValuePair> previousDigits = new PriorityQueue
			<PositionValuePair>();
		int smallest = Integer.MIN_VALUE;
		int position = 0;
		int highPosition = 0;
		int highDigit = 1;
		while(number != 0) {
			int digit = number % 10;
			if(digit >= smallest) {
				smallest = digit;
				PositionValuePair pair = new PositionValuePair(position, digit);
				previousDigits.add(pair);
			} else {
				highPosition = position;
				highDigit = digit;
				break;
			}
			number = number / 10;
			position ++;
		}
		while(previousDigits.peek().value <= highDigit) 
			previousDigits.poll();
		int lowPosition = previousDigits.peek().position;
		int lowDigit = previousDigits.peek().value;
		number = originalNumber;
		position = 0;
		while(number != 0) {
			int base = getIntPow(10, position);
			if(position == lowPosition) {
				result += highDigit * base;
			}
			else if(position == highPosition) {
				result += lowDigit * base;
			}
			else 
				result += (number % 10) * base;
			number = number/10;	
			position ++;
		}
		return result;
	}
	
	private static int getIntPow(int base, int pow) {
		int result = 1;
		for(int i = 0; i < pow; i ++) {
			result *= base;
		}
		return result;
	}
	
	private static int getNumberAppearingOnce(int[] numbers) {
		int result = 0;
		for(int position = 0; position < 32; position ++) {
			int count = 0;
			for(int num : numbers) {
				count += ((num >> position) & 1);
			}
			result |= (count % 3) << position;
		}
		return result;
	}

	private static int[] getThreeIncrementalElementsInArray(int[] input) {
		int[] minFromLeft = new int[input.length];
		int[] maxFromRight = new int[input.length];
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for(int i = 0; i < input.length; i ++) {
			if(input[i] < min) {
				min = input[i];
			}
			minFromLeft[i] = min;
			if(input[input.length - i - 1] > max) {
				max = input[input.length - i -1];
			}
			maxFromRight[input.length - i - 1] = max;
		}
		int middle = -1;
		for(int i = 1; i < input.length - 1; i ++) {
			if(minFromLeft[i - 1] < input[i] && input[i] < 
					maxFromRight[i + 1]) {
				middle = i;
			}
		}
		if(middle == -1) return null;
		int before = 0;
		int after = 0;
		for(int i= 0; i < middle; i ++) {
			if(input[i] < input[middle]) {
				before = i;
			}
		}
		for(int i = middle +1 ; i < input.length; i++) {
			if(input[i] > input[middle]) {
				after = i;
			}
		}
		return new int[]{before, middle, after};
	}

	public static void main(String args[]) {
/*		System.out.println(isPalindrome(0));
		System.out.println(isPalindrome(101));
		System.out.println(isPalindrome(101101));
		int[] m = new int[]{1, 2, 4, 8, 9, 10};
		int[] n = new int[]{3, 5, 6, 7};
		int[] a = new int[]{1, 3, -1, -3, 5, 3, 6, 7};
		// System.out.println(getTheMedianOfTwoSortedArrays(m, n));
		// System.out.println(getKthSmallestNumber(m, n, 10));
		// printArray(getMaximumInSlidingWindow(a, 3));
		// calculateAllSquareAdd(100, new ArrayList<Integer>());
		// System.out.println(printMaxNumberOfA(10, 10));
		printAllAddsUp(7, convertArray2List(new int[]{2, 3, 6, 7}), 
			new ArrayList<Integer>());
		getLongestArithmicList(convertArray2List(new int[]{1,6,3,5,9,7})).
			forEach(i -> System.out.print(i + " "));
		System.out.println(getLongestConsecutiveNumbers(convertArray2List(
			new int[]{1, 6, 3, 5, 9, 7})));
		System.out.println(getNextNumberByRearrangingDigits(543126432));
		System.out.println(getNumberAppearingOnce(new int[]{2, 2, 2, 3, 3, 3, 
			1,1,1,11}));*/
/*		int[] result = getThreeIncrementalElementsInArray(new int[]{2, 5, 6,4,3,5});
		for(int i : result)
			System.out.println(i);*/
		
	}
}
