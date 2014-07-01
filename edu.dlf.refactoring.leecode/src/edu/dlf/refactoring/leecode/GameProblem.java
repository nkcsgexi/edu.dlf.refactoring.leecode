package edu.dlf.refactoring.leecode;

public class GameProblem {

	/*
	 * There are n coins in a line. (Assume n is even). Two players take turns to 
	 * take a coin from one of the ends of the line until there are no more coins 
	 * left. The player with the larger amount of money wins. Would you rather go 
	 * first or second? Does it matter? Assume that you go first, describe an 
	 * algorithm to compute the maximum amount of money you can win.
	 * */
	private static int winCoinMax(int[] input) {
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
	
	/*You are trying to control an on-screen keyboard (e.g. on a television) 
	 * that looks like this: 

		a b c d e 
		f g h i j 
		... 

		You can issue the following commands to move the cursor and select letters: 
		‘u’ - up 
		‘d’ - down 
		‘l’ - left 
		‘r’ - right 
		‘!’ - select letter 

		You are given an input string and the length of the rows in the on-screen 
		keyboard. You must produce the sequence of commands needed to type out 
		the input string on the specified keyboard, e.g.: “aci”, 5 -> “!rr!dr!”
	*/	
	private static void getMove(int N, String s ) {
		int previousRow = 0;
		int previousColumn = 0;
		for( char c : s.toCharArray()) {
			int row = (c - 'a') / N;
			int column = (c - 'a') % N;
			printMove(previousRow, previousColumn, row, column);
			previousRow = row;
			previousColumn = column;
		}
	}

	private static void printMove(int pR, int pC, int r, int c) {
		while(pR != r) {
			if(pR > r) {
				System.out.print("u"); 
				pR --;
			} else {
				System.out.print("d");
				pR ++;
			} 
		}
		while(pC != c) {
			if(pC > c) {
				System.out.print("l");
				pC --;
			} else {
				System.out.print("r");
				pC ++;
			}
		}
		System.out.println("!");
	}


	
	public static void main(String[] args) {
		getMove(5, "aci");
	}
	
}
