package edu.dlf.refactoring.leecode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class MathProblems {
	/* 
	 * Design an algorithm to find the kth number such that the only prime 
	 * factors are 3, 5, and 7
	 * */
	private static int getKthNumber(int k) {
		Queue<Integer> q3 = new LinkedList<Integer>();
		Queue<Integer> q5 = new LinkedList<Integer>();
		Queue<Integer> q7 = new LinkedList<Integer>();
		q3.add(3);
		q5.add(5);
		q7.add(7);
		int value = 0;
		for(int i = 0; i < k; i ++) {
			value = Math.min(q3.peek(), Math.min(q5.peek(), q7.peek()));
			if(value == q3.peek()) {
				q3.remove();
				q3.add(value * 3);
				q5.add(value * 5);
				q7.add(value * 7);	
			}else if(value == q5.peek()) {
				q5.remove();
				q5.add(value * 5);
				q7.add(value * 7);
			} else {
				q7.remove();
				q7.add(value * 7);
			}
		}
		return value;
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
		for(int current = addition; current >= 0; current --) {
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
	private static List<Integer> getAllPrimeNumbersBefore(int N) {
		List<Integer> result = new ArrayList<Integer>();
		for(int i = 2; i < N; i ++) {
			boolean flag = true;
			for(int factor = 2; factor < N/2; factor ++) {
				if(i % factor == 0) {
					flag = false;
					break;
				}
			}
			if(flag){
				result.add(i);
			}
		}
		return result;
	}
	
	/* Generate random number by weight. */
	private static int calcualteRandomNumber(int[] weight) {
		int total = 0;
		for(int w : weight) {
			total += w;
		}
		Random random = new Random();
		int raw = random.nextInt(total);
		int i = 0;
		while(raw >= 0) {
			raw -= weight[i++];
		}
		return i - 1;
	}

	/* How many ways to filing a board of 2*N with brickes of 2*1.*/
	private static int calcualteWaysToFillBoard(int N) {
		int pre = 2;
		int prepre = 1;
		for(int length = 3; length != N; length ++) {
			int current = pre + prepre;
			prepre = pre;
			pre = current;
		}
		return pre + prepre;
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

		for(int i = 1; i < 100; i ++) {
			System.out.println(getKthNumber(i));
		}
	}
	 
	
}
