#include <iostream>

using namespace std;

int n, m;
int graf[101][101];
int sumaLin[101], sumaCol[101];
int nrNoduri;

int main()
{
    cin>>n>>m;
    for(int i=0; i<m; i++)
    {
        int x, y;
        cin>>x>>y;
        graf[x][y]=1;
        sumaLin[x]++;
        sumaCol[y]++;
    }
    for(int i=1; i<=n; i++)
        if(sumaLin[i]==sumaCol[i])
            nrNoduri++;
    cout<<nrNoduri<<'\n';
    for(int i=1; i<=n; i++)
        if(sumaLin[i]==sumaCol[i])
            cout<<i<<' ';
    return 0;
}
