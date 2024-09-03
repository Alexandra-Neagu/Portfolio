#include <iostream>
#include <fstream>
#include <climits>

using namespace std;

struct Coada
{
    int x,y;
    int val;
};

int n,m;
int palat[1005][1005];
char celula;
Coada coada[1010025];
int startX, startY;
int minim=INT_MAX;
int minX, minY;
int indCurent, total;

int dx[4]={1, 0, -1, 0};
int dy[4]={0, 1, 0, -1};

void Fill(int x, int y, int val)
{
    if(palat[x][y]==0) palat[x][y]=val;
    else if(palat[x][y]==INT_MAX-1) palat[x][y]=-val;
    for(int l=0; l<4; l++)
        if(x+dx[l]>=0 && x+dx[l]<n && y+dy[l]>=0 && y+dy[l]<m)
            if(palat[x+dx[l]][y+dy[l]]==0 || palat[x+dx[l]][y+dy[l]]==INT_MAX-1)
            {
                coada[total].x=x+dx[l];
                coada[total].y=y+dy[l];
                coada[total].val=val+1;
                total++;
            }
}

int main()
{
    ifstream in("palat.in");
    ofstream out("palat.out");

    in>>n>>m;
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
        {
            in>>celula;
            if(celula=='Z') palat[i][j]=INT_MAX;
            else if(celula=='F') palat[i][j]=INT_MAX-1;
            else if(celula=='_') palat[i][j]=0;
            else if(celula=='I')
            {
                palat[i][j]=0;
                startX=i;
                startY=j;
            }
        }
    coada[0].x=startX;
    coada[0].y=startY;
    coada[0].val=0;
    indCurent=0;
    total=1;
    while(indCurent<total)
    {
        Fill(coada[indCurent].x, coada[indCurent].y, coada[indCurent].val);
        indCurent++;
        //cout<<indCurent<<' '<<total<<"\n";
    }
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
            if(palat[i][j]<0)
                if(-palat[i][j]<=minim)
                {
                  minim=-palat[i][j];
                  minX=i+1;
                  minY=j+1;
                }
    out<<minX<<' '<<minY;
    return 0;
}
