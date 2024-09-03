#include <iostream>
#include <fstream>

using namespace std;

int n, m;
int harta[105][105];

int xMos, yMos;
int contorSpiridusi;

int dx[4]={-1, 0, 1, 0};
int dy[4]={0, 1, 0, -1};

void Fill(int x, int y)
{
    if(harta[x][y]==3) contorSpiridusi++;
    harta[x][y]=0;
    for(int i=0; i<4; i++)
        if(x+dx[i]>=0 && x+dx[i]<n && y+dy[i]>=0 && y+dy[i]<m && harta[x+dx[i]][y+dy[i]]!=0)
            Fill(x+dx[i], y+dy[i]);
}

int main()
{
    ifstream in("moscraciun.in");
    ofstream out("moscraciun.out");

    in>>n>>m;
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
        {
            in>>harta[i][j];
            if(harta[i][j]==2)
            {
                xMos=i;
                yMos=j;
            }
        }
    Fill(xMos, yMos);
    out<<contorSpiridusi;
    return 0;
}
