#include <iostream>
#include <fstream>

using namespace std;

struct Coada
{
    int x, y;
};

int n;
int nrCopaci;
int xCopac, yCopac;
int x1, y1, x2, y2;
int parc[180][180];

int indCurent;
int indTotal;
Coada coada[32400];

int dx[4]={-1, 0, 1, 0};
int dy[4]={0, 1, 0, -1};

void Parcurgere()
{
    int x=coada[indCurent].x;
    int y=coada[indCurent].y;

    for(int i=0; i<4; i++)
    {
        if(parc[x+dx[i]][y+dy[i]]==0 && x+dx[i]>=1 && x+dx[i]<=n && y+dy[i]>=1 && y+dy[i]<=n)
        {
            parc[x+dx[i]][y+dy[i]]=parc[x][y]+1;
            coada[indTotal].x=x+dx[i];
            coada[indTotal].y=y+dy[i];
            indTotal++;

        }
    }
}

int main()
{
    ifstream in("alee.in");
    ofstream out("alee.out");

    in>>n;
    in>>nrCopaci;
    for(int i=0; i<nrCopaci; i++)
    {
        in>>xCopac>>yCopac;
        parc[xCopac][yCopac]=-1;
    }
    in>>x1>>y1>>x2>>y2;
    parc[x1][y1]=1;
    coada[indCurent].x=x1;
    coada[indCurent].y=y1;
    indTotal=1;
    //Parcurgere();
    while(indCurent<=indTotal)
    {
        Parcurgere();
        indCurent++;
    }
    out<<parc[x2][y2];
    return 0;
}
