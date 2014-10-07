#include<string>	
#include<iostream>

using namespace std;

struct Node {
	Node* left;
	Node* right;
	int value;
	Node(int value) {
		this->value = value;
		this->left = NULL;
		this->right = NULL;
	}
};

struct LinkedNode {
	LinkedNode* next;
	int value;
	LinkedNode(int value) {
		this -> value = value;
		this -> next = NULL;
	}
};

Node* createTree() {
	Node** ns = new Node*[5];
	for(int i = 0; i < 5; i ++)
		ns[i] = new Node(i);
	ns[0]->left = ns[1];
	ns[0]->right = ns[2];
	ns[1]->left = ns[3];
	ns[1]->right = ns[4];
	return ns[0];
}

void convertToDLinkedList(Node* p, Node*& pre, Node*& head) {
	if(!p) return;
	convertToDLinkedList(p->left, pre, head);
	p->left = pre;
	if(pre)
		pre->right = p;
	else 
		head = p;
	Node* right = p->right;
	head->left = p;
	p->right = head;
	pre = p;
	convertToDLinkedList(right, pre, head);
}

void testConvert() {
	Node* root = createTree();
	Node* pre = NULL;
	Node* head = NULL;
	convertToDLinkedList(root, pre, head);
	for(Node* c = head; c-> right != head; c = c->right)
		cout << c->value << endl;
	cout << head->left->value << endl;
}


LinkedNode* getMid(LinkedNode* head, LinkedNode* end) {
	LinkedNode* slow = head;
	LinkedNode* fast = head;
	while(slow && fast && 
			slow->next != end && 
			fast->next->next != end) {
		slow = slow->next;
		fast = fast->next->next;
	}
	return slow;
}

Node* convert2Tree(LinkedNode* node, LinkedNode* end) {
	if(!node) return NULL;
	LinkedNode* mid = getMid(node, end);
	Node* left = convert2Tree(node, mid);
	Node* right = convert2Tree(mid->next, end);
	Node* root = new Node(mid->value);
	root -> left = left;
	root -> right = right;
	return root;
}

int main(int c, const char* args[]) {
	testConvert();
	return 0;
}
