package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class IntegerProblems {

	private static int getDigitAt(int num, int pos) {
		int base = 1;
		for (int i = 0; i < pos; i++, base *= 10)
			;
		return (num / base) % 10;
	}

	private static boolean determinePalindromeRecursive(int num, int high,
			int low) {
		if (low == high)
			return true;
		if (low > high)
			return false;
		if (low == high - 1 && getDigitAt(num, high) == getDigitAt(num, low))
			return true;
		if (getDigitAt(num, high) == getDigitAt(num, low))
			return determinePalindromeRecursive(num, high - 1, low + 1);
		else
			return false;
	}

	private static boolean determinePalindrome(int num) {
		if (num < 0)
			return false;
		int high, base;
		for (high = 0, base = 10; num / base != 0; high++, base *= 10)
			;
		if (high == 0)
			return true;
		return determinePalindromeRecursive(num, high, 0);
	}

	// this is the optimum answer
	private static boolean isPalindrome(int num) {
		if (num < 0)
			return false;
		int div = 1;
		while (num / div >= 10) {
			div *= 10;
		}
		while (num != 0) {
			int h = num / div;
			int l = num % 10;
			if (h != l)
				return false;
			num = (num % div) / 10;
			div = div / 100;
		}
		return true;
	}

	/*
	 * Given an array of non-negative integers A and a positive integer k, we
	 * want to: Divide A into k or fewer partitions, such that the maximum sum
	 * over all the partitions is minimized.
	 */
	private static int decideSchedule(int[] tasks, int workers) {
		int[][] matrix = new int[workers][tasks.length];
		for (int i = 0; i < workers; i++) {
			matrix[i][0] = tasks[0];
		}
		for (int i = 1; i < tasks.length; i++) {
			matrix[0][i] = matrix[0][i - 1] + tasks[i];
		}

		for (int i = 1; i < workers; i++) {
			for (int j = 1; j < tasks.length; j++) {
				int best = Integer.MAX_VALUE;
				for (int middle = 0; middle < j; middle++) {
					best = Math.min(best, Math.max(matrix[i - 1][middle],
							(matrix[0][j] - matrix[0][middle])));
				}
				matrix[i][j] = best;
			}
		}
		return matrix[workers - 1][tasks.length - 1];
	}

	/*
	 * There are two sorted arrays A and B of size m and n respectively. Find
	 * the median of the two sorted arrays. The overall run time complexity
	 * should be O(log (m+n)).
	 */
	private static double getTheMedianOfTwoSortedArrays(int[] input1,
			int[] input2) {
		if (input1.length == 0)
			return getMedian(input2);
		else if (input2.length == 0)
			return getMedian(input1);

		if (input1.length == 1 || input2.length == 1) {
			int num = input1.length == 1 ? input1[0] : input2[0];
			int[] input = input1.length == 1 ? input2 : input1;
			return calculateSpecialCase(num, input);
		}

		if (compareMedian(input1, input2) == 0) {
			return getMedian(input1);
		}
		int trim = Math.min(input1.length / 2, input2.length / 2);
		int[] trimEnd = compareMedian(input1, input2) > 0 ? input1 : input2;
		int[] trimStart = trimEnd == input1 ? input2 : input1;
		trimEnd = getSubarray(trimEnd, 0, trimEnd.length - trim);
		trimStart = getSubarray(trimStart, trim, trimStart.length);
		return getTheMedianOfTwoSortedArrays(trimEnd, trimStart);
	}

	private static double calculateSpecialCase(int num, int[] input) {
		int[] newInput = new int[input.length + 1];
		boolean used = false;
		for (int i = 0; i < newInput.length; i++) {
			if (input[i] < num)
				newInput[i] = input[i];
			if (input[i] >= num && used == false) {
				used = true;
				newInput[i] = num;
				newInput[i + 1] = input[i];
				i++;
			}
		}
		return getMedian(newInput);
	}

	private static int getIndex(int num, int[] input, int start, int end) {
		if (start == end)
			return start;
		int median = (end + start) / 2;
		if (input[median] == num)
			return median;
		if (input[median] > num) {
			return getIndex(num, input, start, median - 1);
		} else
			return getIndex(num, input, median + 1, end);
	}

	private static int[] getSubarray(int[] input, int start, int end) {
		int[] results = new int[end - start];
		for (int i = 0; i < results.length; i++)
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
		return input.length % 2 == 1 ? input[input.length / 2] * 2
				: input[input.length / 2 - 1] + input[input.length / 2];
	}

	/*
	 * Given two sorted arrays A, B of size m and n respectively. Find the k-th
	 * smallest element in the union of A and B. You can assume that there are
	 * no duplicate elements.
	 */
	private static int getKthSmallestNumber(int[] m, int[] n, int k) {
		if (m.length == 0)
			return n[k - 1];
		if (n.length == 0)
			return m[k - 1];
		if (k == 1) {
			return Math.min(m[0], n[0]);
		}
		int middleM = m[m.length / 2];
		int position = -binarySearch(n, middleM, 0, n.length - 1);
		int split;
		if (n[position] < middleM) {
			position++;
		}
		split = m.length / 2 + position + 1;
		if (split == k)
			return m[m.length / 2];
		if (split < k)
			return getKthSmallestNumber(
					getSubarray(m, m.length / 2 + 1, m.length),
					getSubarray(n, position, n.length), k - split);
		else
			return getKthSmallestNumber(getSubarray(m, 0, m.length / 2),
					getSubarray(n, 0, position), k);
	}

	private static int binarySearch(int[] input, int target, int start, int end) {
		if (start == end) {
			return input[start] == target ? start : -start;
		}
		int middle = (start + end) / 2;
		if (target == input[middle])
			return middle;
		if (target > input[middle]) {
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
	 */
	public static int[] getMaximumInSlidingWindow(int[] input, int window) {
		int[] results = new int[input.length - window + 1];
		DoubleQueue queue = new DoubleQueue(window);
		for (int i = 0; i < window; i++) {
			while (!queue.empty() && input[queue.getTail()] < input[i]) {
				queue.removeTail();
			}
			queue.insertTail(i);
		}
		for (int end = window; end < input.length; end++) {
			results[end - window] = input[queue.getHead()];
			while (!queue.empty() && input[queue.getTail()] < input[end]) {
				queue.removeTail();
			}
			queue.insertTail(end);
			while (!queue.empty() && queue.getHead() <= end - window)
				queue.removeHead();
		}
		results[input.length - window] = input[queue.getHead()];
		return results;
	}

	private static class DoubleQueue {

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
		for (int i : input) {
			System.out.println(i);
		}
	}

	private static void calculateAllSquareAdd(int number, List<Integer> previous) {
		if (number == 0) {
			for (int factor : previous) {
				System.out.print(factor + " ");
			}
			System.out.println();
			return;
		}
		int base = 1;
		for (; base * base <= number; base++)
			;
		for (int start = base - 1; start > 0; start--) {
			previous.add(start);
			calculateAllSquareAdd(number - start * start, previous);
			previous.remove(previous.size() - 1);
		}
	}

	private static void printTwoSquareAdd(int number) {
		int base = 1;
		for (; base * base < number; base++)
			for (int first = base - 1; first > 0; first--) {
				int second = number - first * first;
				for (int i = 1; i < second - 1; i++) {
					if (i * i == second) {
						System.out.println(first + " " + i);
						break;
					}
				}
			}
	}

	private static int printMaxNumberOfA(int count, int remainSteps) {
		int max = 0;
		for (int inputPart = 1; inputPart < remainSteps; inputPart++) {
			int current = copyPastePart(inputPart, remainSteps - inputPart);
			max = current > max ? max : current;
		}
		return max;
	}

	private static int copyPastePart(int count, int steps) {
		int maxCount = 0;
		for (int current = 4; current <= steps; current++) {
			int times = current - 3;
			int max = Math.max(times * current,
					copyPastePart(current * times, steps - current));
			maxCount = max > maxCount ? max : maxCount;
		}
		return maxCount;
	}

	/*
	 * print all the way that multiple integer can be added up to get the given
	 * result do not print duplicate. For instance: 7 = 2 + 2 +3. The duplicate
	 * of 3 + 2 + 2 is not allowed.
	 */
	private static void printAllAddsUp(int target, List<Integer> candidates,
			List<Integer> previous) {
		if (target == 0) {
			for (int i : previous) {
				System.out.print(i + " ");
			}
			System.out.println();
			return;
		}
		if (candidates.size() == 0)
			return;
		int value = candidates.get(0);
		for (int current = 0; value * current <= target; current++) {
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
		for (int i : array) {
			result.add(i);
		}
		return result;
	}

	private static class IntegerPair {
		Integer first;
		Integer second;

		public IntegerPair(Integer first, Integer second) {
			this.first = first;
			this.second = second;
		}
	}

	/* Find the arithmetic number list in a given set of numbers. */
	private static List<Integer> getLongestArithmicList(List<Integer> allNumbers) {
		Hashtable<Integer, List<IntegerPair>> table = new Hashtable<Integer, List<IntegerPair>>();
		for (int i = 0; i < allNumbers.size() - 1; i++) {
			int first = allNumbers.get(i);
			for (int j = i + 1; j < allNumbers.size(); j++) {
				int second = allNumbers.get(j);
				int gap = second - first;
				IntegerPair pair = new IntegerPair(i, j);
				List<IntegerPair> list = table.get(gap);
				if (list == null) {
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
		for (List<IntegerPair> list : allLists) {
			int length = 2;
			index.clear();
			for (int i = 0; i < list.size() - 1; i++) {
				if (list.get(i).second == list.get(i + 1).first) {
					length++;
					if (index.size() == 0) {
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
				if (length > maxLength) {
					maxLength = length;
					maxIndexList.clear();
					maxIndexList.addAll(index);
				}
			}
		}
		return maxIndexList;
	}

	/* Find the longest consecutive number list in a given set of numbers. */
	private static int getLongestConsecutiveNumbers(List<Integer> numbers) {
		Hashtable<Integer, Integer> set = new Hashtable<Integer, Integer>();
		for (int num : numbers)
			set.put(num, num);
		int longest = Integer.MIN_VALUE;
		while (set.size() != 0) {
			int length = 1;
			int head = set.keys().nextElement();
			set.remove(head);
			int before = head - 1;
			while (set.remove(before) != null) {
				before--;
				length++;
			}
			int after = head + 1;
			while (set.remove(after) != null) {
				after++;
				length++;
			}
			if (length > longest) {
				longest = length;
			}
		}
		return longest;
	}

	private static class PositionValuePair implements
			Comparable<PositionValuePair> {
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
		Queue<PositionValuePair> previousDigits = new PriorityQueue<PositionValuePair>();
		int smallest = Integer.MIN_VALUE;
		int position = 0;
		int highPosition = 0;
		int highDigit = 1;
		while (number != 0) {
			int digit = number % 10;
			if (digit >= smallest) {
				smallest = digit;
				PositionValuePair pair = new PositionValuePair(position, digit);
				previousDigits.add(pair);
			} else {
				highPosition = position;
				highDigit = digit;
				break;
			}
			number = number / 10;
			position++;
		}
		while (previousDigits.peek().value <= highDigit)
			previousDigits.poll();
		int lowPosition = previousDigits.peek().position;
		int lowDigit = previousDigits.peek().value;
		number = originalNumber;
		position = 0;
		while (number != 0) {
			int base = getIntPow(10, position);
			if (position == lowPosition) {
				result += highDigit * base;
			} else if (position == highPosition) {
				result += lowDigit * base;
			} else
				result += (number % 10) * base;
			number = number / 10;
			position++;
		}
		return result;
	}

	private static int getIntPow(int base, int pow) {
		int result = 1;
		for (int i = 0; i < pow; i++) {
			result *= base;
		}
		return result;
	}

	private static int getNumberAppearingOnce(int[] numbers) {
		int result = 0;
		for (int position = 0; position < 32; position++) {
			int count = 0;
			for (int num : numbers) {
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
		for (int i = 0; i < input.length; i++) {
			if (input[i] < min) {
				min = input[i];
			}
			minFromLeft[i] = min;
			if (input[input.length - i - 1] > max) {
				max = input[input.length - i - 1];
			}
			maxFromRight[input.length - i - 1] = max;
		}
		int middle = -1;
		for (int i = 1; i < input.length - 1; i++) {
			if (minFromLeft[i - 1] < input[i] && input[i] < maxFromRight[i + 1]) {
				middle = i;
			}
		}
		if (middle == -1)
			return null;
		int before = 0;
		int after = 0;
		for (int i = 0; i < middle; i++) {
			if (input[i] < input[middle]) {
				before = i;
			}
		}
		for (int i = middle + 1; i < input.length; i++) {
			if (input[i] > input[middle]) {
				after = i;
			}
		}
		return new int[] { before, middle, after };
	}

	/*
	 * Problem: Numbers are serialized increasingly into a sequence in the
	 * format of 0123456789101112131415..., which each digit occupies a position
	 * in the sequence. For instance, the digit in the position 5 is 5, in the
	 * position 13 is 1, in the position 19 is 4, and so on.
	 */
	private static int getDigitAtPosition(int position) {
		int base = 10;
		int count = 1;
		int p = position;
		for (; p > 0; base *= 10, count++) {
			p = p - base * count;
		}
		base /= 10;
		count -= 1;
		p = p + base * count;
		int number = base / 10 + p / count;
		int digit = count - (p % count) - 1;
		while (digit != 0) {
			number /= 10;
			digit--;
		}
		return number % 10;
	}

	/* Find the integer of the max repetition in the given array. */
	private static int getMaxRepeat(int[] input) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i : input) {
			if (map.get(i) != null) {
				int count = map.remove(i);
				map.put(i, count + 1);
			} else
				map.put(i, 1);
		}
		int maxRepeat = Integer.MIN_VALUE;
		int result = 0;
		for (int key : map.keySet()) {
			int value = map.get(key);
			if (value > maxRepeat) {
				maxRepeat = value;
				result = key;
			}
		}
		return result;
	}

	/*
	 * Get a range that every element in the range appear in the given input
	 * array.
	 */
	private static int[] getRangeIncludingMost(int[] input) {
		HashSet<Integer> set = new HashSet<Integer>();
		for (int i : input)
			set.add(i);
		int rangeStart = 0, rangeEnd = 0;
		int rangeLength = Integer.MIN_VALUE;
		while (set.size() != 0) {
			int seed = set.stream().findFirst().get();
			set.remove(seed);
			int start = seed - 1;
			int end = seed + 1;
			for (; set.contains(start); start--)
				;
			for (; set.contains(end); end++)
				;
			start++;
			end--;
			if (end - start + 1 > rangeLength) {
				rangeLength = end - start + 1;
				rangeStart = start;
				rangeEnd = end;
			}
		}
		return new int[] { rangeStart, rangeEnd };
	}

	private static List<List<Integer>> waysToCollect(int total,
			List<Integer> options) {
		List<List<Integer>> results = new ArrayList<List<Integer>>();
		if (options.size() == 1 && options.get(0) == 1) {
			List<Integer> l = new ArrayList<Integer>();
			l.add(total);
			results.add(l);
			return results;
		}
		int option = options.get(0);
		List<Integer> restOptions = options.subList(1, options.size());
		int num = 0;
		for (int value = total; value >= 0; value -= option) {
			List<List<Integer>> list = waysToCollect(value, restOptions);
			for (List<Integer> l : list) {
				l.add(0, num);
				results.add(l);
			}
			num++;
		}
		return results;
	}

	private static List<Integer> convertArrayToList(int[] array) {
		List<Integer> results = new ArrayList<Integer>();
		for (int i : array) {
			results.add(i);
		}
		return results;
	}

	private static void printList(List<List<Integer>> lists) {
		for (List<Integer> list : lists) {
			for (int i : list) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
	}

	private static class SetElement {
		int value;
		int count;

		SetElement(int value, int count) {
			this.value = value;
			this.count = count;
		}
	}

	private static List<SetElement> convertToSetElement(List<Integer> numbers) {
		List<SetElement> results = new ArrayList<SetElement>();
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i : numbers) {
			if (map.containsKey(i)) {
				int value = map.get(i) + 1;
				map.remove(i);
				map.put(i, value);
			} else
				map.put(i, 1);
		}
		for (Entry<Integer, Integer> pair : map.entrySet()) {
			results.add(new SetElement(pair.getKey(), pair.getValue()));
		}
		return results;
	}

	/*
	 * We are given a set of integers with repeated occurences of elements. For
	 * Example, S={1,2,2}. We need to print the power set of S ensuring that the
	 * repeated elements of the power set are printed only once. For the above
	 * S, the power set will be {NULL, {1}, {2}, {2}, {1,2}, {1,2}, {2,2},
	 * {1,2,2}}. So, as per the question requirements, we need to print {NULL,
	 * {1}, {2}, {1,2}, {2,2}, {1,2,2}}
	 */
	private static List<List<Integer>> getAllSubset(List<SetElement> numbers) {
		List<List<Integer>> results = new ArrayList<List<Integer>>();
		if (numbers.size() == 0) {
			List<Integer> l = new ArrayList<Integer>();
			results.add(l);
			return results;
		}
		List<List<Integer>> rest = getAllSubset(numbers.subList(1,
				numbers.size()));
		int value = numbers.get(0).value;
		int count = numbers.get(0).count;
		results.addAll(rest);
		for (int c = 1; c <= count; c++) {
			for (List<Integer> r : rest) {
				List<Integer> l = new ArrayList<Integer>();
				l.addAll(r);
				for (int k = 0; k < c; k++) {
					l.add(0, value);
				}
				results.add(l);
			}
		}
		return results;
	}

	private static void printSubSets(int[] numbers) {
		printList(getAllSubset(convertToSetElement(convertArrayToList(numbers))));
	}

	/*
	 * Print all valid phone numbers of length n subject to following
	 * constraints: 1.If a number contains a 4, it should start with 4 2.No two
	 * consecutive digits can be same 3.Three digits (e.g. 7,2,9) will be
	 * entirely disallowed, take as input
	 */
	private static void printAllPhoneNumbers(int n, List<Integer> prefix) {
		List<Integer> allowed = getAllowedDigit(prefix);
		for (int allow : allowed) {
			List<Integer> newPrefix = new ArrayList<Integer>();
			newPrefix.addAll(prefix);
			newPrefix.add(allow);
			if (newPrefix.size() == n) {
				for (int p : newPrefix) {
					System.out.print(p);
				}
				System.out.println();
			} else {
				printAllPhoneNumbers(n, newPrefix);
			}
		}
	}

	private static List<Integer> getAllowedDigit(List<Integer> prefix) {
		List<Integer> results = new ArrayList<Integer>();
		if (prefix.size() == 0) {
			results.addAll(digits);
			return results;
		}
		results.addAll(digits);
		results.removeIf(d -> prefix.get(prefix.size() - 1) == d);
		results.removeIf(d -> d == 4);
		return results;
	}

	private final static List<Integer> digits = convertArrayToList(new int[] {
			0, 1, 3, 4, 5, 6, 8 });

	private static class MedianStream {
		private final Queue<Integer> minQueue = new PriorityQueue<Integer>();

		private final Queue<Integer> maxQueue = new PriorityQueue<Integer>(10,
				Collections.reverseOrder());

		public MedianStream() {
			minQueue.add(Integer.MAX_VALUE);
			maxQueue.add(Integer.MIN_VALUE);
		}

		void insertValue(int value) {
			if (value > maxQueue.peek()) {
				minQueue.add(value);
			} else {
				maxQueue.add(value);
			}
			Queue<Integer> from = maxQueue.size() > minQueue.size() ? maxQueue
					: minQueue;
			Queue<Integer> to = from == maxQueue ? minQueue : maxQueue;
			while (from.size() > to.size()) {
				to.add(from.remove());
			}
		}

		int getMedian() {
			int mip = minQueue.peek();
			int map = maxQueue.peek();
			if (minQueue.size() == maxQueue.size()) {
				return (minQueue.peek() + maxQueue.peek()) / 2;
			}
			return minQueue.size() > maxQueue.size() ? minQueue.peek()
					: maxQueue.peek();
		}

		public static void testMedianStream() {
			MedianStream stream = new MedianStream();
			stream.insertValue(0);
			stream.insertValue(1);
			stream.insertValue(2);
			stream.insertValue(3);
			System.out.println(stream.getMedian());
			stream.insertValue(5);
			System.out.println(stream.getMedian());
			stream.insertValue(7);
			System.out.println(stream.getMedian());
		}
	}

	/* Print all subsets. */
	private static void printAllSubset(List<Integer> prefix,
			List<Integer> remains) {
		if (remains.size() == 0) {
			System.out.print("{");
			for (int i : prefix) {
				System.out.print(i + ",");
			}
			System.out.println("}");
			return;
		}
		int next = remains.get(0);
		List<Integer> nextRemains = remains.subList(1, remains.size());
		printAllSubset(prefix, nextRemains);
		List<Integer> contained = new ArrayList<Integer>();
		contained.addAll(prefix);
		contained.add(next);
		printAllSubset(contained, nextRemains);
	}

	private static List<Integer> spiralTraversal(int[][] matrix, int N) {
		List<Integer> results = new ArrayList<Integer>();
		for (int offset = 0; offset < N / 2; offset++) {
			int left = offset;
			int right = N - offset - 1;
			int top = offset;
			int bottom = N - offset - 1;
			if (left == right && top == bottom) {
				results.add(matrix[top][left]);
				return results;
			}
			for (int column = left; column < right; column++) {
				results.add(matrix[top][column]);
			}
			for (int row = top; row < bottom; row++) {
				results.add(matrix[row][right]);
			}
			for (int column = right; column > left; column--) {
				results.add(matrix[bottom][column]);
			}
			for (int row = bottom; row > top; row--) {
				results.add(matrix[row][left]);
			}
		}
		return results;
	}

	private static int[][] convertToMatrix(int[] nums) {
		int dimension = 1;
		for (; dimension * dimension <= nums.length; dimension++)
			;
		dimension--;
		int i = 0;
		int[][] matrix = new int[dimension][dimension];
		for (int row = 0; row < dimension; row++) {
			for (int column = 0; column < dimension; column++) {
				matrix[row][column] = nums[i++];
			}
		}
		return matrix;
	}

	private static void testMatrix() {
		int dimension = 6;
		int[] nums = new int[dimension * dimension];
		for (int i = 1; i < dimension * dimension; i++)
			nums[i] = i;
		int[][] matrix = convertToMatrix(nums);
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++)
				System.out.print(matrix[i][j]);
			System.out.println();
		}
		List<Integer> results = spiralTraversal(matrix, dimension);
		for (int r : results)
			System.out.println(r);
	}

	private static void printAllPasswords(List<Integer> prefix, int N) {
		if (prefix.size() == N) {
			for (int i : prefix) {
				System.out.print(i);
			}
			System.out.println();
			return;
		}
		int M = N - prefix.size() - 1;
		int pre = prefix.size() != 0 ? prefix.get(prefix.size() - 1) : 0;
		List<Integer> options = getPossibleDigit(pre, M);
		for (int o : options) {
			List<Integer> l = new ArrayList<Integer>();
			l.addAll(prefix);
			l.add(o);
			printAllPasswords(l, N);
		}
	}

	private static List<Integer> getPossibleDigit(int pre, int M) {
		int max = 9 - M;
		List<Integer> results = new ArrayList<Integer>();
		for (int i = pre + 1; i <= max; i++)
			results.add(i);
		return results;
	}

	private static HashSet<Integer> getAllowedDigit(int d) {
		HashSet<Integer> l = new HashSet<Integer>();
		int row = (d - 1) / 3;
		int column = (d - 1) % 3;
		int rowStart = row * 3 + 1;
		int columnStart = column + 1;
		for (int i = 0; i < 3; i++) {
			l.add(rowStart + i);
			l.add(columnStart + 3 * i);
		}
		l.remove(d);
		return l;
	}

	private static boolean isPassworkAllowed(int[] real, int[] input) {
		boolean chanceUsed = false;
		for (int i = 0; i < real.length; i++) {
			if (real[i] != input[i]) {
				if (!chanceUsed && getAllowedDigit(input[i]).contains(real[i])) {
					chanceUsed = true;
				} else
					return false;
			}
		}
		return true;
	}

	/*
	 * Given a set of digits, randomly get a number that can be represented by
	 * those digits.
	 */
	private static void getNumberRandomly(int[] digits) {
		Random seed = new Random();
		for (int i = 0; i < digits.length; i++) {
			int j = seed.nextInt(digits.length - 1 - i);
			int swap = digits[i];
			digits[i] = digits[j];
			digits[j] = swap;
		}
	}

	/*
	 * Given a list of numbers, rearrange the positions of numbers so that the
	 * even numbers are on even positions or the odd numbers are on odd
	 * positions.
	 */
	private static void rearrangeNumbers(int[] nums) {
		int length = nums.length;
		Stack<Integer> oddPositions = new Stack<Integer>();
		Stack<Integer> evenPositions = new Stack<Integer>();
		for (int i = 0; i < length; i++) {
			if (i % 2 == nums[i] % 2) {
				continue;
			}
			if (i % 2 == 1) {
				evenPositions.push(i);
			} else
				oddPositions.push(i);
		}
		while (evenPositions.size() != 0 || oddPositions.size() != 0) {
			int e = evenPositions.pop();
			int o = oddPositions.pop();
			int temp = nums[e];
			nums[e] = nums[o];
			nums[o] = temp;
		}
	}

	private static String printPreviousLine(String pl) {
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		for (char c : pl.toCharArray()) {
			if (c >= '0' && c <= '9') {
				int value = 1;
				if (map.containsKey(c)) {
					value = map.get(c) + 1;
					map.remove(c);
				}
				map.put(c, value);
			}
		}
		StringBuilder sb = new StringBuilder();
		for (Entry<Character, Integer> entry : map.entrySet()) {
			sb.append(entry.getValue() + "" + entry.getKey());
		}
		return sb.toString();
	}

	private static void printNLines(int N) {
		String preLine = "1";
		for (int i = 0; i < N; i++) {
			System.out.println(preLine);
			preLine = printPreviousLine(preLine);
		}
	}

	private static void printNumbersAddingToValue(int value, int[] input) {
		List<Integer> list = convertArrayToList(input);
		Collections.sort(list);
		printNumbersInternal(value, new ArrayList<Integer>(), list);
	}

	private static void printNumbersInternal(int total, List<Integer> prefix,
			List<Integer> options) {
		if (total == 0) {
			for (int i : prefix) {
				System.out.print(i + " ");
			}
			System.out.println();
			return;
		}
		if (options.size() == 0)
			return;
		int option = options.get(0);
		if (option > total)
			return;
		List<Integer> newPrefix = new ArrayList<Integer>();
		newPrefix.addAll(prefix);
		newPrefix.add(option);
		options.remove(0);
		List<Integer> newOptions = new ArrayList<Integer>();
		newOptions.addAll(options);
		printNumbersInternal(total, prefix, options);
		printNumbersInternal(total - option, newPrefix, newOptions);
	}

	private static void printProduct(int[] input) {
		int total = 1;
		for (int i : input) {
			total *= i;
		}
		for (int i : input) {
			System.out.print(total / i + " ");
		}
	}

	private static int splitArraysWithSameAverage(int[] nums) {
		int average = 0;
		for (int n : nums) {
			average += n;
		}
		average /= nums.length;
		int currentSum = 0;
		for (int i = 0; i < nums.length; i++) {
			currentSum += nums[i];
			if (currentSum / (i + 1) == average) {
				return i;
			}
		}
		return nums.length - 1;
	}

	private static void printNumbers() {
		int[] digits = new int[] { 1, 2, 3, 4, 5, 6 };
		while (getNextNumber(digits)) {
			for (int i : digits)
				System.out.print(i);
			System.out.println();
		}
	}

	private static boolean getNextNumber(int[] digits) {
		boolean found = false;
		for (int index = digits.length - 1; index > 0; index--) {
			if (digits[index] > digits[index - 1]) {
				int swap = digits.length - 1;
				for (; swap >= index; swap--) {
					if (digits[swap] > digits[index - 1]) {
						break;
					}
				}
				int temp = digits[swap];
				digits[swap] = digits[index - 1];
				digits[index - 1] = temp;
				found = true;

				List<Integer> l = new ArrayList<Integer>();
				for (int i = digits.length - 1; i >= index; i--) {
					l.add(digits[i]);
				}
				Collections.sort(l);
				int k = l.size() - 1;
				for (int i = digits.length - 1; i >= index; i--) {
					digits[i] = l.get(k--);
				}

				break;
			}
		}
		return found;
	}

	private static boolean binarySearch2(int[] row, int start, int end,
			int target) {
		if (end < start)
			return false;
		int middle = (start + end) / 2;
		if (row[middle] == target)
			return true;
		if (row[middle] > target)
			return binarySearch2(row, start, middle - 1, target);
		else
			return binarySearch2(row, middle + 1, end, target);
	}

	private static int convertExcelRowNumberToRealNumber(String s) {
		int sum = 0;
		for (int i = s.length() - 1, base = 1; i >= 0; i--, base *= 26) {
			int n = s.charAt(i) - 'a' + 1;
			sum += n * base;
		}
		return sum - 1;
	}

	private static void testExcelRow() {
		System.out.println(convertExcelRowNumberToRealNumber("a"));
		System.out.println(convertExcelRowNumberToRealNumber("ab"));
		System.out.println(convertExcelRowNumberToRealNumber("abc"));
	}

	private static void printCombinations(int total, List<Integer> options,
			List<Integer> prefix) {
		if (total == 0) {
			for (int p : prefix) {
				System.out.print(p + "+");
			}
			System.out.println();
			return;
		}
		if (options.size() == 0)
			return;
		int op = options.get(0);
		for (int i = 0; i * op <= total; i++) {
			int newTotal = total;
			List<Integer> newPre = new ArrayList<Integer>();
			newPre.addAll(prefix);
			for (int j = 0; j < i; j++) {
				newPre.add(op);
				newTotal -= op;
			}
			printCombinations(newTotal, options.subList(1, options.size()),
					newPre);
		}
	}

	private static void testPrintCombination() {
		List<Integer> options = convertArrayToList(new int[] { 10, 1, 2, 7, 6,
				5 });
		List<Integer> prefix = new ArrayList<Integer>();
		int total = 8;
		printCombinations(total, options, prefix);
	}

	private static int bestTimeToBuyAndSell(int[] values) {
		List<Integer> minValueFromLeft = new ArrayList<Integer>();
		List<Integer> maxValueFromRight = new ArrayList<Integer>();
		minValueFromLeft.add(values[0]);
		maxValueFromRight.add(values[values.length - 1]);
		for (int i = 1; i < values.length; i++) {
			int tail = minValueFromLeft.get(minValueFromLeft.size() - 1);
			minValueFromLeft.add(Math.min(tail, values[i]));
		}
		for (int i = values.length - 1; i >= 0; i--) {
			int head = maxValueFromRight.get(0);
			maxValueFromRight.add(0, Math.max(head, values[i]));
		}
		int maxGap = Integer.MIN_VALUE;
		for (int i = 1; i < values.length - 1; i++) {
			int leftMin = minValueFromLeft.get(i);
			int rightMax = maxValueFromRight.get(i);
			if (rightMax - leftMin > maxGap)
				maxGap = rightMax - leftMin;
		}
		return maxGap;
	}

	private static void testBuyAndSell() {
		int[] values = new int[] {2, 2, 1, 5, 1, 4 };
		System.out.println(bestTimeToBuyAndSell(values));
	}

	private static List<Integer> getIntersectionOfTwoSortedArrays(
			List<Integer> l1, List<Integer> l2) {
		int p1 = 0;
		int p2 = 0;
		List<Integer> results = new ArrayList<Integer>();
		while (p1 != l1.size() && p2 != l2.size()) {
			int n1 = l1.get(p1);
			int n2 = l2.get(p2);
			if (n1 == n2) {
				results.add(n1);
				p1 ++;
				p2 ++;
			}
			else if (n1 > n2)
				p2++;
			else
				p1++;
		}
		return results;
	}

	private static void testIntersection() {
		List<Integer> l1 = convertArrayToList(new int[]{1, 3, 4, 5, 7});
		List<Integer> l2 = convertArrayToList(new int[]{4, 7});
		Collections.sort(l1);
		Collections.sort(l2);
		List<Integer> l3 = getIntersectionOfTwoSortedArrays(l1, l2);
		for( int i : l3) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
	
	private static int[] xorSwap(int x, int y) {
		x = x ^ y;
		y = x ^ y;
		x = x ^ y;
		return new int[]{x, y};
	}
	
	private static void testXorSwap() {
		int[] result = xorSwap(1, 222);
		System.out.println(result[0] + " " + result[1]);
	}
	
	private static int gcd(int a, int b) {
		while(a != b) {
			if (a > b) {
				a = a - b;
			} else {
				b = b - a;
			}
		}
		return a;
	}
	
	private static void testGCD() {
		System.out.println(gcd(42435, 424719));
	}
	
	public static void main(String args[]) {
		testGCD();
	}
}
