#include <iostream>
#include <fstream>

using namespace std;

long long n;
long long nr;
//int suma;

/*void Suma(int doi, int contor)
{
    if(contor==n+1) return;
    suma+=doi;
    Suma(doi*2, contor+1);
}*/

int main()
{
    ifstream in("sume1.in");
    ofstream out("sume1.out");

    /*int n;
    int nr;*/
    in>>n;
    nr=1<<n+1;
    nr--;
    out<<nr%1000000007;

    /*in>>n;
    Suma(1, 0);
    out<<suma%1000000007;*/

    return 0;
}
