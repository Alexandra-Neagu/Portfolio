#include <iostream>

using namespace std;

struct Copil
{
    int index;
    int height;
};

int n;
Copil copii[1001];
Copil aux[1001];

void mergeArrays(int s, int f)
{
    int ind=0;
    int l=s;
    int mij0=(s+f)/2;
    int j=mij0+1;
    while(l<=mij0 && j<=f)
    {
        if(copii[l].height < copii[j].height)
        {
            aux[ind].height=copii[l].height;
            aux[ind++].index=copii[l++].index;
        }
        else
        {
            aux[ind].height=copii[j].height;
            aux[ind++].index=copii[j++].index;
        }
    }
    for(int i=l; i<=mij0; i++)
    {
        aux[ind].height=copii[i].height;
        aux[ind++].index=copii[i].index;
    }
    for(int i=j; i<=f; i++)
    {
        aux[ind].height=copii[i].height;
        aux[ind++].index=copii[i].index;
    }
    for(int i=s; i<=f; i++)
        copii[i]=aux[i-s];
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
    {
        cin>>copii[i].height;
        copii[i].index=i+1;
    }
    mergeSort(0, n-1);
    for(int i=0; i<n; i++)
        cout<<copii[i].index<<' ';
    return 0;
}
