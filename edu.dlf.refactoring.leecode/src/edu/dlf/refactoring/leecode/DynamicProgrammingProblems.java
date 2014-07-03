package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.List;

public class DynamicProgrammingProblems {
	
	private static int getMaximumSubArray(int[] array) {
		int length = array.length;
		List<Integer> starts = new ArrayList<Integer>();
		List<Integer> ends = new ArrayList<Integer>();
		starts.add(0);
		for(int i = 0; i < length; i ++) {
			if(array[i] < 0) {
				starts.add(i + 1 < length ? i + 1 : length - 1);
				ends.add(i - 1 >= 0 ? i - 1 : 0);
			}	
		}
		ends.add(length - 1);
		int max = Integer.MIN_VALUE;
		for(int s : starts) {
			for(int e : ends) {
				if(e >= s) {
					int sum = 0;
					for(int index = s; index <= e; index ++) {
						sum += array[index];
					}
					max = sum > max ? sum : max;
				}
			}
		} 
		return max;
	}

	private static int maxSplitRope(int length) {
		int[][] matrix = new int[length][length];
		for(int i = 0; i < length - 1; i ++) {
			matrix[i][i + 1] = 2;
		}
		for(int layer = 2; layer < length; layer ++) {
			for(int i = 0; i < length - layer; i ++) {
				int j = i + layer;
				int max = Integer.MIN_VALUE;
				for(int middle = i + 1; middle < j; middle ++) {
					max = Math.max(matrix[i][middle] + matrix
						[middle][j], max);
				}
				matrix[i][j] = Math.max(max, layer * layer + 1);
			}
		}
		return matrix[0][length - 1];
	}

	private static int maximizeStealing(int[] values) {
		int length = values.length;
		int[][] matrix = new int[length][length];
		for(int i = 0; i < length; i ++) {
			matrix[i][i] = values[i];
		}
		for(int layer = 1; layer < length; layer++) {
			for(int row = 0; row < length - layer; row ++) {
				int column = row + layer;
				int max = Integer.MIN_VALUE;
				for(int skip = row; skip <= column; skip ++) {
					int first = skip == row ? 0 : 
						matrix[row][skip - 1];
					int second = skip == column? 0 : 
						matrix[skip + 1][column];
					max = Integer.max(first + second, max);
				}
				matrix[row][column] = max;
			}
		}
		return matrix[0][length - 1];
	}

	public static void main(String[] args) {
	/*	System.out.println(getMaximumSubArray(new int[]{1, -2, 3, 10, -4, 7, 
			2, -5}));*/
		System.out.println(maxSplitRope(8));
		System.out.println(maximizeStealing(new int[]{6, 1, 2, 7}));
	}
}
