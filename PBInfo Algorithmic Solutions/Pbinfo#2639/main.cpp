#include <iostream>
#include <fstream>

using namespace std;

ifstream in("radiera.in");
ofstream out("radiera.out");

int main()
{
    int n;
    int sir[1005];
    int lungMax[1005];
    int maxim;
    int maximTotal=0;

    in>>n;
    for(int i=0; i<n; i++)
        in>>sir[i];
    lungMax[0]=1;
    for(int i=1; i<n; i++)
    {
        maxim=0;
        for(int j=0; j<i; j++)
            if(sir[j]<=sir[i])
                if(lungMax[j]>maxim)
                    maxim=lungMax[j];
        lungMax[i]=maxim+1;
    }
    for(int i=0; i<n; i++)
    {
        cout<<lungMax[i]<<' ';
        if(lungMax[i]>maximTotal)
            maximTotal=lungMax[i];
    }
    out<<n-maximTotal;
    return 0;
}
