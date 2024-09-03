#include <iostream>

using namespace std;

int n;
int v[100001];
int aux[100001];

void mergeArrays(int s, int f)
{
    int ind=0;
    int l=s;
    int mij0=(s+f)/2;
    int j=mij0+1;
    while(l<=mij0 && j<=f)
    {
        if(v[l] < v[j]) aux[ind++]=v[l++];
        else aux[ind++]=v[j++];
    }
    for(int i=l; i<=mij0; i++)
        aux[ind++]=v[i];
    for(int i=j; i<=f; i++)
        aux[ind++]=v[i];
    for(int i=s; i<=f; i++)
        v[i]=aux[i-s];
}

void mergeSort(int s, int f)
{
    if(s<f)
    {
        int mij=(s+f)/2;
        mergeSort(s, mij);
        mergeSort(mij+1, f);
        mergeArrays(s, f);
    }
}

int main()
{
    cin>>n;
    for(int i=0; i<n; i++)
        cin>>v[i];
    mergeSort(0, n-1);
    for(int i=0; i<n; i++)
        cout<<v[i]<<' ';
    return 0;
}
