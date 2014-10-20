#include<iostream>
#include<ostream>
#include<string>
#include<vector>
#include<climits>
using namespace std;

struct Node{
	Node* left;
	Node* right;
	int value;
	Node(int value) {this->value = value;}
};

int max_bst(Node* root, int min, int max, Node*& child, int& maxSize, Node*&
		maxRoot) {
	if(!root) {
		child = NULL;
		return 0;
	}
	if(root->value > min && root->value < max) {
		int v = root->value;
		child = new Node(v);
		int l = max_bst(root->left, min, v, child->left, maxSize, maxRoot);
		int r = max_bst(root->right, v, max, child->right, maxSize, maxRoot);
		int total = l + r + 1;
		if(total > maxSize) {
			maxSize = total;
			maxRoot = child;
		}
		return total;
	}
	Node* nn;
	max_bst(root, INT_MIN, INT_MAX, nn, maxSize, maxRoot);
	child = NULL;
	return 0;
}

int max_bst_subtree(Node* root, int& min, int& max, Node*& maxTree, int& maxSize) {
	if(!root) return 0;
	int v = root->value;
	int lmin, lmax, rmin, rmax;
	int l = max_bst_subtree(root->left, lmin, lmax, maxTree, maxSize);
	bool isLeftOk = (l != -1) && (l == 0 || lmax < v);
	int r = max_bst_subtree(root->right, rmin, rmax, maxTree, maxSize);
	bool isRightOk = (r != -1) && (r == 0 || lmin > v);
	bool isBST = isLeftOk && isRightOk;
	if(isBST) {
		min = l == 0 ? v : lmin;
		max = r == 0 ? v : rmax;
		int total = l + r + 1;
		if(total > maxSize) {
			maxSize = total;
			maxTree = root;
		}
		return total;
	}
	return -1;
}

void serializeTree(Node* root, ostream& out) {
	if(!root) {out << "#"; return;}
	out << root->value;
	serializeTree(root->left, out);
	serializeTree(root->right, out);
}

void deserializeTree(Node*& root, istream& in) {
	string s;
	in >> s;
	if(s.compare("#")) return;
	deserializeTree(root->left, in);
	deserializeTree(root->right, in);
}

void deserialize_bst(Node*& root, int min, int max, int& next, istream& in) {
	if(next > min && next < max) {
		root = new Node(next);
		string s;
		if(in >> s) {
			next = stoi(s);
			deserialize_bst(root->left, min, next, next, in);
			deserialize_bst(root->right, next, max, next, in);
		}
		return;
	}
	root = NULL;
}

void print_layer(Node* root, int lv) {
	if(!root) return;
	if(lv == 1)
		cout << root->value << " ";
	else {
		print_layer(root->left, lv - 1);
		print_layer(root->right, lv - 1);
	}
}

void layer_visit(Node* root) {
	vector<Node*>* pre = new vector<Node*>();
	vector<Node*>* cur = new vector<Node*>();
	pre->push_back(root);
	while(pre->size() != 0) {
		while(pre->size() != 0) {
			Node* c = pre->front();
			pre->erase(pre->begin());
			if(c -> left) cur->push_back(c->left);
			if(c -> right) cur->push_back(c->right);
		}
		auto t = pre;
		pre = cur;
		cur = t;
	}
}
		

int main(int c, char** args) {
	return 1;
}
