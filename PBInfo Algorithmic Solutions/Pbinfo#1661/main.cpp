#include <iostream>

using namespace std;


int main()
{
    int n, m;
    int nrMod[105][105]={0};

    cin>>n>>m;
    for(int i=0; i<=n; i++)
        nrMod[i][0]=1;
    for(int j=0; j<=m; j++)
        nrMod[0][j]=1;
    for(int i=1; i<=n; i++)
        for(int j=1; j<=m; j++)
            nrMod[i][j]=nrMod[i-1][j]+nrMod[i][j-1];
    cout<<nrMod[n][m];
    return 0;
}
