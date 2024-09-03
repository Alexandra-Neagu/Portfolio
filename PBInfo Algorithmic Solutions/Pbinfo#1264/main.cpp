#include <iostream>
#include <fstream>
#include <algorithm>

using namespace std;

int main()
{
    ifstream in("statisticiordine.in");
    ofstream out("statisticiordine.out");

    unsigned int n, k;
    unsigned long long v[100005];

    in>>n>>k;
    for(int i=0; i<n; i++)
        in>>v[i];
    sort(v, v+n);
    out<<v[k-1];
    return 0;
}
