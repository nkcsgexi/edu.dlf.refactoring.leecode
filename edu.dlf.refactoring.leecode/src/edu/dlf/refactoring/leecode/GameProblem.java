package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
		�u� - up 
		�d� - down 
		�l� - left 
		�r� - right 
		�!� - select letter 

		You are given an input string and the length of the rows in the on-screen 
		keyboard. You must produce the sequence of commands needed to type out 
		the input string on the specified keyboard, e.g.: �aci�, 5 -> �!rr!dr!�
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

	private static void sudokuSolver(int[][] grid) {
		// I assume it is 9 * 9
		boolean leaf = true;
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++) {
				if(grid[i][j] < 1 || grid[i][j] > 9) {
					int original = grid[i][j];
					List<Integer> numbers = 
						findPossibleNumbers(grid, i, j);
					for(int n : numbers) {
						grid[i][j] = n;
						sudokuSolver(grid);
					}
					grid[i][j] = original;
					leaf = false;
				}	
			}
		}

		if(!leaf) 
			return;
		System.out.println("solutions:");
		for(int i = 0; i < 9; i ++) {
			for(int j = 0; j < 9; j ++) {
				System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}
	}

	private static List<Integer> findPossibleNumbers(int[][] grid, int i, int j) {
		List<Integer> results = new ArrayList<Integer>();
		HashSet<Integer> forbiddenNumbers = new HashSet<Integer>();
		for(int k = 0; k < 9; k ++) {
			forbiddenNumbers.add(grid[i][k]);
			forbiddenNumbers.add(grid[k][j]);
		}
		int startRow = i / 3 * 3;
		int startColumn = j / 3 * 3;
		for(int r = startRow; r < startRow + 3; r ++)
			for(int c = startColumn; c < startColumn + 3; c ++)
				forbiddenNumbers.add(grid[r][c]);
		for(int num = 1; num <= 9; num ++){
			if(forbiddenNumbers.contains(num))
				continue;
			results.add(num);
		}
		return results;
	}
	
	private static void sudokuSolverTest() {
		int[][] grids = {
				new int[]{7, 0, 0, 0, 2, 6, 0, 0, 8},
				new int[]{4, 0, 0, 3, 5, 7, 0, 6, 0},
				new int[]{0, 0, 6, 0, 1, 8, 0, 9, 0},
				new int[]{0, 3, 0, 0, 9, 4, 7, 0, 0},
				new int[]{9, 0, 0, 0, 0, 0, 0, 0, 4},
				new int[]{0, 0, 2, 6, 7, 0, 0, 8, 0},
				new int[]{0, 8, 0, 7, 6, 0, 9, 0, 0},
				new int[]{0, 7, 0, 2, 3, 1, 0, 0, 5},
				new int[]{5, 0, 0, 8, 4, 0, 0, 0, 3},
		};
		sudokuSolver(grids);
	}
	
	public static void main(String[] args) {
		sudokuSolverTest();
	}
	
}
