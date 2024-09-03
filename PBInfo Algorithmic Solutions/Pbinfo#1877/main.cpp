#include <iostream>
#include <algorithm>

using namespace std;

int main()
{
    unsigned int n, k;
    int  v[1001];
    int suma=0;

    cin>>n;
    for(int i=0; i<n; i++)
        cin>>v[i];
    cin>>k;
    sort(v, v+n);
    for(int i=0; i<k; i++)
        suma+=-v[i];
    for(int i=k; i<n; i++)
        suma+=v[i];
    cout<<suma;
    return 0;
}
