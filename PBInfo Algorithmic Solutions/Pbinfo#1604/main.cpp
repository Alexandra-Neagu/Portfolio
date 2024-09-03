#include <iostream>
#include <fstream>

using namespace std;

ifstream in("dmin.in");
ofstream out("dmin.out");

int n, m;
int k;
int graf[101][101];
int coada[101], preced[101], viz[101];

int main()
{
    in>>n>>m;
    for(int i=1; i<=m; i++)
    {
        int x, y;
        in>>x>>y;
        graf[x][y]=graf[y][x]=1;
    }
    in>>k;
    while(k)
    {
        int x, y;
        in>>x>>y;

        for(int i=1; i<=n; i++)
            coada[i]=preced[i]=viz[i]=0;
        int pr=0, ult=1;
        coada[pr]=x;
        viz[x]=1;
        while(pr<=ult)
        {
            for(int i=1; i<=n; i++)
                if(graf[i][coada[pr]]==1 && viz[i]!=1)
                {
                    coada[ult++]=i;
                    viz[i]=1;
                    preced[i]=coada[pr];
                    if(coada[ult-1]==y)
                        break;
                }
            if(coada[ult-1]==y)
                break;
            pr++;
        }
        int lung=0;
        for(int nod=y; preced[nod]!=0; nod=preced[nod])
            lung++;
        out<<lung<<'\n';

        k--;
    }
    return 0;
}
