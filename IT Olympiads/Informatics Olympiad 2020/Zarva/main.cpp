#include <fstream>

using namespace std;

ifstream in("zarva.in");
ofstream out("zarva.out");

int n;
int v[86402];
int a,b;
int x=1;
int liniste=0;
int zgomot=0;
int secunde=0;
int maxim=0;

int main()
{
    in>>n;
    for(int i=1;i<=n;i++)
    {
        in>>a>>b;
        for(int j=a; j<=b ;j++)
            v[j]=1;
    }
    for(int i=0; i<86400; i++)
    {
        if(v[i]==0)
            secunde++;
        if(x==1 && v[i]==0)
        {
            x=0;
            maxim=max(maxim,zgomot);
            zgomot=0;
        }
        if(x==0 && v[i]==1)
        {
            x=1;
            liniste++;
        }
        if(x==1)
            zgomot++;
    }
    if(v[86399]==0)
        liniste++;
    if(v[86399]==1 && maxim==0)
        maxim=86400;
    out<<liniste<<' '<<secunde<<'\n'<<maxim;
    return 0;
}
