#include <iostream>

using namespace std;

void P(int v[], int n, int &minim, int &maxim, int &suma)
{
    if(n==1)
    {
        minim=v[n-1];
        maxim=v[n-1];
        suma=v[n-1];
    }
    else
    {
        P(v, n-1, minim, maxim, suma);
        if(v[n-1]<minim) minim=v[n-1];
        if(v[n-1]>maxim) maxim=v[n-1];
        suma+=v[n-1];
    }
}

int main()
{

    return 0;
}
