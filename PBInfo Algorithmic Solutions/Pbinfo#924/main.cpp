#include <iostream>

using namespace std;

int a[]={9,273,63,83,93,123};

int multiplu(int v[], int n, int k)
{
    if(n==1)
    {
        if(v[n-1]%k==0 && v[n-1]%10==k) return 1;
        return 0;
    }
    else
    {
        if(v[n-1]%k==0 && v[n-1]%10==k) return 1 + multiplu(v, n-1, k);
        return multiplu(v, n-1, k);
    }
}

int main()
{
    cout<<multiplu(a, 6, 3);
    return 0;
}
