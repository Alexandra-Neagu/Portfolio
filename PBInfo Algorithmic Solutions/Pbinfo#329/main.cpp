#include <iostream>
#include <fstream>

using namespace std;

ifstream in("bila.in");
ofstream out("bila.out");

int n, m;
int mat[22][22];
int iStart, jStart;
int pozMax;

int dx[]={-1, 0, 1, 0};
int dy[]={0, 1, 0, -1};


void Backtrack(int x, int y, int nrPoz)
{
    nrPoz++;
    if(x==0 || x==n-1 || y==0 || y==m-1)
    {
        if(nrPoz>pozMax)
            pozMax=nrPoz;
        return;
    }
    for(int i=0; i<4; i++)
        if(x+dx[i]>=0 && x+dx[i]<n && y+dy[i]>=0 && y+dy[i]<m)
            if(mat[x+dx[i]][y+dy[i]]<mat[x][y])
                Backtrack(x+dx[i], y+dy[i], nrPoz);
}

int main()
{
    in>>n>>m;
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
            in>>mat[i][j];
    in>>iStart>>jStart;
    Backtrack(iStart-1, jStart-1, 0);
    out<<pozMax;
    return 0;
}
