#include <iostream>
#include <fstream>

using namespace std;

ifstream in("roy-floyd.in");
ofstream out("roy-floyd.out");

int n, m;
int a[105][105];

int main()
{
    in>>n>>m;
    for(int i=1; i<=n; i++)
        for(int j=1; j<=n; j++)
            if(i!=j)
                a[i][j]=-1;
    for(int i=0; i<m; i++)
    {
        int x, y, cost;
        in>>x>>y>>cost;
        a[x][y]=cost;
    }
    for(int i=1; i<=n; i++)
        for(int k=1; k<=n; k++)
            if(i!=k)
                for(int j=1; j<=n; j++)
                    if(i!=j && j!=k)
                        if((a[i][j]>a[i][k]+a[k][j] || a[i][j]==-1) && a[i][k]!=-1 && a[k][j]!=-1)
                            a[i][j]=a[i][k]+a[k][j];
    for(int i=1; i<=n; i++)
    {
        for(int j=1; j<=n; j++)
            out<<a[i][j]<<' ';
        out<<'\n';
    }
    return 0;
}
