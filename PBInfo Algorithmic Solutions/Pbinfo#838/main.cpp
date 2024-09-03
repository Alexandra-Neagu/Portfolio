#include <iostream>
#include <fstream>

using namespace std;

int n;
int cladire[105][105];
int m;
int x1, y1;
bool esteAccesibila=false;
int indCurent;
int total;

int dx[4]={-1, 0, 1, 0};
int dy[4]={0, 1, 0, -1};

void Parcurgere(int x, int y)
{
    cladire[x][y]=2;
    for(int k=0; k<4; k++)
        if(cladire[x+dx[k]][y+dy[k]]==0 && x+dx[k]>=0 && x+dx[k]<n && y+dy[k]>=0 && y+dy[k]<n)
            Parcurgere(x+dx[k], y+dy[k]);
}

int main()
{
    ifstream in("iesire.in");
    ofstream out("iesire.out");

    in>>n;
    in>>m;
    for(int i=0; i<n; i++)
        for(int j=0; j<n; j++)
            in>>cladire[i][j];
    for(int i=0; i<n; i++)
    {
        if(cladire[i][0]==0) Parcurgere(i, 0);
        if(cladire[i][n-1]==0) Parcurgere(i, n-1);
        if(cladire[0][i]==0) Parcurgere(0, i);
        if(cladire[n-1][i]==0) Parcurgere(n-1, i);
    }
    for(int i=0; i<m; i++)
    {
       in>>x1>>y1;
       if(cladire[x1-1][y1-1]==2) out<<"da\n";
       else out<<"nu\n";
    }
    return 0;
}
