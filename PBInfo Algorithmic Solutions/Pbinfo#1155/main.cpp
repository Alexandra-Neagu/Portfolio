#include <iostream>

using namespace std;

int CautareBinara(int s, int f, int nrVerif, int x[])
{
    int m=(s+f)/2;
    if(s>f) return 0;
    else if(nrVerif == x[m]) return 1;
    else
    {
        if(nrVerif > x[m]) CautareBinara(m+1, f, nrVerif, x);
        else CautareBinara(s, m-1, nrVerif, x);
    }
}

int main()
{
    int n, m;
    int x[25002];
    int y[200002];

    cin>>n;
    for(int i=0; i<n; i++)
        cin>>x[i];

    cin>>m;
    for(int i=0; i<m; i++)
        cin>>y[i];

    for(int i=0; i<m; i++)
        cout<<CautareBinara(0, n-1, y[i], x)<<' ';
    return 0;
}
