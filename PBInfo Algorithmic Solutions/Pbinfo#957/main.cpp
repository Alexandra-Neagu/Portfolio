#include <iostream>
#include <fstream>

using namespace std;

struct Coada
{
    int x, y;
};

int n, m;
int nrCadouri;
int xCadou, yCadou;
int cladire[185][185];

int cadouriMax;
int camereMax;

Coada coada[34225];
int indCurent;
int total;

int dx[4]={-1, 0, 1, 0};
int dy[4]={0, 1, 0, -1};

void Parcurgere()
{
    int x=coada[indCurent].x;
    int y=coada[indCurent].y;

    for(int i=0; i<4; i++)
    {
        if(cladire[x+dx[i]][y+dy[i]]==0 && x+dx[i]>=0 && x+dx[i]<n && y+dy[i]>=0 && y+dy[i]<m)
        {
            cladire[x+dx[i]][y+dy[i]]=cladire[x][y]+1;
            coada[total].x=x+dx[i];
            coada[total].y=y+dy[i];
            total++;
        }
        else if(cladire[x+dx[i]][y+dy[i]]<0 && x+dx[i]>=0 && x+dx[i]<n && y+dy[i]>=0 && y+dy[i]<m)
        {
            if(-cladire[x+dx[i]][y+dy[i]]>cadouriMax)
            {
                cadouriMax=-cladire[x+dx[i]][y+dy[i]];
                camereMax=1;
            }
            else if(-cladire[x+dx[i]][y+dy[i]]==cadouriMax && cadouriMax!=0)
            {
                camereMax++;
                //cout<<camereMax<<' '<<cadouriMax<<' '<<x+dx[i]<<' '<<y+dy[i]<<"\n";
            }
            cladire[x+dx[i]][y+dy[i]]=cladire[x][y]+1;
        }
    }
}

int main()
{


    ifstream in("zana.in");
    ofstream out("zana.out");

    in>>n>>m;
    in>>nrCadouri;
    for(int i=0; i<nrCadouri; i++)
    {
        in>>xCadou>>yCadou;
        cladire[n-xCadou][yCadou-1]--;
    }
    cladire[n-1][0]=1;
    coada[indCurent].x=n-1;
    coada[indCurent].y=0;
    total++;
    while(indCurent<total)
    {
        Parcurgere();
        indCurent++;
    }
    out<<cadouriMax<<"\n"<<camereMax;
    return 0;
}
