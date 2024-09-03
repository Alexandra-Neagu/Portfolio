#include <iostream>
#include <fstream>

using namespace std;

ifstream in("bazine.in");
ofstream out("bazine.out");

int n, m;
int mat[101][101];
int viz[101];
int compConexe=1;

void DFS(int index)
{
    viz[index]=compConexe;
    for(int i=1; i<=n; i++)s
        if(mat[i][index]==1 && viz[i]==0)
            DFS(i);
}

int main()
{
    in>>n>>m;
    for(int i=0; i<m; i++)
    {
        int x, y;
        in>>x>>y;
        mat[x][y]=mat[y][x]=1;
    }
    for(int i=1; i<=n; i++)
        if(viz[i]==0)
        {
            DFS(i);
            compConexe++;
        }
    out<<compConexe-1;
    return 0;
}
