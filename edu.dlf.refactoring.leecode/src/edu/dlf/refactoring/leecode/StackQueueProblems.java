package edu.dlf.refactoring.leecode;

public class StackQueueProblems {
	
	private interface IStack {
		int pop();
		void push(int value);
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
		int[] elements = new int[size];
		int[] mins = new int[size];
		int pointer = -1;
		
		@Override
		public int pop() {
			return elements[pointer--];
		}

		@Override
		public void push(int value) {
			pointer ++;
			elements[pointer] = value;
			mins[pointer] = Math.min(mins[pointer - 1], value);
		}
		
		public int min() {
			return mins[pointer];
		}
	}
}
