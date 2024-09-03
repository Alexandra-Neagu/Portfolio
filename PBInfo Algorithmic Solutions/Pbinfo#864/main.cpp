#include <iostream>
#include <queue>
#include <fstream>

using namespace std;

int n, m;
int mat[1005][1005];
int x1, y1, x2, y2;

int dx[4]={-1, 0, 1, 0};
int dy[4]={0, 1, 0, -1};

queue < pair < int, int > > coada;

void Lee()
{
    int x, y;
    while(!coada.empty())
    {
        x=coada.front().first;
        y=coada.front().second;
        coada.pop();
        for(int i=0; i<4; i++)
        {
            if(x+dx[i]>=0 && x+dx[i]<n && y+dy[i]>=0 && y+dy[i]<m && mat[x+dx[i]][y+dy[i]]==0)
            {
                mat[x+dx[i]][y+dy[i]]=mat[x][y]-1;
                coada.push(make_pair(x+dx[i], y+dy[i]));
            }
        }

    }
}

int main()
{
    ifstream in("roboti.in");
    ofstream out("roboti.out");

    in>>n>>m;
    for(int i=0; i<n; i++)
        for(int j=0; j<m; j++)
            in>>mat[i][j];
    in>>x1>>y1;
    in>>x2>>y2;
    coada.push(make_pair(x1-1, y1-1));
    Lee();
    if(mat[x2-1][y2-1]==0) out<<-1;
    else out<<-mat[x2-1][y2-1];
    return 0;
}
