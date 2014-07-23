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
