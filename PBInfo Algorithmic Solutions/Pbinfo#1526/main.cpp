#include <iostream>
#include <istream>
#include <cstring>
#include <fstream>

using namespace std;

int main()
{
    ifstream in("align.in");
    ofstream out("align.out");

    int n;
    int indice;
    int lungMax=0;
    char aliniere;
    char linie[200];

    in>>n;
    in>>aliniere;
    for(int i=0; i<n; i++)
    {
        in.ignore();
        in.get(linie,200);
        if(strlen(linie)>lungMax) lungMax=strlen(linie);
    }
    in.close();
    in.open("align.in");
    in.get();
    while(getline(in,linie))
    {
        indice=0;
        while(linie[indice]==' ') indice++;
        strcpy(linie, linie+indice);
        if(aliniere=='S') out<<linie<<"\n";
        else if(aliniere=='D')
        {
            for(int j=0; j<lungMax-strlen(linie); j++)
                out<<" ";
            out<<linie<<"\n";
        }
    }
    in.close();
    out.close();
    return 0;
}
