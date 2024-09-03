#include <iostream>
#include <fstream>

using namespace std;

int main()
{
    ifstream in("eureni.in");
    ofstream out("eureni.out");

    unsigned long S;
    int n, e;
    unsigned long bancnote[11];
    int nrCurent;
    int total=0;

    in>>S>>n>>e;

    bancnote[0]=1;
    for(int i=1; i<=n; i++)
        bancnote[i]=bancnote[i-1]*e;
    int j=n;
    while(S>0)
    {
        if(S>=bancnote[j])
        {
            nrCurent=0;
            out<<bancnote[j]<<' ';
            while(S>=bancnote[j])
            {
               nrCurent++;
                total++;
                S-=bancnote[j];
            }
            out<<nrCurent<<'\n';
        }
        j--;
    }
    out<<total;
    return 0;
}
