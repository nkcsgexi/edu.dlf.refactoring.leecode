package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.List;


public class SortingAlgorithms {

	private static class HeapNode {
		HeapNode left;
		HeapNode right;
		int value;
		public HeapNode(int value) {
			this.value = value;
		}
	}


	public static int[] insertionSort(int[] input) {
		for(int i = 1; i < input.length; i ++) {
			for(int j = i; j > 0;j--) {
				if(input[j] > input[j-1]) {
					int temp = input[j];
					input[j] = input[j - 1];
					input[j - 1] = temp;
				} else
					break;
			}
		}
		return input;
	}

	public static int[] selectionSort(int[] input) {
		for(int position = 0; position < input.length; position ++) {
			int min = Integer.MAX_VALUE;
			int index = 0;
			for(int i = position; i < input.length; i ++) {
				if(input[i] < min) {
					min = input[i];
					index = i;
				}
			}
			int temp = input[position];
			input[position] = input[index];
			input[index] = temp;
		}
		return input;
	}

	public static int[] quickSort(int[] input, int start, int end) {
		if(start == end) {
			return input;
		}
		int pivotPosition = start;
		int pivot = input[end];
		for(int i = start; i < end; i ++) {
			if(input[i] < pivot) {
				int temp = input[i];
				input[i] = input[pivotPosition];
				input[pivotPosition] = temp;
				pivotPosition ++;
			}
		}
		int temp = input[pivotPosition];
		input[pivotPosition] = input[end];
		input[end] = temp;
		if(pivotPosition > start) quickSort(input, start, pivotPosition - 1);
		if(pivotPosition < end)quickSort(input, pivotPosition + 1, end);
		return input;
	}


	public static int[] mergeSort(int[] input, int start, int end) {
		if(start == end)
			return new int[]{input[start]};
		int[] first = mergeSort(input, start, (end + start)/2);
		int[] second = mergeSort(input, (end + start)/2 + 1, end);
		int[] results = new int[first.length + second.length];
		int index1 = 0, index2 = 0;
		while(index1 < first.length && index2 < second.length) {
			if(first[index1] < second[index2]) {
				results[index1 + index2] = first[index1];
				index1 ++;
			} else {
				results[index1 + index2] = second[index2];
				index2 ++;
			}
		}
		for(;index1 < first.length; index1 ++)
			results[index1 + index2] = first[index1];
		for(;index2 < second.length; index2 ++)
			results[index1 + index2] = second[index2];
		return results;
	}

	public static HeapNode insertHeap(HeapNode heap, int value) {
		return mergeHeap(heap, new HeapNode(value));
	}

	public static HeapNode removeHeap(HeapNode heap) {
		return mergeHeap(heap.left, heap.right);
	}

	public static HeapNode mergeHeap(HeapNode heap1, HeapNode heap2) {
		if(heap1 == null) return heap2;
		if(heap2 == null) return heap1;
		HeapNode heapMin = heap1.value < heap2.value ? heap1 : heap2;
		HeapNode heapMax = heapMin == heap1 ? heap2 : heap1;
		heapMin.right = mergeHeap(heapMin.right, heapMax);
		return heapMin;
	}

	public static int[] heapSort(int[] input) {
		HeapNode root = new HeapNode(input[0]);
		for(int i = 1; i < input.length; i ++){
			root = insertHeap(root, input[i]);
		}
		for(int i = 0; i < input.length; i ++) {
			input[i] = root.value;
			root = removeHeap(root);
		}
		return input;
	}

	/*
	 * You are given two sorted arrays, A and B, and A has a large enough
	 * buffer at the end to hold B. Write a method to merge B into A in sorted
	 * order.
	 * */
	private static void mergeArrays(int[] A, int[] B, int AInitialSize) {
		int ALength = AInitialSize;
		for(int i = 0; i < B.length; i ++) {
			int b = B[i];
			int position = binarySearch(A, 0, ALength - 1, b);
			for(int j = ALength; j > position; j --) {
				A[j] = A[j-1];
			}
			A[position] = b;
			ALength += 1;
		}
	}

