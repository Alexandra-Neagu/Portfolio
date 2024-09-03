#include <iostream>
#include <fstream>

using namespace std;

ifstream in("soarece.in");
ofstream out("soarece.out");

int n, m;
int mat[11][11];
int xStart, yStart;
int xFinal, yFinal;
int ctr;

int dx[]={-1, 0, 1, 0};
int dy[]={0, 1, 0, -1};

void Backtrack(int x, int y)
{
    mat[x][y]=2;
    if(x==xFinal-1 && y==yFinal-1)
    {
        ctr++;
        return;
    }
    for(int i=0; i<4; i++)
    {
        if(x+dx[i]>=0 && x+dx[i]<n && y+dy[i]>=0 && y+dy[i]<m && mat[x+dx[i]][y+dy[i]]==0)
        {
            Backtrack(x+dx[i], y+dy[i]);
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
    in>>xStart>>yStart;
    in>>xFinal>>yFinal;

    Backtrack(xStart-1, yStart-1);
    out<<ctr;
    return 0;
}
