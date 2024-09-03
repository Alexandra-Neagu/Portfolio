#include <fstream>
#include <cstring>

using namespace std;

int main()
{
    ifstream in("doarvocale.in");
    ofstream out("doarvocale.out");

    int n;
    int contor=0;

    char a[31];

    bool areDoarVocale;

    in>>n;
    for(int i=0; i<n; i++)
    {
        in>>a;
        areDoarVocale=true;
        for(int j=0; j<strlen(a); j++)
            if(a[j]!='a' && a[j]!='e' && a[j]!='i' && a[j]!='o' && a[j]!='u')
            {
                areDoarVocale=false;
                break;
            }
        if(areDoarVocale) contor++;
    }
    out<<contor;
    return 0;
}
