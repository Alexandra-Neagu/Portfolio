#include <iostream>
#include <fstream>

using namespace std;

ifstream in("lungimeminima.in");
ofstream out("lungimeminima.out");

int n, p, L;
int graf[101][101];
int nrNoduri;
int noduri[101];
int viz[101];
int coada[101];

int main()
{
    int index=0;
    int pr=0, ult=1;
    in>>n>>p>>L;
    while(!in.eof())
    {
        int x, y;
        in>>x>>y;
        graf[x][y]=graf[y][x]=1;
    }

    coada[pr]=p;
    viz[p]=1;
    while(pr<=ult)
    {
        for(int i=1; i<=n; i++)
            if(graf[i][coada[pr]]==1 && viz[i]==0)
            {
                coada[ult++]=i;
                viz[i]=viz[coada[pr]]+1;
            }
        pr++;
    }
    for(int i=1; i<=n; i++)
        if(viz[i]-1==L)
        {
            nrNoduri++;
            noduri[index++]=i;
        }
    out<<nrNoduri<<'\n';
    for(int i=0; i<index; i++)
        out<<noduri[i]<<' ';
    return 0;
}
