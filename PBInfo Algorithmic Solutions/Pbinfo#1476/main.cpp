#include <iostream>
#include <algorithm>

using namespace std;

struct nod
{
    int info;
    nod *urm;
};

void sortareCrescator(nod *&prim)
{
    bool isSorted=false;

    while(!isSorted)
    {
        isSorted=true;
        for(nod *curNod=prim; curNod->urm!=NULL; curNod=curNod->urm)
            if(curNod->info>curNod->urm->info)
            {
                swap(curNod->info,curNod->urm->info);
                isSorted=false;
            }
    }
}

int main()
{

    return 0;
}
