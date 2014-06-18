package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.List;

public class RecursiveProblems {
	private static int calculateFibonacci(int n) {
		if(n == 0) return 0;
		if(n == 1) return 1;
		if(n < 0) return -1;
		return calculateFibonacci(n - 1) + calculateFibonacci(n -2);
	}
	
	/* 
	 * Imagine a robot sitting on the upper left hand corner of an NxN grid. 
	 * The robot can only move in two directions: right and down. How many 
	 * possible paths are there for the robot? FOLLOW UP Imagine certain squares 
	 * are “off limits”, such that the robot can not step on them. Design an 
	 * algorithm to get all possible paths for the robot.
	 * */
	private enum Movement{
		Down,
		Right
	}
	
	private static ArrayList<ArrayList<Movement>> getAllPaths(boolean[][] matrix, 
			int i, int j) {
		ArrayList<ArrayList<Movement>> results = new ArrayList<ArrayList<Movement>>();
		if(i == matrix.length - 1 && j == matrix[0].length - 1) {
			return results;
		}
		if(i < matrix.length - 1 && matrix[i + 1][j]) {
			ArrayList<ArrayList<Movement>> paths = getAllPaths(matrix, i + 1, j);
			paths.forEach(p -> p.add(0, Movement.Down));
			results.addAll(paths);
		} 
		if(j < matrix[0].length - 1 && matrix[i][j + 1]) {
			ArrayList<ArrayList<Movement>> paths = getAllPaths(matrix, i, j + 1);
			paths.forEach(p -> p.add(0, Movement.Right));
			results.addAll(paths);
		}
		return results;
	}
	
	/* Get all subsets of a given set.*/
	private static List<List<Integer>> getAllSubsets(List<Integer> numbers) {
		ArrayList<List<Integer>> result = new ArrayList<List<Integer>>();
		if(numbers.size() == 0) {
			result.add(new ArrayList<Integer>());
			return result;
		}
		int current = numbers.get(0);
		List<List<Integer>> remains = getAllSubsets(numbers.subList(1, numbers.
			size()));
		for(List<Integer> re : remains) {
			List<Integer> add = new ArrayList<Integer>();
			add.addAll(re);
			add.add(0, current);
			result.add(add);
			result.add(re);
		}
		return result;
	}
	
	/* Calculate all the permutation of a given string.*/
	private static List<String> getAllPermutation(String s) {
		ArrayList<String> results = new ArrayList<String>();
		if(s.length() == 1) {
			results.add(s);
			return results;
		}
		List<String> subs = getAllPermutation(s.substring(1));
		for(String sub : subs) {
			for(int i = 0; i <= sub.length(); i ++) {
				String before = i == 0 ? "" : sub.substring(0, i);
				String after = i == sub.length() ? "" : sub.substring(i);
				results.add(before + s.charAt(0) + after);
			}
		}
		return results;
	}
	
	/*
	 * Implement an algorithm to print all valid (e.g., properly opened and 
	 * closed) combinations of n-pairs of parentheses. EXAMPLE: input: 3 (e.g., 
	 * 3 pairs of parentheses) output: ()()(), ()(()), (())(), ((())).
	 * */
	private static void printAllParenthesis(int pairs) throws Exception {
		printParens(pairs, pairs).forEach(l -> {
			l.forEach(c -> System.out.print(c));
			System.out.println();
		});;
	}
	private static List<List<Character>> printParens(int left, int right) 
			throws Exception{
		List<List<Character>> result = new ArrayList<List<Character>> ();
		if(left > right) {
			throw new Exception();
		}
		if(left == right && left == 0) {
			result.add(new ArrayList<Character>());
		}
		if(left > 0) {
			printParens(left - 1, right).forEach(l -> {
				l.add(0, '(');
				result.add(l);
			});;
		}
		if(left < right) {
			printParens(left, right - 1).forEach(l -> {
				l.add(0, ')');
				result.add(l);
			});
		}
		return result;
	}
	
	/*
	 * Given an infinite number of quarters (25 cents), dimes (10 cents), nickels 
	 * (5 cents) and pennies (1 cent), write code to calculate the number of 
	 * ways of representing n cents.
	 * */
	private static void printAllCoins(int amount) throws Exception {
		List<Integer> options = new ArrayList<Integer>();
		options.add(25);
		options.add(10);
		options.add(5);
		options.add(1);
		printCoinsInternal(options, amount).forEach(l -> {
			l.forEach(i -> System.out.print(i + " "));
			System.out.println();
		});
	}
	private static List<List<Integer>> printCoinsInternal(List<Integer> options, 
			int amount) throws Exception {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		if(options.size() == 1) {
			if(options.get(0) == 1) {
				List<Integer> temp = new ArrayList<Integer>();
				temp.add(amount);
				result.add(temp);
				return result;
			} else {
				throw new Exception();
			}
		}
		int max = amount/options.get(0);
		for(int i = 0; i <= max; i ++) {
			final int count = i;
			printCoinsInternal(options.subList(1, options.size()), amount - 
					count * options.get(0)).forEach(l -> {
					l.add(0, count);
					result.add(l);
			});
		}
		return result;
	}
	
	/*
	 * Write an algorithm to print all ways of arranging eight queens on a chess 
	 * board so that none of them share the same row, column or diagonal
	 * */
	private static void printAllQueenArrangement() {
		internalPrint(0, new ArrayList<Integer>());
	}
	private static void internalPrint(int row, List<Integer> columnsBefore) {
		List<Integer> columns = getColumns(row, columnsBefore);
		if(row == 7) {
			for(int col : columns) {
				for(int c : columnsBefore) {
					System.out.print(c + " ");
				}
				System.out.println(col);
			}
		}
		for(int col : columns) {
			ArrayList<Integer> newColumns = new ArrayList<Integer>();
			newColumns.addAll(columnsBefore);
			newColumns.add(col);
			internalPrint(row + 1, newColumns);
		}
	}
	
	private static List<Integer> getColumns(int row, List<Integer> columnsBefore) {
		List<Integer> result = new ArrayList<Integer>();
		for(int column = 0; column < 8; column ++) {
			boolean isColumnOk = true;
			if(columnsBefore.contains(column))
				isColumnOk = false;
			for(int i = 0; i < columnsBefore.size(); i ++) {
				int rowDiff = Math.abs(row - i);
				int columnDiff = Math.abs(columnsBefore.get(i) - column);
				if(rowDiff - columnDiff == 0) {
					isColumnOk = false;
				}
			}
			if(isColumnOk)
				result.add(column);
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {
	
	}
}
