#include <iostream>
#include <cstring>
#include <cmath>
#include <fstream>

using namespace std;

ifstream in("intrare.in");
ofstream out("iesire.out");

int main()
{
    char a[16], b[16];

    int A[4]={0};
    int B[4]={0};

    int ordA=0;
    int ordB=0;

    cin>>a>>b;

    int lungA=strlen(a);
    int lungB=strlen(b);

    long long int rez;

    for(int i=0; i<lungA; i++)
    {
        if(a[i]=='.')
            ordA++;
        else
        {
            A[ordA]=A[ordA]*10+((int)(a[i])-48);
        }
    }
    for(int i=0; i<lungB; i++)
    {
        if(b[i]=='.')
            ordB++;
        else
        {
            B[ordB]=B[ordB]*10+((int)(b[i])-48);
        }
    }
    rez=256*256*256;
    rez*=(B[0]-A[0]);
    rez+=(B[1]-A[1])*256*256+(B[2]-A[2])*256+B[3]-A[3]+1;
    cout<<rez;
    return 0;
}
