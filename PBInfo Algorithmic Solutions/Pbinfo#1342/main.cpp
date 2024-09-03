#include <fstream>

using namespace std;

ifstream in("nrsubsircresc.in");
ofstream out("nrsubsircresc.out");

int main()
{
    int n;
    int nr[310];
    long long nrSiruri[310];
    long long nrSubsirCresc=0;

    in>>n;
    for(int i=0; i<n; i++)
    {
        in>>nr[i];
        nrSiruri[i]=1;
        for(int j=0; j<i; j++)
            if(nr[j]<nr[i])
                nrSiruri[i]+=nrSiruri[j];
        nrSubsirCresc+=nrSiruri[i];
    }
    out<<nrSubsirCresc;
    return 0;
}
