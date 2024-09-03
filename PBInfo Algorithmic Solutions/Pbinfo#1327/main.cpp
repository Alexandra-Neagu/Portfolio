#include <iostream>
#include <fstream>
#include <algorithm>

using namespace std;

ifstream in("sirpie.in");
ofstream out("sirpie.out");

int n;
int nr[11];
int sol[11];
int ales[11]={0};


int VerifPrime(int a, int b)
{
    while(a!=b)
    {
        if(a>b)
            a-=b;
        else
            b-=a;
    }
    return a;
}

void Backtrack(int lvl)
{
    for(int i=0; i<n; i++)
    {
        if(ales[i]==0)
            if(lvl==0 || VerifPrime(nr[i], sol[lvl-1])==1)
            {
                sol[lvl]=nr[i];
                ales[i]=1;
                if(lvl+1==n)
                {
                    for(int j=0; j<n; j++)
                        out<<sol[j]<<' ';
                    out<<'\n';
                }
                else
                    Backtrack(lvl+1);
                ales[i]=0;
            }
    }
}

int main()
{
    in>>n;
    for(int i=0; i<n; i++)
        in>>nr[i];
    sort(nr, nr+n);
    Backtrack(0);
    return 0;
}
