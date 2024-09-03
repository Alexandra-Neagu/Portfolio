#include <iostream>
#include <fstream>

using namespace std;

struct Mutare
{
    int start, finish;
};

int main()
{
    ifstream in("pachete.in");
    ofstream out("pachete.out");

    int n;
    int pachete[103];
    int poz[103];
    int locGol;
    Mutare mutari[309];
    int ind=0;
    int total=0;

    in>>n;
    locGol=n+1;
    for(int i=1; i<=n; i++)
    {
        in>>pachete[i];
        poz[pachete[i]]=i;
    }
    pachete[locGol]=-1;
    for(int i=1; i<=n; i++)
    {
        if(pachete[i]!=i)
        {
            if(pachete[i]!=-1)
            {
                mutari[ind].start=i;
                mutari[ind++].finish=locGol;
                poz[pachete[i]]=locGol;
                swap(pachete[i], pachete[locGol]);
                locGol=i;
            }
            if(pachete[i]==-1)
            {
                mutari[ind].start=poz[i];
                mutari[ind++].finish=locGol;
                swap(pachete[i], pachete[poz[i]]);
                locGol=poz[i];
                poz[i]=i;
            }
        }
    }
    out<<ind<<'\n';
    for(int i=0; i<ind; i++)
        out<<mutari[i].start<<' '<<mutari[i].finish<<'\n';
    return 0;
}
