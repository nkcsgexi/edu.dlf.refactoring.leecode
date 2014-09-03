#include <stdio.h> 
#include <stdlib.h>   
#include <time.h> 
#include <iostream>

using namespace std;


void swap(int* nums, int a, int b) {
    int temp = nums[a];
    nums[a] = nums[b];
    nums[b] = temp;
}

void positionWell(int* nums, int start, int end) {
    if(end < start)
        return;
    int pivot = *(nums + end);
    int current = start;
    for(int i = start; i < end; i ++) {
        if(*(nums + i) <= pivot) {
            swap(nums, i, current);
            current ++;
        }
    }
    swap(nums, current, end);
    positionWell(nums, start, current - 1);
    positionWell(nums, current + 1, end);
}

void quickSort(int* nums, int len) {
    positionWell(nums, 0, len - 1);
}

void printString(char* c, int len) {
	for(int i = 0; i < len; i++) 
		cout<<c[i];
	cout<<endl;
}

void stringPermutation(char* s, int start, int end, char *prefix) {
	if(start > end) {
		printString(prefix, end);
		return;
	}
	char c = s[start];
	for(int p = 0; p < start + 1; p ++) {
		char* next = new char[start + 1];
		next[p] = c;
		for(int i = 0, j = 0; i < start +1; i++) {
			if(i != p) {
				next[i] = prefix[j];
				j++;
			}
		}
		stringPermutation(s, start + 1, end, next);
	}
}


int main()
{
    int size = 100;
    int* nums = new int[size];
    int* original = new int[size];
    for(int i = 0; i < size; i++) {
        original[i] = nums[i] = rand() % 100; 
        cout<<nums[i]<<endl;
    }
     cout<<"============================="<<endl;
    quickSort(nums, size);
    for(int i = 0; i < size; i++) {
        cout<<nums[i]<<endl;
    }
   return 0;
}
