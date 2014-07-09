package edu.dlf.refactoring.leecode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class LinkedListProblems {

	private static class LinkedNode {
		private LinkedNode next;
		private int value;
	}
	
	/* 
	 * Write code to remove duplicates from an unsorted linked list. 
	 * How would you solve this problem if a temporary buffer is not allowed?
	 * */
	private static LinkedNode removeDuplicates(LinkedNode head) {
		if(head == null || head.next == null)
			return head;
		LinkedNode current = head.next;
		LinkedNode preCurrent = head;
		while(current != null) {
			LinkedNode prior = head;
			while(prior != current) {
				if(prior.value == current.value) {
					preCurrent.next = current.next;
					current = preCurrent;
					break;
				}
			}
			preCurrent = current;
			current = current.next;
		}
		return head;
	}
	
	/* Implement an algorithm to find the nth to last element of a singly 
	 * linked list.*/
	private static LinkedNode getN2LastNode(LinkedNode head, int n) {
		LinkedNode tail = head;
		LinkedNode current = head;
		for(int count = 0; count < n; count++) {
			tail = tail.next;
		}
		while(tail != null) {
			current = current.next;
			tail = tail.next;
		}
		return current;
	}
	
	/*
	* Implement an algorithm to delete a node in the middle of a single linked list, 
	* given only access to that node. EXAMPLE Input: the node �c� from the linked 
	* list a->b->c->d->e Result: nothing is returned, but the new linked list looks 
	* like a->b->d->e
	*/
	private static void removeNodeInMiddle(LinkedNode node) {
		LinkedNode prior = null;
		while(node.next != null) {
			node.value = node.next.value;
			prior = node;
			node = node.next;
		}
		prior.next = null;
	}
	
	/*
	* You have two numbers represented by a linked list, where each node contains a 
	* single digit. The digits are stored in reverse order, such that the 1�s digit 
	* is at the head of the list. Write a function that adds the two numbers and 
	* returns the sum as a linked list. EXAMPLE Input: (3 -> 1 -> 5), (5 -> 9 -> 2) 
	* Output: 8 -> 0 -> 8 
	* */
	private static LinkedNode add2Numbers(LinkedNode num1, LinkedNode num2) {
		LinkedNode head = null;
		LinkedNode previousNode = null;
		int carry = 0;
		while(num1 != null || num2 != null) {
			LinkedNode node = new LinkedNode();
			int addition = (num1 == null ? 0 : num1.value) + (num2==null? 0 : 
				num2.value) + carry;
			node.value = addition % 10;
			carry = addition / 10;
			num1 = num1 == null ? null : num1.next;
			num2 = num2 == null ? null : num2.next;
			if(previousNode == null) 
				head = node;
			else 
				previousNode.next = node;
			previousNode = node;
		}
		return head;
	}
	
	/* 
	 * Given a circular linked list, implement an algorithm which returns node 
	 * at the beginning of the loop. DEFINITION Circular linked list: A 
	 * (corrupt) linked list in which a node�s next pointer points to an earlier 
	 * node, so as to make a loop in the linked list. EXAMPLE Input: A -> B -> 
	 * C -> D -> E -> C [the same C as earlier] Output: C
	 * */
	private static LinkedNode getLoopStart(LinkedNode head) {
		LinkedNode fast = head, slow = head;
		while(fast != slow) {
			slow = slow.next;
			fast = fast.next.next;
		}
		slow = head;
		while(fast != slow) {
			fast = fast.next;
			slow = slow.next;
		}
		return slow;
	}

	private static void swapKthFromStartAndFromEnd(LinkedNode head, int k) {
		LinkedNode fastPointer = head;
		for(int i = 1; i < k; i ++) {
			fastPointer = fastPointer.next;
			if(fastPointer == null) {
				return;
			}
		}
		LinkedNode before = fastPointer;
		LinkedNode slowPointer = head;
		while(fastPointer.next != null) {
			slowPointer = slowPointer.next;
			fastPointer = fastPointer.next;
		}
		LinkedNode after = slowPointer;
		int temp = after.value;
		after.value = before.value;
		before.value = temp;
	}

	private static LinkedNode constructLinkedList(int[] numbers) {
		if(numbers.length == 0)
			return null;
		LinkedNode head = new LinkedNode();
		head.value = numbers[0];
		LinkedNode before = head;
		for(int i = 1; i < numbers.length; i++) {
			LinkedNode node = new LinkedNode();
			node.value = numbers[i];
			before.next = node;
			before = node;
		}
		return head;
	}
	
	private static void printLinkedList(LinkedNode node) {
		for(LinkedNode n = node; n != null; n = n.next) {
			System.out.print(n.value + "->");
		}
		System.out.println();
	}
	
	/*
	 * Given a linked list: 5 -> 4 -> 3 -> 2 -> 1, produce the following output: 
	 * 4 -> 2 -> 0 -> 2 -> 1 by substracting the 1st node with nth node, the 2nd 
	 * with nth -1 node, etc... Only apply the stated action on the first half 
	 * of the list
	 * */
	private static void subtract(LinkedNode head) {
		int length =0;	
		for(LinkedNode temp = head; temp != null; temp = temp.next, length ++);
		LinkedNode fast = head;
		LinkedNode slow = head;
		int count = length / 2;
		for(int i = 0; i < count; fast = fast.next, i ++); 
		if(length % 2 == 1)  {
			fast.value = 0;
			fast = fast.next;
		}
		Stack<LinkedNode> first = new Stack<LinkedNode>();
		Queue<LinkedNode> second = new LinkedList<LinkedNode>();
		for(int i =0; i < count; i ++) {
			first.push(fast);
			second.add(slow);
			fast = fast.next;
			slow = slow.next;
		}
		while(first.size() != 0) {
			LinkedNode f = first.pop();
			LinkedNode s = second.remove();
			s.value = s.value - f.value;
		}
	}

	private static boolean hasLoop(LinkedNode head) {
		LinkedNode fast = head, slow = head;
		while(fast != null) {
			if(fast.next == null)
				break;
			fast = fast.next.next;
			slow = slow.next;
			if(fast == slow)
				return true;
		}
		return false;
	}
	
	private static LinkedNode[] splitLinkedList(LinkedNode head) {
		LinkedNode fast = head, slow = head;
		LinkedNode preSlow = null;
		while(fast != null && fast.next != null) {
			fast = fast.next.next;
			preSlow = slow;
			slow = slow.next;
		}
		if(fast == null) {
			preSlow.next = null;
			return new LinkedNode[]{head, slow};
		}else {
			LinkedNode temp = slow.next;
			slow.next = null;
			return new LinkedNode[]{head, temp};
		}
	}

	private static void testSplitLinkedList() {
		int[] nums = new int[]{1,2,3,4,5,6,7,8,9};
		LinkedNode[] heads = splitLinkedList(constructLinkedList(nums));
		printLinkedList(heads[0]);
		printLinkedList(heads[1]);
		nums = new int[]{1,2,3,4,5,6};
		heads = splitLinkedList(constructLinkedList(nums));
		printLinkedList(heads[0]);
		printLinkedList(heads[1]);
	}

	public static void main(String[] args) {
		testSplitLinkedList();
	}


	

}
