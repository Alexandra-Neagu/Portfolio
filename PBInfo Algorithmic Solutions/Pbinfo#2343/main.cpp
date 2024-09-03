#include <iostream>
#include <fstream>

using namespace std;

//nu este o rezolvare corecta

struct Bec
{
    int x, y;
    int consum;
    int copaciLuminati;
};

int n, m;
int p;
int k;
int x, y, consum;
int padure[155][155];

int copaciLuminatiMax;
int xMax, yMax;
int indice;

bool gasit=false;

Bec becuri[105];

int main()
{
    ifstream in("bec.in");
    ofstream out("bec.out");

    in>>p;
    in>>n>>m;
    in>>k;

    for(int i=0; i<k; i++)
    {
        in>>x>>y>>consum;
        padure[x-1][y-1]=consum;
        becuri[i].x=x-1;
        becuri[i].y=y-1;
        becuri[i].consum=consum;
        becuri[i].copaciLuminati=n*m-k;
    }
    for(int i=0; i<n; i++)
    {
        int x=becuri[i].x;
        int y=becuri[i].y;

        for(int j=y+2; j<m; j++)
            if(padure[x][y]==0)
                becuri[i].copaciLuminati--;
        for(int j=y-2; j>=0; j--)
            if(padure[x][y]==0)
                becuri[i].copaciLuminati--;
        for(int j=x+2; j<n; j++)
            if(padure[x][y]==0)
                becuri[i].copaciLuminati--;
        for(int j=x-2; j>=0; j--)
            if(padure[x][y]==0)
                becuri[i].copaciLuminati--;

    }
    for(int i=0; i<k; i++)
    {
        if(becuri[i].copaciLuminati>copaciLuminatiMax)
        {
            copaciLuminatiMax=becuri[i].copaciLuminati;
            xMax=becuri[i].x+1;
            yMax=becuri[i].y+1;
            indice=i;
        }
        else if(becuri[i].copaciLuminati==copaciLuminatiMax)
            if(becuri[i].consum<becuri[indice].consum && !gasit)
            {
                xMax=becuri[i].x+1;
                yMax=becuri[i].y+1;
                indice=i;
                gasit=true;
            }
    }
    if(p==1) out<<copaciLuminatiMax; //<- nu da bine, nu stiu de ce
    else if(p==2) out<<xMax<<' '<<yMax;
    return 0;
}
