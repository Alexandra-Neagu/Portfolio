#include <iostream>
#include <fstream>

using namespace std;

ifstream in("produscartezian1.in");
ofstream out("produscartezian1.out");

int n, m;
int sol[7];

void Backtrack(int lvl)
{
    for(int i=0; i<n; i++)
    {
        sol[lvl]=i+1;
        if(lvl+1==m)
        {
            for(int j=0; j<m; j++)
                out<<sol[j]<<' ';
            out<<'\n';
        }
        else
            Backtrack(lvl+1);
    }
}

int main()
{
    in>>n>>m;
    Backtrack(0);
    return 0;
}
