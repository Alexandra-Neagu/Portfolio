#include <iostream>
#include <cmath>

using namespace std;

int n;
int mat[1050][1050];

void Fill(int nr, int xStart, int yStart, int xFinal, int yFinal)
{
    for(int i=xStart; i<xFinal; i++)
        for(int j=yStart; j<yFinal; j++)
            mat[i][j]=1;
    if(nr>=0)
    {
        Fill(nr-1, xFinal, yStart, xFinal+pow(2, nr-1), yStart+pow(2, nr-1));
        Fill(nr-1, xStart, yFinal, xStart+pow(2, nr-1), yFinal+pow(2, nr-1));
        Fill(nr-1, xFinal, yFinal, xFinal+pow(2, nr-1), yFinal+pow(2, nr-1));
    }
}

int main()
{
    int latura;

    cin>>n;
    latura=1<<n;
    Fill(n-1, 0, 0, latura/2, latura/2);
    for(int i=0; i<latura; i++)
    {
        for(int j=0; j<latura; j++)
            cout<<mat[i][j]<<' ';
        cout<<"\n";
    }
    return 0;
}
