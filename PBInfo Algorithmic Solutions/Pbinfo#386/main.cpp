#include <fstream>

using namespace std;

ifstream in("sumtri1.in");
ofstream out("sumtri1.out");

int n;
int mat[102][102];
int matPreluc[102][102];

int main()
{
    in>>n;
    for(int i=0; i<n; i++)
        for(int j=0; j<=i; j++)
        {
            in>>mat[i][j];
            matPreluc[i][j]=mat[i][j];
        }
    for(int i=n-2; i>=0; i--)
        for(int j=i; j>=0; j--)
            matPreluc[i][j]+=min(matPreluc[i+1][j], matPreluc[i+1][j+1]);
    out<<matPreluc[0][0]<<'\n';
    int x=0, y=0;
    while(x<n)
    {
        out<<mat[x][y]<<' ';
        matPreluc[x][y]-=mat[x][y];
        if(matPreluc[x][y]==matPreluc[x+1][y])
            x++;
        else if(matPreluc[x][y]==matPreluc[x+1][y+1])
        {
            x++;
            y++;
        }
    }
    return 0;
}
