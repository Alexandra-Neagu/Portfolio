#include <iostream>
#include <math.h>

using namespace std;

int n;
int v[40];
int i = 0;
int ii;

int binar[50];

void Generare(int n, int v[])
{
    if (i== pow(2,n))return;
    for (ii=0; ii<=n-1;ii++)
    {
        cout<<v[ii];
    }
    v[n-1] ++;
    cout<<"\n";

    for (ii = n-1; ii>=0; ii--)
    {
        if (v[ii]==2)
        {
            //if (ii-1>0)
            v[ii-1]++;
            v[ii]=0;
        }

    }
    i++;
    Generare(n,v);
}

void Binar(int n, int indice)
{
    if(n==0) return;
    binar[indice]=n%2;
    Binar(n/2, indice+1);
}

int main()
{
    cin>>n;
    /*int maxim=1<<n;
    for(int i=0; i<maxim; i++)
    {
        Binar(i, 0);
        for(int j=n-1; j>=0; j--)
        {
            cout<<binar[j];
            binar[j]=0;
        }
        cout<<"\n";
    }*/
    Generare(n, v);
    return 0;
}
