#include <iostream>

using namespace std;

struct nod
{
    int info;
    nod *urm;
};

void ins_cresc(nod *&p, int x)
{
    nod *newNod=new nod;
    nod *curNod=p;
    bool inserat=false;

    if(x<=p->info)
    {
        newNod->info=x;
        newNod->urm=p;
        p=newNod;
        inserat=true;
    }
    else
    {
        for(; curNod->urm!=NULL; curNod=curNod->urm)
        {
            if(x>curNod->info && x<=curNod->urm->info)
            {
                newNod=newNod;
                newNod->info=x;
                newNod->urm=curNod->urm;
                curNod->urm=newNod;
                inserat=true;
                break;
            }
        }
        if(!inserat)
        {
            newNod=new nod;
            newNod->info=x;
            newNod->urm=NULL;
            curNod->urm=newNod;
        }
    }
}

int main()
{

    return 0;
}
