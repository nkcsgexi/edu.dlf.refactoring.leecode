#include<iostream>
#include<climits>
using namespace std;

struct Node {
	Node* left;
	Node* right;
	int value;
	Node(int value) {this-> value = value;}
};

void convertBST2LinkedList(Node* p, Node*& pre, Node*& head) {
	if(!p) return;
	convertBST2LinkedList(p->left, pre, head);
	p->left = pre;
	if(pre)
		pre->right = p;
	else
		head = p;
	Node* right = p -> right;
	p->right = head;
	head->left = p;
	pre = p;
	convertBST2LinkedList(right, pre, head);
}

int largestBST(Node* p, int min, int max, Node*& child, int& maxSize,
		Node*& maxBST) {
	if(!p) return 0;
	if(p->value > min && p -> value < max) {
		Node* np = new Node(p->value);
		int l = largestBST(p->left, min, p->value, child, maxSize,
				maxBST);
		if(l != 0) np->left = child;
		int r = largestBST(p->right, p->value, max, child, maxSize,
				maxBST);
		if(r != 0) np->right = child;
		if(l + r + 1 > maxSize) {
			maxSize = l + r + 1;
			maxBST = np;
		}
		child = np;
		return l + r + 1;
	}
	largestBST(p, INT_MIN, INT_MAX, child, maxSize, maxBST);
	return 0;
}

int largestSubtreeBST(Node* p, int& min, int& max, Node*& maxBST, int& maxSize) {
	if(!p) return 0;
	bool isBST = true;
	int left = largestSubtreeBST(p->left, min, max, maxBST, maxSize);
	int lmin = left == 0 ? p->value : min;
	if(left == -1 || (left != 0 && max < p->value)) isBST = false;
	int right = largestSubtreeBST(p->right, min, max, maxBST, maxSize);
	int rmax = right == 0 ? p->value : max;
	if(right == -1 || (right != 0 && min > p->value)) isBST = false;
	if(isBST) {
		min = lmin;
		max = rmax;
		int total = left + right + 1;
		if(total > maxSize) {
			maxSize = total;
			maxBST = p;
		}
		return total;
	}
	return -1;
}


int main(int c, char** args) {
	cout << "hi" <<endl;
}
