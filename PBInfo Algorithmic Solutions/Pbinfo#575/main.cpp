#include <iostream>

using namespace std;

int n, m;
int graf[101][101];
int sumaCol[101];
int maxim;

int main()
{
    cin>>n>>m;
    for(int i=0; i<m; i++)
    {
        int x, y;
        cin>>x>>y;
        if(graf[x][y]==0)
        {
            graf[x][y]=1;
            sumaCol[y]++;
            if(sumaCol[y]>maxim)
                maxim=sumaCol[y];
        }
    }
    for(int i=1; i<=n; i++)
        if(sumaCol[i]==maxim)
            cout<<i<<' ';
    return 0;
}
