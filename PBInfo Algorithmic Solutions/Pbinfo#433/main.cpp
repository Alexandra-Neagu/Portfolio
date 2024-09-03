#include <fstream>

using namespace std;

ifstream in("cladire3.in");
ofstream out("cladire3.out");

int n, m;
int mat[204][204];

int main()
{
    in>>n>>m;
    for(int i=1; i<=n; i++)
        for(int j=1; j<=m; j++)
            in>>mat[i][j];
    for(int i=0; i<=max(n,m); i++)
    {
        mat[i][0]=10000000;
        mat[n+1][i]=10000000;
    }
    for(int i=n; i>=1; i--)
        for(int j=1; j<=m; j++)
            if(j!=1 || i!=n)
                mat[i][j]+=min(mat[i][j-1], mat[i+1][j]);
    out<<mat[1][m]<<'\n';
    return 0;
}
