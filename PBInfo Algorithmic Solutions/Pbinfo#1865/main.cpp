#include <fstream>
#include <iostream>

using namespace std;

int n;
int nr;
long long sume[1000002];

int CautareBinara(int s, int f, int nrVerif)
{
    if(s>f) return 0;
    else
    {
        int m=(s+f)/2;
        if(nrVerif == sume[m]) return m+1;
        else if(nrVerif > sume[m]) return CautareBinara(m+1, f, nrVerif);
        else return CautareBinara(s, m-1, nrVerif);
        return 0;
    }
}

int main()
{
    ifstream in("summit.in");
    ofstream out("summit.out");

    in>>n;
    in>>nr;
    sume[0]=nr;
    out<<1<<"\n";
    for(int i=1; i<n; i++)
    {
        in>>nr;
        sume[i]=sume[i-1]+nr;
        out<<CautareBinara(0, i, nr)<<"\n";
    }
    return 0;
}
