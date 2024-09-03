#include <iostream>
#include <fstream>

using namespace std;

ifstream in("cladire.in");
ofstream out("cladire.out");

int n, m;
long long cladire[1001][1001];

int main()
{
    in>>n>>m;
    for(int i=0; i<max(n, m); i++)
    {
        cladire[0][i]=1;
        cladire[i][0]=1;
    }
    for(int i=1; i<n; i++)
        for(int j=1; j<m; j++)
            cladire[i][j]=(cladire[i-1][j]+cladire[i][j-1])%9901;
    out<<cladire[n-1][m-1];
    return 0;
}
