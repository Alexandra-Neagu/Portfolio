#include <iostream>
#include <fstream>

using namespace std;

ifstream in("soarece1.in");
ofstream out("soarece1.out");

int n, m;
char matChar[11][11];
int mat[11][11];
int xStart, yStart;
int xFinal, yFinal;
bool gasit=false;

int dx[]={-1, 0, 1, 0};
int dy[]={0, 1, 0, -1};

void Backtrack(int x, int y, int pas)
{
    if(gasit)
        return;
    mat[x][y]=pas;
    if(x==xFinal && y==yFinal)
    {
        gasit=true;
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<m; j++)
            {
                if(mat[i][j]==-1)
                    out<<0<<' ';
                else
                    out<<mat[i][j]<<' ';
            }
            out<<'\n';
        }
        return;
    }
    for(int i=0; i<4; i++)
        if(x+dx[i]>=0 && x+dx[i]<n && y+dy[i]>=0 && y+dy[i]<m && mat[x+dx[i]][y+dy[i]]==0)
        {
            Backtrack(x+dx[i], y+dy[i], pas+1);
            mat[x+dx[i]][y+dy[i]]=0;
        }
}

int main()
{
    in>>n>>m;
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
        {
            in>>matChar[i][j];
            if(matChar[i][j]=='_')  mat[i][j]=0;
            else if(matChar[i][j]=='#') mat[i][j]=-1;
            else if(matChar[i][j]=='S')
            {
                xStart=i;
                yStart=j;
            }
            else if(matChar[i][j]=='B')
            {
                xFinal=i;
                yFinal=j;
            }
        }
    Backtrack(xStart, yStart, 1);
    if(gasit==false)
    {
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<m; j++)
                out<<0<<' ';
            out<<'\n';
        }

    }
    return 0;
}
