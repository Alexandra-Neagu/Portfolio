#include <iostream>

using namespace std;

int main()
{
    int n;
    int plopi[1001];
    int difMax;
    int total=0;

    cin>>n;
    for(int i=0; i<n; i++)
        cin>>plopi[i];

    difMax=plopi[0]-plopi[1];
    for(int i=1; i<n-1; i++)
    {
        if(plopi[i]-plopi[i-1]>difMax)
            difMax=plopi[i]-plopi[i-1];
        if(plopi[i]-plopi[i+1]>difMax)
            difMax=plopi[i]-plopi[i+1];
    }
    if(plopi[n-1]-plopi[n-2]>difMax)
        difMax=plopi[n-1]-plopi[n-2];

    if(plopi[0]-plopi[1]==difMax)
            total++;
    if(plopi[n-1]-plopi[n-2]==difMax)
        total++;
    for(int i=1; i<n-1; i++)
    {
        if(plopi[i]-plopi[i-1]==difMax)
            total++;
        if(plopi[i]-plopi[i+1]==difMax)
            total++;
    }
    cout<<difMax<<' '<<total;
    return 0;
}
