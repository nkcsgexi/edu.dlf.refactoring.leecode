package edu.dlf.refactoring.leecode;

import java.util.Stack;

public class StackQueueProblems {
	
	private interface IStack {
		int pop();
		void push(int value);
	}
	
	private interface IQueue {
		void add(int value);
		int remove();
		int peek();
	}
	
	/* Describe how can you implement three stacks using an array*/
	private static class ThreeStacks {
		private final int size = 100;
		private final StackNode[] elements = new StackNode[size];
		private boolean[] isFree = new boolean[size];
		private int[] stackTails = new int[]{-1, -1, -1};
		
		
		private static class StackNode {
			int previous;
			int value;
		}
		
		public int pop(int stackNum) throws Exception {
			if(stackTails[stackNum] == -1) {
				throw new Exception();
			}
			StackNode end = elements[stackTails[stackNum]];
			isFree[stackTails[stackNum]] = true;
			stackTails[stackNum] = end.previous;
			return end.value;
		}
		
		public void push(int stackNum, int value) {
			StackNode node = new StackNode();
			node.value = value;
			int freeSlot = findNextFree();
			elements[freeSlot] = node;
			isFree[freeSlot] = false;
			if(stackTails[stackNum] != -1) {
				node.previous = stackTails[stackNum];
			} else {
				node.previous = -1;
			}
			stackTails[stackNum] = freeSlot;
		}
		
		private int findNextFree() {
			for(int i = 0; i < isFree.length; i ++) {
				if(isFree[i]) {
					return i;
				}
			}
			return isFree.length;
		}
		
		public int peek(int stackNum) {
			return elements[stackTails[stackNum]].value;
		}
	}
	
	/* 
	 * How would you design a stack which, in addition to push and pop, also has 
	 * a function min which returns the minimum element? Push, pop and min should 
	 * all operate in O(1) time
	 * */
	private static class MinStack implements IStack {
		private final int size = 100;
		private int[] elements = new int[size];
		private int pointer = -1;
		private Stack<Integer> minValues = new Stack<Integer>();
		
		@Override
		public int pop() {
			int value = elements[pointer--];
			if(value == minValues.peek()) {
				minValues.pop();
			}
			return value;
		}

		@Override
		public void push(int value) {
			pointer ++;
			elements[pointer] = value;
			if(minValues.peek() <= value)
				minValues.push(value);
		}
		
		public int min() {
			return minValues.peek();
		}
	}
	
	/* 
	 * In the classic problem of the Towers of Hanoi, you have 3 rods and N disks 
	 * of different sizes which can slide onto any tower. The puzzle starts with 
	 * disks sorted in ascending order of size from top to bottom (e.g., each 
	 * disk sits on top of an even larger one). You have the following constraints:
	 * (A) Only one disk can be moved at a time. (B) A disk is slid off the top 
	 * of one rod onto the next rod. (C) A disk can only be placed on top of a 
	 * larger disk. Write a program to move the disks from the first rod to the 
	 * last using Stacks.
	 * */
	private static void printHanoiMovements(int plateCount) {
		printHanoiMovementsInternal(plateCount, 1, 1, 3);
	}
	
	private static void printHanoiMovementsInternal(int plateCount, int startId, 
			int fromPod, int toPod) {
		if(plateCount == 1) {
			System.out.println("move " + startId + " from " + fromPod + 
				" to " + toPod);
			return;
		}
		int borrowPod = 6 - fromPod - toPod;
		printHanoiMovementsInternal(plateCount - 1, startId, fromPod, borrowPod);
		System.out.println("move " + (startId + plateCount - 1) + " from " + 
				fromPod + " to " + toPod);
		printHanoiMovementsInternal(plateCount - 1, startId, borrowPod, toPod);
	}

	
	/* Implement a MyQueue class which implements a queue using two stacks.*/
	private class TwoStackQueue implements IQueue{
		private final Stack<Integer> stack1 = new Stack<Integer>();
		private final Stack<Integer> stack2 = new Stack<Integer>();
		
		@Override
		public void add(int value) {
			stack1.push(value);
		}
		
		@Override
		public int remove() {
			if(stack2.size() > 0)
				return stack2.pop();
			while(!stack1.empty()) 
				stack2.push(stack1.pop());
			return stack2.pop();
		}

		@Override
		public int peek() {
			if(stack2.size() > 0)
				return stack2.peek();
			while(!stack1.empty()) 
				stack2.push(stack1.pop());
			return stack2.peek();
		}
	}
	
	/* 
	 * Write a program to sort a stack in ascending order. You should not make 
	 * any assumptions about how the stack is implemented. The following are 
	 * the only functions that should be used to write this program: push | pop 
	 * | peek | isEmpty
	 * */
	private static Stack<Integer> sortStack(Stack<Integer> stack) {
		Stack<Integer> result = new Stack<Integer>();
		Stack<Integer> temp = new Stack<Integer>();
		while(!stack.empty()) {
			int current = stack.pop();
			while(!result.empty() && result.peek() > current) {
				temp.push(result.pop());
			}
			result.push(current);
			while(!temp.empty()) {
				result.push(temp.pop());
			}
		}
		return result;
	}
	
	private static class SynchronizedSizedQueue {
		private final int[] data;
		private final int length;

		private int start;
		private int end;
		
		public SynchronizedSizedQueue(int length) {
			this.length = length + 1;
			this.data = new int[this.length + 1];
			this.start = 0;
			this.end = 0;
		}
		public synchronized int size() {
			if(end >= start)
				return end - start;
			else
				return end + length - start;
		}

		public synchronized boolean enqueue(int t) {
			int originalEnd = end;
			end = end == length - 1 ? 0 : end + 1;
			if(size() == 0) {
				end = originalEnd;
				return false;
			}
			data[originalEnd] = t;
			return true;
		}

		public synchronized int dequeue() {
			if(size() == 0)
				return Integer.MIN_VALUE;
			int r = data[start];
			start = start + 1 == length ? 0 : start + 1;
			return r;
		}
	}
	
	private static void testSizedQueue() {
		SynchronizedSizedQueue queue = new SynchronizedSizedQueue(10);
		for(int i = 1; i <= 10; i ++)
			System.out.println(i + ": " + queue.enqueue(i));
		for(int i = 1; i <= 4; i ++)
			System.out.println(i + ": " + queue.dequeue());
		for(int i = 1; i <= 4; i ++)
			System.out.println(i + ": " + queue.enqueue(i));
		for(int i = 1; i <= 10; i ++)
			System.out.println(i + ": " + queue.dequeue());
	}
		
	
	
	public static void main(String args[]) {
		testSizedQueue();
	}
	
	
}
