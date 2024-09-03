#include <iostream>
#include <fstream>

using namespace std;

int main()
{
    ifstream in("plopi2.in");
    ofstream out("plopi2.out");

    int n;
    int plopi[101];
    int nrTaiati=0;
    int sumaTaiata=0;
    int micCurent;

    in>>n;
    for(int i=0; i<n; i++)
        in>>plopi[i];
    micCurent=plopi[0];
    for(int i=1; i<n; i++)
    {
        if(plopi[i]>micCurent)
        {
            nrTaiati++;
            sumaTaiata+=plopi[i]-micCurent;
        }
        else
            micCurent=plopi[i];
    }
    out<<nrTaiati<<' '<<sumaTaiata;
    return 0;
}
