package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.List;

public class MathProblems {
	/* 
	 * Design an algorithm to find the kth number such that the only prime 
	 * factors are 3, 5, and 7
	 * */
	private static int getKthNumber(int k) {
		int count = 1;
		for(; k > 0; k -= getPow(count ++, 3));
		count --;
		k += getPow(count, 3);
		int[] index = new int[]{count, 0, 0};
		for(int i = 0; i < k; i ++) {
			int source = index[1] > 0 ? 1 : 0;
			int dest = source + 1;
			index[source] --;
			index[dest] ++;
		}
		return 0;
	}
	
	private static int getPow(int index, int base) {
		int result = 1;
		for(int i = 0; i < index; i ++){
			result *= base; 
		}
		return result;
	}
	
	private static void getAllCombination(int addition, int count, List<List<Integer>> 
			results, List<Integer> previous) {
		if(count == 1) {
			previous.add(addition);
			results.add(previous);
			return;
		}
		for(int current = addition; current >=0; current --) {
			List<Integer> list = cloneList(previous);
			list.add(current);
			getAllCombination(addition - current, count - 1, results, list);
		}
	}
	
	private static List<Integer> cloneList(List<Integer> list) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Integer num : list) {
			result.add(num);
		}
		return result;
	}
	
	public static void main(String[] args) {
		ArrayList<List<Integer>> results = new ArrayList<List<Integer>>();
		getAllCombination(6, 3, results, new ArrayList<Integer>());
		for(List<Integer> list : results) {
			for(Integer i : list) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
	}
	 
	
}
