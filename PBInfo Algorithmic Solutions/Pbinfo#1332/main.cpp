#include <iostream>
#include <fstream>

using namespace std;

ifstream in("traseucal.in");
ofstream out("traseucal.out");

int n, m;
int mat[11][11];
bool gasit=false;

int dx[]={2, 2, -2, -2, -1, -1, 1, 1};
int dy[]={1, -1, -1, 1, 2, -2, 2, -2};

void Backtrack(int x, int y, int pas)
{
    mat[x][y]=pas;
    if(gasit)
        return;
    if(x==n-1 && y==m-1)
    {
        gasit=true;
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<m; j++)
                out<<mat[i][j]<<' ';
            out<<'\n';
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
    if(gasit==false)
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<m; j++)
                out<<mat[i][j]<<' ';
            out<<'\n';
        }
    return 0;
}
