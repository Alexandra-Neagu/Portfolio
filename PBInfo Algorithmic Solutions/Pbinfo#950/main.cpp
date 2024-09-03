#include <fstream>
#include <algorithm>

using namespace std;

struct Cerc
{
    int valMin, valMax;
};

bool comp(Cerc a, Cerc b)
{
    return a.valMin<b.valMin;
}

void Citire(int &n, Cerc cercuri[])
{
    ifstream in("cerc3.in");

    int centru, raza;

    in>>n;
    for(int i=0; i<n; i++)
    {
        in>>centru>>raza;
        cercuri[i].valMin=centru-raza;
        cercuri[i].valMax=centru+raza;
    }
}

int main()
{
    ofstream out("cerc3.out");

    int n;
    Cerc cercuri[302];
    int nrInterv=1;
    Cerc intersec;

    Citire(n, cercuri);
    sort(cercuri, cercuri+n, comp);
    intersec.valMin=cercuri[0].valMin;
    intersec.valMax=cercuri[0].valMax;
    for(int i=1; i<n; i++)
    {
        if(cercuri[i].valMin<=intersec.valMax)
        {
            intersec.valMin=cercuri[i].valMin;
            if(cercuri[i].valMax<intersec.valMax)
                intersec.valMax=cercuri[i].valMax;
        }
        else
        {
            nrInterv++;
            intersec.valMin=cercuri[i].valMin;
            intersec.valMax=cercuri[i].valMax;
        }
    }
    out<<nrInterv;
    return 0;
}
