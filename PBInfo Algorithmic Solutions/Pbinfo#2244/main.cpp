#include <iostream>
#include <fstream>

using namespace std;

ifstream in("traseucalmax.in");
ofstream out("traseucalmax.out");

struct Punct
{
    int x, y;
};

int n, m;
int mat[11][11];
Punct sol[101];
Punct solFinala[101];
int pasMax;

int dx[]={2, 2, -2, -2, -1, -1, 1, 1};
int dy[]={1, -1, -1, 1, 2, -2, 2, -2};

void Backtrack(int x, int y, int pas)
{
    mat[x][y]=pas;
    sol[pas-1].x=x;
    sol[pas-1].y=y;
    if(x==n-1 && y==m-1)
    {
        if(pas>pasMax)
        {
            pasMax=pas;
            for(int i=0; i<pas; i++)
                solFinala[i]=sol[i];
        }
        return;
    }
    for(int i=0; i<8; i++)
    {
        if(x+dx[i]>=0 && x+dx[i]<n && y+dy[i]>=0 && y+dy[i]<m && mat[x+dx[i]][y+dy[i]]==0)
        {
            Backtrack(x+dx[i], y+dy[i], pas+1);
            mat[x+dx[i]][y+dy[i]]=0;
        }
    }

}

int main()
{
    in>>n>>m;
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
            in>>mat[i][j];
    Backtrack(0, 0, 1);
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
            if(mat[i][j]==1)
                mat[i][j]=0;
    for(int i=0; i<pasMax; i++)
        mat[solFinala[i].x][solFinala[i].y]=i+1;
    for(int i=0; i<n; i++)
    {
        for(int j=0; j<m; j++)
            out<<mat[i][j]<<' ';
        out<<'\n';
    }
    return 0;
}
