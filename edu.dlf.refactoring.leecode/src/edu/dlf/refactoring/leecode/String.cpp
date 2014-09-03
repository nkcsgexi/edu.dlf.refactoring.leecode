#include <iostream>
#include <vector>

using namespace std;



void stringProblem(string s) {
    auto it = s.begin();
    while(it != s.end()) {
        cout<<*it;
        it ++;
    }
    for(int i = 0; i < s.size(); i ++) {
        cout<<s[i];
    }
}

void stringPermutation(string s, int current, string prefix) {
    if(current == s.length()) {
        cout << prefix << endl;
        return ;
    }
    char c = s[current];
    for(int i = 0; i <= prefix.length(); i++){
        auto newPrefix = new char[prefix.length() + 1];
        auto index = 0;
        int start;
        for(start = 0; start < i; start ++) {
            newPrefix[index ++] = prefix[start];
        }
        newPrefix[index ++] = c;
        for(; start < prefix.length(); start ++ ) {
            newPrefix[index ++] = prefix[start];
        }
        stringPermutation(s, current + 1, newPrefix);
    }
}


string getNextString(string s) {
    int i;
    for(i = s.length() - 2; i >= 0; i--) {
        if(s[i] < s[i+1]) break;
    }
    if(i == -1) return "";
    int max = -100;
    char maxChar = 'z';
    for(int j = i + 1; j < s.length(); j ++) {
        if(s[j] > s[i]) {
            if(s[j] <= maxChar) {
                maxChar = s[j];
                max = j;
            }
        }
    }
    char temp = s[i];
    s[i] = s[max];
    s[max] = temp;
    for(int j = i + 1; j < s.length(); j++) {
        char min = 'z';
        int minP = 0;
        for(int k = j; k < s.length(); k ++) {
            if(s[k] <= min) {
                min = s[k];
                minP = k;
            }
        }
        char temp = s[j];
        s[j] = s[minP];
        s[minP] = temp;
    }
    
    return s;
}


void stringPermutationNorecursive(string s) {
    int count = 1;
    for(string t = s; t != ""; t = getNextString(t)) {
        cout << t << " "<< count++ << endl;
    }
}

class Element {
    public:
        bool isValue;
        int value;
        vector<Element> subElements;
};

Element* createElement(string s) {
    if(s[0] != '(' && s[s.length() - 1] != ')') {
        auto v = atoi(s.c_str());
        auto ele = new Element();
        ele->isValue = true;
        ele-> value = v;
        return ele;
    }
    auto start = 1;
    int end;
    int len;
    auto rootEle = new Element();
    rootEle->isValue = false;
    int brackets = 0;
    for(int end = 1; end < s.length() - 1; end ++) {
        if(s[end] == ',' && brackets == 0) {
            len = end-start;
            string sub = s.substr(start, len);
            cout<<sub<<endl;
            rootEle->subElements.insert(rootEle->subElements.end(), 
                *createElement(sub));
            start = end + 1;
        }
        if(s[end] == '(') brackets ++;
        if(s[end] == ')') brackets --;
    }
    len = end - start;
    string sub = s.substr(start, len);
    rootEle->subElements.insert(rootEle->subElements.end(), 
        *createElement(sub));
     //cout<<sub<<endl;
    return rootEle;
}

vector<int>* extendElement(Element e) {
    auto result = new vector<int>();
    if(e.isValue) {
        result->insert(result->begin(), e.value);
        return result;
    } 
    for(Element sub : e.subElements) {
        auto subResult = extendElement(sub);
        result->insert(result->end(), subResult->begin(), 
            subResult->end());
    }
    return result;
}



int main()
{
   auto element = createElement("(1,(2,3),(4,(5,6),7))");
   auto vi = extendElement(*element);
   for (int i : *vi) {
       cout << i<< endl;
   }
   return 0;
}