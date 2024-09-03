#include <iostream>
#include <fstream>

using namespace std;

ifstream in("dijkstra.in");
ofstream out("dijkstra.out");

int n;
int nodStart;
int a[105][105];
int cost[105];
int viz[105];
int coada[105];
int index;
int noduriRamase;


int main()
{
    in>>n>>nodStart;
    int x, y, pret;
    noduriRamase=n;
    while(in>>x>>y>>pret)
        a[x][y]=pret;
    for(int i=1; i<=n; i++)
        cost[i]=1000000;
    cost[nodStart]=0;
    while(noduriRamase)
    {
        int minim=1000000;
        int urmNod;
        for(int i=1; i<=n; i++)
            if(cost[i]<minim && viz[i]==0)
            {
                minim=cost[i];
                urmNod=i;
            }
        for(int i=1; i<=n; i++)
            if(cost[urmNod]+a[urmNod][i]<cost[i] && a[urmNod][i]!=0)
                cost[i]=cost[urmNod]+a[urmNod][i];
        viz[urmNod]=1;
        noduriRamase--;
    }
    for(int i=1; i<=n; i++)
    {
        if(cost[i]==1000000)
            cost[i]=-1;
        out<<cost[i]<<' ';
    }
    return 0;
}