	private static int binarySearch(int[] array, int start, int end, int target) {
		if(start == end) {
			if(array[start] >= target)
				return start;
			else
				return start + 1;
		}
		int middle = (start + end)/2;
		if(target == array[middle])
			return middle;
		if(target > array[middle])
			return binarySearch(array, middle + 1 > end ? end : middle + 1, end,
				target);
		else
			return binarySearch(array, start, middle - 1 < start ? start :
				middle - 1, target);
	}

	private static void smartMerge(int[] A, int[] B, int AinitialSize) {
		int endA = AinitialSize - 1;
		int endB = B.length - 1;
		int insertPosition = A.length + B.length - 1;
		while(endA >= 0 && endB >= 0) {
			if(A[endA] > B[endB]) {
				A[insertPosition--] = A[endA --];
			}else {
				A[insertPosition--] = B[endB --];
			}
		}
	}
	/*
	 * Given a sorted array of n integers that has been rotated an unknown
	 * number of times, give an O(log n) algorithm that finds an element in the
	 * array. You may assume that the array was originally sorted in increasing
	 * order. EXAMPLE: Input: find 5 in array (15 16 19 20 25 1 3 4 5 7 10 14)
	 * Output: 8 (the index of 5 in the array).
	 * */
	private static int searchAnomalyArray(int[] array, int target, int start, int end) {
		int anomalyIndex = findAnomaly(array, start, end);
		int in1 = binarySearch(array, start, anomalyIndex, target);
		int in2 = binarySearch(array, anomalyIndex + 1, end, target);
		return array[in1] == target ? in1 : in2;
	}
	private static int findAnomaly(int[] array, int start, int end) {
		if(end - start == 1) {
			if(array[end] < array[start])
				return start;
			else
				return -1;
		}
		int middle = (start + end)/2;
		if(array[middle] < array[end])
			return findAnomaly(array, start, middle);
		else
			return findAnomaly(array, middle, end);

	}

	/*
	 * Given a sorted array of strings which is interspersed with empty strings,
	 * write a method to find the location of a given string.*/
	private static int searchString(String[] allStrings, String target,
			int start, int end) {
		if(start == end) {
			return allStrings[start].equals(target) ? start : - 1;
		}
		int premiddle = (start + end) /2;
		int postmiddle = premiddle;
		while(postmiddle < end && allStrings[postmiddle].isEmpty()) postmiddle++;
		while(premiddle > start && allStrings[premiddle].isEmpty()) premiddle--;
		if(allStrings[premiddle].equals(target)) return premiddle;
		if(allStrings[postmiddle].equals(target)) return postmiddle;
		boolean isNotPre = allStrings[premiddle].isEmpty() || allStrings[premiddle].
			compareTo(target) < 0;
		boolean isNotPost = allStrings[postmiddle].isEmpty() || allStrings
			[postmiddle].compareTo(target) > 0;
		if(!isNotPre) {
			return searchString(allStrings, target, start, premiddle - 1 <
				start ? start : premiddle - 1);
		} else if (!isNotPost) {
			return searchString(allStrings, target, postmiddle + 1 > end ?
				end : postmiddle + 1, end);
		}else
			return -1;
	}

	/* Binary-based radix sort.*/
	private static List<Integer> radixSort(List<Integer> nums) {
		List<Integer> zeroList = new ArrayList<Integer>();
		List<Integer> oneList = new ArrayList<Integer>();
		for(int i = 0; i < 32; i ++) {
			zeroList.clear();
			oneList.clear();
			for(int num : nums) {
				int l = (num >> i) & 1;
				if(l == 1)
					oneList.add(num);
				else
					zeroList.add(num);
			}
			nums.clear();
			for(int n : zeroList)
				nums.add(n);
			for(int n : oneList)
				nums.add(n);
		}
		return nums;
	}

	private static void testRadixSort() {
		List<Integer> l = new ArrayList<Integer>();
		l.add(4);
		l.add(3);
		l.add(2);
		l.add(1);
		l.add(7);
		radixSort(l);
		for(int i : l)
			System.out.print(i + "<");
	}



	public static void main(String args[]) {
		testRadixSort();
	}

}



























