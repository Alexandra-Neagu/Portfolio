#include <iostream>
#include <fstream>

using namespace std;

ifstream in("taxa.in");
ofstream out("taxa.out");

struct Matrice
{
    int val;
    bool trecut=false;
};

int n, m;
Matrice mat[1001][1001];
int minim=1000;
int plata;

void Parcurgere(int &suma, int x, int y)
{
    suma+=mat[x][y].val;
    mat[x][y].trecut=true;
    if(suma>=minim)
        return;
    if(x==n-1)
    {
        if(suma<minim)
            minim=suma;
        return;
    }
    if(x<n-1 && mat[x+1][y].trecut==false)
    {
        Parcurgere(suma, x+1, y);
        mat[x+1][y].trecut=false;
    }
    if(y<m-1 && mat[x][y+1].trecut==false)
    {
        Parcurgere(suma, x, y+1);
        mat[x][y+1].trecut=false;
    }
    if(y>0 && mat[x][y-1].trecut==false)
    {
        Parcurgere(suma, x, y-1);
        mat[x][y-1].trecut=false;
    }
}

int main()
{
    in>>n>>m;
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
            in>>mat[i][j].val;
    for(int j=0; j<m; j++)
    {
        plata=0;
        Parcurgere(plata, 0, j);;
    }
    out<<minim;
    return 0;
}
