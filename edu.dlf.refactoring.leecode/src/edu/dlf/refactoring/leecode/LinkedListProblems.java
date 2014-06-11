package edu.dlf.refactoring.leecode;


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
	* given only access to that node. EXAMPLE Input: the node ‘c’ from the linked 
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
	* single digit. The digits are stored in reverse order, such that the 1’s digit 
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
	 * (corrupt) linked list in which a node’s next pointer points to an earlier 
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
}
