#include <iostream>

using namespace std;

int n, m;
int mat[7][7];
int xStart, yStart;
bool gasit=false;

int dx[]={2, 2, -2, -2, -1, -1, 1, 1};
int dy[]={1, -1, -1, 1, 2, -2, 2, -2};

void Backtrack(int x, int y, int pas)
{
    mat[x][y]=pas;
    if(pas==n*m && gasit==false)
    {
        gasit=true;
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<m; j++)
                cout<<mat[i][j]<<' ';
            cout<<'\n';
        }
        return;
    }
    for(int i=0; i<8; i++)
        if(x+dx[i]>=0 && x+dx[i]<n && y+dy[i]>=0 && y+dy[i]<m && mat[x+dx[i]][y+dy[i]]==0 && gasit==false)
        {
            Backtrack(x+dx[i], y+dy[i], pas+1);
            mat[x+dx[i]][y+dy[i]]=0;
        }
}

int main()
{
    cin>>n>>m;
    cin>>xStart>>yStart;
    Backtrack(xStart-1, yStart-1, 1);
    return 0;
}
