#include <iostream>

using namespace std;

double recursive(int n, double v[], double k)
{
    if(n==1)
    {
        if(v[n-1]==k) return n-1;
        else return -1;
    }
    else
    {
        if(v[n-1]==k) return n-1;
        return recursive(n-1, v, k);
    }
}

int main()
{

    return 0;
}
