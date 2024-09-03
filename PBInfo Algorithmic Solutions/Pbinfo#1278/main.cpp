#include <iostream>
#include <fstream>

using namespace std;

ifstream in("produscartezian2.in");
ofstream out("produscartezian2.out");

int n;
int v[8];
int sol[8];

void Backtrack(int lvl)
{
    for(int i=0; i<v[lvl]; i++)
    {
        sol[lvl]=i+1;
        if(lvl+1==n)
        {
            for(int j=0; j<n; j++)
                out<<sol[j]<<' ';
            out<<'\n';
        }
        else
            Backtrack(lvl+1);
    }
}

int main()
{
    in>>n;
    for(int i=0; i<n; i++)
        in>>v[i];
    Backtrack(0);
    return 0;
}
