#include <iostream>

using namespace std;

struct nod
{
    int info;
    nod *urm;
};

void inserare(nod * & p)
{
    nod *start=p;
    nod *newNod=new nod;

    if(start->info%2==0)
    {
        newNod->info=start->info*2;
        newNod->urm=start;
        p=newNod;
    }
    for(nod *curNod=start; curNod->urm!=NULL; curNod=curNod->urm)
    {
        if(curNod->urm->info%2==0)
        {
            newNod=new nod;
            newNod->info=curNod->urm->info*2;
            newNod->urm=curNod->urm;
            curNod->urm=newNod;
            curNod=curNod->urm;
        }
    }
}

int main()
{

    return 0;
}
