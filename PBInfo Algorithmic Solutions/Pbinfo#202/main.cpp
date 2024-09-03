#include <iostream>
#include <fstream>

using namespace std;

ifstream in("permpf.in");
ofstream out("permpf.out");

int n;
int sol[10];
int ales[10];

void Backtrack(int lvl)
{
    for(int i=1; i<=n; i++)
    {
        if(!ales[i] && i!=lvl+1)
        {
            sol[lvl]=i;
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
    Backtrack(0);
    return 0;
}
