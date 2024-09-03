#include <iostream>
#include <fstream>
#include <climits>
#include <queue>

using namespace std;

struct Harta
{
    int val;
    int nrMinPeric;
};

int cerinta;
int n;
int G;
Harta mat[505][505];

int dx[4]={0, 1, 0, -1};
int dy[4]={1, 0, -1, 0};

queue <pair <int,int> > coada;

void Lee()
{
    int x,y;
    while(!coada.empty())
    {
        x=coada.front().first;
        y=coada.front().second;
        coada.pop();
        for(int i=0; i<4; i++)
        {
            if(x+dx[i]>=0 && x+dx[i]<n && y+dy[i]>=0 && y+dy[i]<n && mat[x+dx[i]][y+dy[i]].nrMinPeric>mat[x][y].nrMinPeric)
            {
                //mat[x+dx[i]][y+dy[i]].val=mat[x][y].val+1;
                if(mat[x][y].val==-1) mat[x+dx[i]][y+dy[i]].nrMinPeric=mat[x][y].nrMinPeric+1;
                else mat[x+dx[i]][y+dy[i]].nrMinPeric=mat[x][y].nrMinPeric;
                coada.push(make_pair(x+dx[i], y+dy[i]));
            }
        }
    }
}

int main()
{
    ifstream in("rover.in");
    ofstream out("rover.out");

    in>>cerinta;
    if(cerinta==1)
    {
        in>>n>>G;
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++)
            {
                in>>mat[i][j].val;
                if(mat[i][j].val>=G) mat[i][j].val=0;
                else mat[i][j].val=-1;
                mat[i][j].nrMinPeric=INT_MAX;
            }
        coada.push(make_pair(0, 0));
        mat[0][0].val=1;
        mat[0][0].nrMinPeric=0;
        Lee();
        out<<mat[n-1][n-1].nrMinPeric;
    }
    return 0;
}
