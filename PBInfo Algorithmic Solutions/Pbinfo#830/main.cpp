#include <iostream>
#include <algorithm>

using namespace std;

int n, a, x, y;
int b;
int indice, total;
int v[10005];
int avemDeja[10001];

void Generare(int b)
{
    avemDeja[b]=1;
    if(b+x<=n)
    {
        if(avemDeja[b+x]==0)
        {
            v[total]=b+x;
            total++;
        }
        Generare(b+x);
    }
    if(b+y<=n)
    {
        if(avemDeja[b+y]==0)
        {
            v[total]=b+y;
            total++;
        }
        Generare(b+y);
    }
}

int main()
{
    cin>>n>>a>>x>>y;
    v[indice]=a;
    total++;
    Generare(a);
   // sort(v, v+total);
    for(int i=0; i<total; i++)
        cout<<v[i]<<' ';
    return 0;
}
