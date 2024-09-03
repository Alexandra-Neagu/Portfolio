#include <iostream>
#include <fstream>

using namespace std;

ifstream in("lant1.in");
ofstream out("lant1.out");

int n;
int prim, ultim, intermed;
int coada1[101], coada2[101];
int graf[101][101];
int viz1[101], viz2[101];
int preced1[101], preced2[101];
int pr;
int index=1;
int lungime;

void AfisDrum(int nod, int v[])
{
    if(v[nod]==0 || (v[nod]==intermed))
    {
        out<<nod<<' ';
        return;
    }
    AfisDrum(v[nod], v);
    out<<nod<<' ';

}

void CalcLung(int nod, int v[])
{
    lungime++;
    if(v[nod]==0)
        return;
    CalcLung(v[nod], v);
}

int main()
{
    in>>n;
    in>>prim>>ultim>>intermed;
    while(!in.eof())
    {
        int x, y;
        in>>x>>y;
        graf[x][y]=graf[y][x]=1;
    }

    coada1[0]=prim;
    viz1[prim]=1;
    preced1[prim]=0;

    while(pr<=index)
    {
        for(int i=1; i<=n; i++)
            if(graf[i][coada1[pr]]==1 && viz1[i]!=1)
            {
                    coada1[index++]=i;
                    viz1[i]=1;
                    preced1[i]=coada1[pr];
                    if(coada1[index-1]==intermed)
                        break;
            }
        pr++;
    }

    coada2[0]=intermed;
    viz2[intermed]=1;
    preced2[intermed]=0;
    pr=0; index=1;
    while(pr<=index)
    {
        for(int i=1; i<=n; i++)
            if(graf[i][coada2[pr]]==1 && viz2[i]!=1)
            {
                    coada2[index++]=i;
                    viz2[i]=1;
                    preced2[i]=coada2[pr];
                    if(coada2[index-1]==ultim)
                        break;
            }
        pr++;
    }

    CalcLung(intermed, preced1);
    CalcLung(ultim, preced2);
    out<<lungime-1<<'\n';

    AfisDrum(intermed, preced1);
    AfisDrum(ultim, preced2);
    return 0;
}
