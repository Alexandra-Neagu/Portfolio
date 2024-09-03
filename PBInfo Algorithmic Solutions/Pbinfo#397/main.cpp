#include <iostream>
#include <fstream>

using namespace std;

ifstream in("plopi1.in");
ofstream out("plopi1.out");

int main()
{
    int n;
    int plopi[1005];
    long long nrSiruri[1005];
    int maximTotal=0;

    in>>n;
    for(int i=0; i<n; i++)
        in>>plopi[i];
    nrSiruri[n-1]=1;
    for(int i=n-2; i>=0; i--)
    {
        int maxim=0;
        for(int j=i+1; j<n; j++)
        {
            if(plopi[j]<plopi[i])
                if(nrSiruri[j]>maxim)
                    maxim=nrSiruri[j];
        }
        nrSiruri[i]=maxim+1;
    }
    for(int i=0; i<n; i++)
    {
        if(nrSiruri[i]>maximTotal)
            maximTotal=nrSiruri[i];
    }
    out<<n-maximTotal;
    return 0;
}
