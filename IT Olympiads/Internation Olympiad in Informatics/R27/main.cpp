#include <iostream>
#include <cstring>
#include <fstream>

using namespace std;

ifstream in("intrare.in");
ofstream out("iesire.out");

    int n;
    char SIR[1001];
    char CF[101];

    int sir[101][1001]={-1};
    int cf[11][101]={-1};
    int lungSir[1001];
    int lungCf[101];

int main()
{
    cin>>n;
    for(int i=0; i<n; i++)
    {
        cin>>SIR;
        lungSir[i]=strlen(SIR);
        for(int j=0; j<lungSir[i]; j++)
            sir[i][j]=(int)(SIR[j])-48;
    }
    for(int i=0; i<=9; i++)
    {
        cin>>CF;
        lungCf[i]=strlen(CF);
        for(int j=0; j<lungCf[i]; j++)
            cf[i][j]=(int)(CF[j])-48;
    }

    for(int i=0; i<n; i++)
    {
        //if(lungCf==1)
           // for(int j=0; j<lungSir[i]; j++)
               // cout<<cf[sir[i][j]][0];
       // else
            for(int j=0; j<lungSir[i]; j++)
            {
                for(int k=0; k<=9; k++)
                    for(int l=0; l<lungCf[k]; l++)
                    {
                        if(sir[i][j+l]!=cf[k][l])
                            break;
                        else if(sir[i][j+l]==cf[k][l] && l==lungCf[k]-1)
                        {
                            cout<<k;
                            j+=l;
                        }
                    }
            }
        if(i<n-1)
           cout<<'\n';
    }
    return 0;
}
