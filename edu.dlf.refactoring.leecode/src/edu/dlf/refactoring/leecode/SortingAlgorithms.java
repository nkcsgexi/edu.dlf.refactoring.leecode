package edu.dlf.refactoring.leecode;


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
	
	
	public static void main(String args[]) {
		int[] A = {1, 3, 5, 7, 0, 0, 0};
		int[] B = {2, 4, 8};
		mergeArrays(A, B, 4);
		for(int a : A) {
			System.out.print(a + " ");
		}
	}

}
