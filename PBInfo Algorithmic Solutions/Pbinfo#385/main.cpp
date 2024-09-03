#include <fstream>

using namespace std;

ifstream in("sumtri.in");
ofstream out("sumtri.out");

int n;
int mat[102][102];
int maxim;

int main()
{
    in>>n;
    for(int i=0; i<n; i++)
        for(int j=0; j<=i; j++)
            in>>mat[i][j];
    for(int i=1; i<n; i++)
        for(int j=0; j<=i; j++)
            mat[i][j]+=max(mat[i-1][j], mat[i-1][j-1]);
    for(int j=0; j<n; j++)
        if(mat[n-1][j]>maxim)
            maxim=mat[n-1][j];
    out<<maxim;
    return 0;
}
