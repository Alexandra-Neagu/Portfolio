#include <iostream>
#include <fstream>
#include <algorithm>

using namespace std;

int n, m;
int suma;
int brau[1001], camera[1001], intercl[2002];
int index;

void interclasareKindOf()
{
    for(int i=0; i<n; i++)
        intercl[index++]=brau[i];
    for(int i=0; i<m; i++)
        intercl[index++]=camera[i];
}

int main()
{
    ifstream in("arme.in");
    ofstream out("arme.out");

    in>>n>>m;
    for(int i=0; i<n; i++)
        in>>brau[i];
    for(int i=0; i<m; i++)
        in>>camera[i];

    interclasareKindOf();
    sort(intercl, intercl+index);
    for(int i=index-1; n>0; i--, n--)
        suma+=intercl[i];
    out<<suma;
    return 0;
}
