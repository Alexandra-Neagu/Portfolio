#include <iostream>
#include <fstream>

using namespace std;

int n, m;
int harta[105][105];

int maxSuma, maxim, xMax, yMax;
int maxRegiune, sumaRegiune, xReg, yReg;

int dx[4]={-1, 0, 1, 0};
int dy[4]={0, 1, 0, -1};

void Fill(int x, int y, int &maxReg, int &sumaReg, int &xReg, int &yReg)
{
    sumaRegiune+=harta[x][y];
    if(harta[x][y]>maxReg)
    {
        maxReg=harta[x][y];
        xReg=x;
        yReg=y;
    }
    harta[x][y]=0;
    for(int i=0; i<4; i++)
        if(x+dx[i]>=0 && x+dx[i]<n && y+dy[i]>=0 && y+dy[i]<m && harta[x+dx[i]][y+dy[i]]!=0)
            Fill(x+dx[i], y+dy[i], maxReg, sumaReg, xReg, yReg);
}

int main()
{
    ifstream in("oaste.in");
    ofstream out("oaste.out");

    in>>n>>m;
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
            in>>harta[i][j];
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
        {
            if(harta[i][j]!=0)
            {
                sumaRegiune=0;
                maxRegiune=0;
                xReg=i;
                yReg=j;
                Fill(i, j, maxRegiune, sumaRegiune, xReg, yReg);
                if(sumaRegiune>maxSuma)
                {
                    maxSuma=sumaRegiune;
                    maxim=maxRegiune;
                    xMax=xReg;
                    yMax=yReg;
                }
                else if(sumaRegiune==maxSuma)
                {
                    if(maxRegiune>maxim)
                    {
                        maxim=maxRegiune;
                        xMax=xReg;s
                        yMax=yReg;
                    }
                }
            }
        }
    out<<maxim<<' '<<xMax+1<<' '<<yMax+1;
    return 0;
}
