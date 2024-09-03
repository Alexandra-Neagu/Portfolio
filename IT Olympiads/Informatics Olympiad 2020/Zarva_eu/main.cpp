#include <iostream>
#include <fstream>
#include <algorithm>

using namespace std;

ifstream in("zarva.in");
ofstream out("zarva.out");

struct Interval
{
    int start;
    int finish;
};

int n;
Interval inv[10001];
Interval invReal[10001];

bool comp(Interval a, Interval b)
{
    if(a.start > b.start)
        return a.start < b.start;
    else if(a.start == b.start)
        return a.finish < b.finish;
}

int main()
{
    in>>n;
    for(int i=0; i<n; i++)
        in>>inv[i].start>>inv[i].finish;
    sort(inv, inv+n, comp);
    invReal[0]=inv[0];
    int ind=0;
    for(int i=1; i<n; i++)
    {
        if(inv[i].start<=invReal[ind].finish || (inv[i].start==invReal[ind].finish+1))
            invReal[ind].finish=inv[i].finish;
        else
            invReal[++ind]=inv[i];
    }

    int momLinis=0;
    int totalLinis=0;
    int maxZgom;

    for(int i=0; i<=ind; i++)
        if((invReal[i].finish-invReal[i].start+1) > maxZgom)
            maxZgom=invReal[i].finish-invReal[i].start+1;
    momLinis=ind+1;
    if(invReal[0].start>0)
        momLinis++;
    totalLinis+=invReal[0].start+(86400-invReal[ind].finish-1);
    for(int i=1; i<=ind; i++)
        totalLinis+=invReal[i].start-invReal[i-1].finish-1;
    out<<momLinis<<' '<<totalLinis<<'\n'<<maxZgom;
    return 0;
}
