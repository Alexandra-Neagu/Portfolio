#include <iostream>

using namespace std;

int n, m;
int sol[8];

void Backtrack(int lvl)
{
    for(int i=0; i<m; i++)
    {
        sol[lvl]=i;
        if(lvl+1==n)
        {
            if(sol[lvl]==sol[0] && sol[0]!=0)
            {
                for(int j=0; j<n; j++)
                    cout<<sol[j];
                cout<<'\n';
            }
        }
        else
            Backtrack(lvl+1);
    }
}

int main()
{
    cin>>n>>m;

    Backtrack(0);
    return 0;
}
