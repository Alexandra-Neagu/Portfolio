#include <iostream>
#include <fstream>

using namespace std;

ifstream in("submultimi.in");
ofstream out("submultimi.out");

int n;
int sol[11];

void Backtrack(int lvl)
{
    int i;
    if(lvl==0)
        i=0;
    else
        i=sol[lvl-1];
    for(; i<n; i++)
    {
        sol[lvl]=i+1;
        for(int j=0; j<=lvl; j++)
            out<<sol[j]<<' ';
        out<<'\n';
        if(lvl+1<n)
            Backtrack(lvl+1);
    }
}

int main()
{
    in>>n;
    Backtrack(0);
    return 0;
}
