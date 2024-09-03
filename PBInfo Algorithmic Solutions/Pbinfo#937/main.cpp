#include <iostream>
#include <fstream>

using namespace std;

ifstream in("hercule.in");
ofstream out("hercule.out");

int n, m;
int mat[11][11];
int ctr;

int dx[]={-1, 0, 1, 0};
int dy[]={0, 1, 0, -1};

void Backtrack(int x, int y)
{
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
            mat[i][j]--;
    int val=mat[x][y];
    mat[x][y]=0;
    if(x==n-1 && y==m-1)
    {
        ctr++;
        mat[x][y]=val;
        return;
    }
    for(int i=0; i<4; i++)
    {
        if(x+dx[i]>=0 && x+dx[i]<n && y+dy[i]>=0 && y+dy[i]<m && mat[x+dx[i]][y+dy[i]]>1)
        {
            Backtrack(x+dx[i], y+dy[i]);
            for(int k=0; k<n; k++)
                for(int j=0; j<m; j++)
                    mat[k][j]++;
        }
    }
    mat[x][y]=val;
}

int main()
{
    in>>n>>m;
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
        {
            in>>mat[i][j];
            mat[i][j]++;
        }
    Backtrack(0, 0);
    out<<ctr;
    return 0;
}
