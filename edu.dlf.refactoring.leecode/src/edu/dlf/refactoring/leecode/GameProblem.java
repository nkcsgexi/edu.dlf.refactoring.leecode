package edu.dlf.refactoring.leecode;

public class GameProblem {

	/*
	 * There are n coins in a line. (Assume n is even). Two players take turns to 
	 * take a coin from one of the ends of the line until there are no more coins 
	 * left. The player with the larger amount of money wins. Would you rather go 
	 * first or second? Does it matter? Assume that you go first, describe an 
	 * algorithm to compute the maximum amount of money you can win.
	 * */
	private static int winMax(int[] input) {
		int matrix[][] = new int[input.length][input.length];
		for(int i = 0; i< input.length; i ++) {
			matrix[i][i] = input[i];
			if(i < input.length-1)
				matrix[i][i +1] = Math.max(input[i], input[i + 1]);
		}
		for(int gap = 2; gap < input.length; gap ++) {
			for(int i = 0; i < input.length - gap; i ++){
				int j = i + gap;
				int situation1 = input[j] + sumSubarray(input, i, j) 
					- matrix[i][j-1];
				int situation2 = input[i] + sumSubarray(input, i + 1, j + 1) 
					- matrix[i+1][j];
				matrix[i][j] = Math.max(situation1, situation2);
			}
		}
		return matrix[0][input.length - 1];
	}
	
	private static int sumSubarray(int[] input, int start, int end) {
		int sum = 0;
		for(int i = start; i < end; i ++) {
			sum += input[i];
		}
		return sum;
	}
	
	public static void main(String[] args) {
		int[] input = new int[]{3, 2, 2, 3, 1, 2};
		int result = winMax(input);
		System.out.println(result);
	}
	
}
