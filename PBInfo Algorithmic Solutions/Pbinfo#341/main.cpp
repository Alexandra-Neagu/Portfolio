#include <iostream>
#include <fstream>
#include <algorithm>

using namespace std;

struct Mutare
{
    int luam;
    int punem;
    int nrBombo;
};

struct Bomboana
{
    int val;
    int indice;
};

/*
void sortare(int n, Bomboana v[])
{
    bool sorted=false;
    while(!sorted)
    {
        sorted=true;
        for(int i=0; i<n-1; i++)
        {
            if(v[i].val>v[i+1].val)
            {
                swap(v[i], v[i+1]);
                sorted=false;
            }
        }
    }
}
*/

bool comp(Bomboana a, Bomboana b)
{
    return a.val<b.val;
}

int main()
{
    ifstream in("bomboane.in");
    ofstream out("bomboane.out");

    int n;
    Bomboana bombo[1005];
    int suma=0;
    int i;
    int j;
    Mutare mutari[10001];
    int index=0;
    int total=0;

    in>>n;
    for(int l=0; l<n; l++)
    {
        in>>bombo[l].val;
        suma+=bombo[l].val;
        bombo[l].indice=l;
    }
    if(suma%n!=0)
    {
        out<<-1;
        return 0;
    }
    else
        suma/=n;
    i=0;
    j=n-1;
    //sortare(n, bombo);
    sort(bombo, bombo+n, comp);

    while(i!=j)
    {
        while(bombo[i].val==suma)
            i++;
        while(bombo[j].val==suma)
            j--;
        if(i>=j)
            break;
        if(bombo[j].val+bombo[i].val>=2*suma)
        {
            bombo[j].val-=suma;
            bombo[j].val+=bombo[i].val;
            mutari[index].luam=bombo[j].indice+1;
            mutari[index].punem=bombo[i].indice+1;
            mutari[index++].nrBombo=suma-bombo[i].val;
            bombo[i].val=suma;
            total++;
        }
        else
        {
            bombo[i].val+=bombo[j].val;
            bombo[i].val-=suma;
            mutari[index].luam=bombo[j].indice+1;
            mutari[index].punem=bombo[i].indice+1;
            mutari[index++].nrBombo=bombo[j].val-suma;
            bombo[j].val=suma;
            total++;
        }
        //sortare(n, bombo);
        sort(bombo, bombo+n, comp);
    }
    out<<total<<'\n';
    for(int l=0; l<index; l++)
        out<<mutari[l].luam<<' '<<mutari[l].punem<<' '<<mutari[l].nrBombo<<'\n';
    return 0;
}
