#include <iostream>
#include <fstream>

using namespace std;

ifstream in("shuffle.in");
ofstream out("shuffle.out");

int n;
int nr[10];
int poz[10];
int sirCresc[10];
int ales[10];
int sol[10];
bool exista=false;

void Backtrack(int lvl)
{
    for(int i=0; i<n; i++)
    {
        if(ales[i]==0)
            if(lvl==0 || (sirCresc[i]!=nr[poz[sol[lvl-1]]-1] && sirCresc[i]!=nr[poz[sol[lvl-1]]+1]))
            {
                ales[i]=1;
                sol[lvl]=sirCresc[i];
                if(lvl+1==n)
                {
                    exista=true;
                    for(int j=0; j<n; j++)
                        out<<sol[j]<<' ';
                    out<<'\n';
                }
                else
                    Backtrack(lvl+1);
                ales[i]=0;
            }
    }
}


int main()
{
    in>>n;
    for(int i=0; i<n; i++)
    {
        in>>nr[i];
        poz[nr[i]]=i;
        sirCresc[i]=i+1;
    }
    Backtrack(0);
    if(exista==false)
        out<<"nu exista";
    return 0;
}
