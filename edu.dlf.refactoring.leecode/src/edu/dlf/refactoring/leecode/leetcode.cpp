#include<string>
#include<fstream>
#include<iostream>
#include<climits>
#include<vector>
#include<set>
using namespace std;

struct Node {
	Node* left;
	Node* right;
	Node* nextRight;
	int value;
	Node(int value) {
		this->value = value;
		this->left = NULL;
		this->right = NULL;
	}
};

struct GNode {
	vector<GNode*> neighbors;
	int value;
	GNode(int value) {this->value = value;}
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

void populateNextRight(Node* p) {
	if(!p) return;
	if(p->left) 
		p->left->nextRight = p->right;
	if(p->right)
		p->right->nextRight = p->nextRight ? p->nextRight->left : NULL;
	populateNextRight(p->left);
	populateNextRight(p->right);
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
int findLargestBST(Node* p, int min, int max, Node*& maxBST, int& maxSize,
		Node*& child) {
	if(!p) return 0;
	if(p->value > min && p->value < max) {
		Node* parent = new Node(p->value);
		int cl = findLargestBST(p->left, min, p->value, maxBST, maxSize, 
			child);
		parent->left = cl > 0 ? child : NULL;
		int cr = findLargestBST(p->right, p->value, max, maxBST, maxSize, 
			child);
		parent->right = cr > 0 ? child : NULL;
		child = parent;
		if(maxSize < cl + cr + 1) {
			maxSize = cl + cr + 1;
			maxBST = parent;
		}
		return cl + cr + 1;
	} else {
		findLargestBST(p, INT_MIN, INT_MAX, maxBST, maxSize, child);
		return 0;
	}
}

int findBST(Node* p, int& min, int& max, Node*& maxBST, int& maxSize) {
	if(!p) return 0;
	bool isBST = true;
	int l = findBST(p->left, min, max, maxBST, maxSize);
	int curMin = l == 0 ? p -> value : min;
	if(l == -1 || (l != 0 && p->value <= max))
		isBST = false;
	int r = findBST(p->right, min, max, maxBST, maxSize);
	int curMax = r == 0 ? p -> value : max;
	if(r == -1 || (r != 0 && p->value >= min))
		isBST = false;
	if(isBST) {
		min = curMin;
		max = curMax;
		if(l + r + 1 > maxSize) {
			maxSize = r + l + 1;
			maxBST = p;
		}
		return l + r + 1;
	}
	else 
		return -1;
}

int findLargestBST2(Node* p, int min, int max, Node*& maxBST, int& maxSize, 
		Node*& child) {
	if(!p) return 0;
	int v = p -> value;
	if(v > min && v < max) {
		int left = findLargestBST2(p->left, min, v, maxBST, max, child);
		Node* l = left > 0 ? child : NULL;
		int right = findLargestBST2(p->right, v, max, maxBST, max, child);
		Node* r = right > 0 ? child : NULL;
		Node* np = new Node(v);
		np->left = l;
		np->right = r;
		child = np;
		if(left + right + 1 > max) {
			max = left + right + 1;
			maxBST = np;
		}
		return left + right + 1;
	} else {
		findLargestBST2(p, INT_MIN, INT_MAX, maxBST, max, child);
		return 0;
	}
}
	
bool isBST(Node* p, int& min, int& max) {
	if(!p) {
		min = INT_MAX;
		max = INT_MIN;
		return true;
	}
	if(!isBST(p->left, min, max))
		return false;
	int leftMin = min;
	int leftMax = max;
	if(!isBST(p->right, min, max))
		return false;
	int rightMin = min;
	int rightMax = max;
	if(p->value > leftMax && p->value < leftMin) {
		min = leftMin;
		max = rightMax;
		return true;
	}
	return false;
}

void readBST(Node*& p, int min, int max, int value, ifstream& stream) {
	if(value > min && value < max) {
		int v = value;
		p = new Node(v);
		if(stream >> value) {
			readBST(p->left, min, v, value, stream);
			readBST(p->right, v, max, value, stream);
		}
	}
}
void readBST(ifstream stream) {
	Node* root = NULL;
	int v;
	stream >> v;
	readBST(root, INT_MIN, INT_MAX, v, stream);
}

bool printLayer(Node* p, int l) {
	if(NULL == p) return false;
	if(1 == l) {
		cout << p->value << " ";
		return true;
	}
	bool left = printLayer(p->left, l - 1);
	bool right = printLayer(p->right, l - 1);
	return left | right;
}

void printAllLayers(Node* root) {
	int i = 1;
	while(printLayer(root, i++))
		cout << endl;
}

void testLayers() {
	Node* root = createTree();
	printAllLayers(root);
}

bool isBST2(Node* root, int& val) {
	if(!root) return true;
	if(isBST2(root->left, val)) {
		if(root->value > val) {
			val = root->value;
			return isBST2(root->right, val);
		}
	}
	return false;
}

void writeBT(Node* p, ostream& out) {
	if(!p) {
		out << "#";
		return;
	}
	out << p->value;
	writeBT(p->left, out);
	writeBT(p->right, out);
}

void readBT(Node*& p, ifstream& in) {
	string token;
	in >> token;
	if(token.compare("#") == 0) {
		p = NULL;
	} else {
		p = new Node(stoi(token));
		readBT(p->left, in);
		readBT(p->right, in);
	}
}
	

void writeBST(Node* p, ostream& out) {
	if(!p) return;
	out << p->value;
	writeBST(p->left, out);
	writeBST(p->right, out);
}
void readBST(Node*& p, int min, int max, int& v, istream& in) {
	if(v > min && v < max) {
		p = new Node(v);
		int pv = v;
		if(in >> v) {
			readBST(p->left, min, pv, v, in);
			readBST(p->right, pv, max, v, in);
		}
	}
}

bool isBST2(Node* p, int& min, int& max) {
	if(!p) {
		min = INT_MIN;
		max = INT_MAX;
		return true;
	}
	int lmin, rmax;
	if(isBST2(p->left, lmin, max) && p->value > max
		&& isBST2(p->right, min, rmax)
		&& p->value < min) {
			min = lmin;
			max = rmax;
			return true;
	}
	return false;
}

int find_largest_bst_subtree(Node* p, int& min, int& max, Node*& result, int& size) {
	if(!p) return 0;
	int lmin, lmax, rmin, rmax;
	int l = find_largest_bst_subtree(p->left, lmin, lmax, result, size);
	int r = find_largest_bst_subtree(p->right, rmin, rmax, result, size);
	if(l != -1 && r != -1 && (l == 0 || p->value > lmax) && 
				(r == 0 ||p->value < rmin)) {
	       	min = l != 0 ? lmin : p->value;
		max = r != 0 ? rmax : p->value;
		int total = l + r + 1;
		if(total > size) {
			size = total;
			result = p;
		}
		return total;
	}
	return -1;
}
int find_largest_bst(Node* p, int min, int max, Node*& child, Node*& maxRoot, 
		int& maxSize) {
	if(!p) {
		child = NULL;
		return 0;
	}
	if(p->value > min && p->value < max) {
		Node* root = new Node(p->value);
		int l = find_largest_bst(p->left, min, p->value, root->left, 
			maxRoot, maxSize);
		int r = find_largest_bst(p->right, p->value, max, root->right,
			maxRoot, maxSize);
		int total = l + r + 1;
		if(total > maxSize) {
			maxSize = total;
			maxRoot = root;
		}
		child = root;
		return total;
	}
	find_largest_bst(p, INT_MIN, INT_MAX, child, maxRoot, maxSize);
	child = NULL;
	return 0;
}

LinkedNode* reverst_linked_list(LinkedNode* root) {
	if(!root) return NULL;
	LinkedNode* pre = NULL;
	LinkedNode* current = root;
	do{
		LinkedNode* next = current -> next;
		current -> next = pre;
		pre = current;
		current = next;
	}while(!current);
	return pre;
}

void preorder(Node* root) {
	vector<Node*> stack;
	stack.push_back(root);
	while(stack.size() > 0) {
		Node* n = stack.back();
		stack.pop_back();
		if(n->right) stack.push_back(n->right);
		if(n->left) stack.push_back(n->left);
	}
}

void postOrder(Node* root) {
	vector<Node*> stack;
	stack.push_back(root);
	set<Node*> black;
	while(stack.size() != 0) {
		Node* n = stack.back();
		bool isOK = true;
		if(n->right && black.find(n->right) == black.end()) {
			stack.push_back(n->right);
			isOK = false;
		}
		if(n->left && black.find(n->left) == black.end()) {
			stack.push_back(n->left);
			isOK = false;
		}
		if(isOK) {
			stack.pop_back();
			black.insert(n);
		}
	}
}

void inorder(Node* node) {
	vector<Node*> s;
	set<Node*> black;
	s.push_back(node);
	while(s.size() != 0) {
		Node* n = s.back();
		bool l = true;
		bool visited = false;
		if(n->left && black.find(n->left) == black.end()) {
			s.push_back(n->left);
			l = false;
		}
		if(l) {
			s.pop_back();
			black.insert(n);
			visited = true;
		}
		if(visited && n->right && black.find(n->right) == black.end()) {
			s.push_back(n->right);
		}
	}
}
void df_search(GNode* root) {
	set<GNode*> black, grey;
	vector<GNode*> s;
	s.push_back(root);
	while(s.size() != 0) {
		GNode* n = s.back();
		bool allVisited = true;
		for(auto it = n->neighbors.begin(); it != n->neighbors.end();
				it++) {
			GNode* nei = *it;
			if(black.find(nei) == black.end() &&
				grey.find(nei) == grey.end()) {
					allVisited = false;
					s.push_back(nei);
					grey.insert(nei);
			}
		}
		if(allVisited) {
			s.pop_back();
			black.insert(n);
		}
	}
}

void bf_search(GNode* root) {
	vector<GNode*> q;
	set<GNode*> black;
	set<GNode*> grey;
	q.push_back(root);
	while(q.size() != 0) {
		GNode* n = q.front();
		q.erase(q.begin());
		black.insert(n);
		for(auto it = n->neighbors.begin(); it != n->neighbors.end();
				it ++) {
			GNode* nei = *it;
			if(black.find(nei) == black.end() && grey.find(nei)
					== grey.end())
				grey.insert(nei);
		}
	}
}

bool isPalindrome(int num) {
	int base = 1;
	while(num >= 10 * base) base *= 10;
	while(num != 0) {
		int l = num % 10;
		int h = num / base;
		if(l != h) return false;
		num = (num % base) / 10;
		base /= 100;
	}
	return true;
}

Node* LCA(Node* root, Node* a, Node* b) {
	if(!root) return NULL;
	if(root == a || root == b) return root;
	Node* l = LCA(root->left, a, b);
	Node* r = LCA(root->right, a, b);
	return !l ? r : (!r ? l : root);
}

int main(int c, const char* args[]) {
	testLayers();
	return 0;
}
