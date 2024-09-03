#include <iostream>
#include <fstream>

using namespace std;

ifstream in("BFS.in");
ofstream out("BFS.out");

int n, m, x;
int mat[101][101];
int sol[101];
bool viz[101]={false};
int total=1;

void BFS(int indexCur)
{
    for(int i=1; i<=n; i++)
        if(mat[i][sol[indexCur]]==1 && viz[i]==false)
        {
            sol[total++]=i;
            viz[i]=true;
        }
    if(indexCur<total)
        BFS(indexCur+1);
}

int main()
{
    in>>n>>m>>x;
    for(int i=0; i<m; i++)
    {
        int x, y;
        in>>x>>y;
        mat[x][y]=mat[y][x]=1;
    }
    sol[0]=x;
    viz[x]=true;
    BFS(0);
    for(int i=0; i<total; i++)
        out<<sol[i]<<' ';
    return 0;
}
