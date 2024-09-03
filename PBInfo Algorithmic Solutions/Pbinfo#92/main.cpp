#include <iostream>
#include <fstream>
#include <algorithm>

using namespace std;

struct Proiect
{
    int timp;
    int indice;
};

bool comp(Proiect a, Proiect b)
{
    return a.timp<b.timp;
}

int main()
{
    ifstream in("proiecte.in");
    ofstream out("proiecte.out");

    int n;
    Proiect pro[1001];

    in>>n;
    for(int i=0; i<n; i++)
    {
        in>>pro[i].timp;
        pro[i].indice=i;
    }
    sort(pro, pro+n, comp);
    for(int i=0; i<n; i++)
        out<<pro[i].indice+1<<' ';
    return 0;
}
