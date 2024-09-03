#include <iostream>
#include <fstream>
#include <algorithm>

//da 80 pct

using namespace std;

ifstream in("partial.in");
ofstream out("partial.out");

int n;
int graf[201][201];
int suma[201];
int muchii;

int main()
{
    in>>n;
    while(!in.eof())
    {
        int x, y;
        in>>x>>y;
        graf[x][y]=graf[y][x]=1;
        suma[x]++;
        muchii++;
    }
    muchii/=2;
    for(int i=1; i<=n; i++)
        for(int j=i; j<=n; j++)
            if(suma[i]>1 && graf[i][j]==1)
            {
                if(muchii==0)
                    break;
                graf[i][j]=graf[j][i]=0;
                suma[i]--;
                muchii--;
            }
    for(int i=1; i<=n; i++)
    {
        for(int j=1; j<=n; j++)
            out<<graf[i][j]<<' ';
        out<<'\n';

    }
    return 0;
}
