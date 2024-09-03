#include <fstream>

using namespace std;

ifstream in("cladire5.in");
ofstream out("cladire5.out");

int n, m;
int mat[204][204];
int S;

int main()
{
    in>>n>>m;
    in>>S;
    for(int i=1; i<=n; i++)
        for(int j=1; j<=m; j++)
            in>>mat[i][j];
    for(int i=0; i<=max(n,m); i++)
    {
        mat[i][m+1]=10000000;
        mat[0][i]=10000000;
    }
    for(int i=1; i<=n; i++)
        for(int j=m; j>=1; j--)
            if(j!=m || i!=1)
                mat[i][j]+=min(mat[i][j+1], mat[i-1][j]);
    out<<S-mat[n][1];
    return 0;
}
