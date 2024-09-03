#include <iostream>
#include <fstream>

using namespace std;

ifstream in("cladire2.in");
ofstream out("cladire2.out");

struct Punct
{
    int x, y;
};

int n, m;
int mat[202][202];
int bombo[202][202];
Punct drum[400002];
int ind;

int main()
{
    in>>n>>m;
    for(int i=1; i<=n; i++)
        for(int j=1; j<=m; j++)
        {
            in>>mat[i][j];
            bombo[i][j]=mat[i][j];
        }
    for(int i=1; i<=n; i++)
        for(int j=1; j<=m; j++)
            bombo[i][j]=bombo[i][j]+max(bombo[i-1][j], bombo[i][j-1]);
    out<<bombo[n][m]<<'\n';
    int x=n;
    int y=m;
    drum[ind].x=x;
    drum[ind++].y=y;
    while(x!=1 || y!=1)
    {
        bombo[x][y]-=mat[x][y];
        if(bombo[x][y-1]==bombo[x][y] && y-1>=1)
                y--;
        else if(bombo[x-1][y]==bombo[x][y] && x-1>=1)
                x--;
        drum[ind].x=x;
        drum[ind++].y=y;
    }
    for(int i=ind-1; i>=0; i--)
        out<<drum[i].x<<' '<<drum[i].y<<'\n';
    return 0;
}
