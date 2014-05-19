package edu.dlf.refactoring.leecode;

public class SortingAlgorithms {
	
	
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
	
	public static int[] heapSort(int[] input) {
		
		return null;
	}
	
	public static void main(String args[]) {
		int[] input = {2, 1, 3, 5, 4};
		input = mergeSort(input, 0, input.length - 1);
		for(int i = 0; i < input.length; i ++) {
			System.out.println(input[i]);
		}
		
	}

}
