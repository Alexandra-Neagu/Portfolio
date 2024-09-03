#include <iostream>

using namespace std;

struct Meci
{
    int jucat1;
    int jucat2;
};

int T;
int N;
int M;
int maxim;
int minim;
int juc1, juc2;
int jucatori[52]={0};
int scorFinal[52]={0};
Meci meciuri[202];

int main()
{
    cin>>T;
    for(int i=0; i<T; i++)
    {
        cin>>N>>M;
        minim=2147483646;
        maxim=-1;
        for(int j=0; j<52; j++)
        {
            jucatori[j]=0;
            scorFinal[j]=0;
        }
        for(int j=0; j<200; j++)
        {
            meciuri[j].jucat1=-1;
            meciuri[j].jucat2=-1;
        }
        for(int j=0; j<M; j++)
        {
            cin>>meciuri[j].jucat1>>meciuri[j].jucat2;
            jucatori[meciuri[j].jucat1]++;
            jucatori[meciuri[j].jucat2]++;
        }
        int nr=2*M;
        while(nr>0)
        {
            int jucator=0;
            int minLocal=2147483646;
            for(int j=1; j<=N; j++)
            {
                if(jucatori[j]<minLocal && jucatori[j]>0)
                {
                    minLocal=jucatori[j];
                    jucator=j;
                }
            }
            int maxLocal=-1;
            int indMeci=-1;
            for(int j=0; j<M; j++)
            {
                if(meciuri[j].jucat1==jucator && jucatori[meciuri[j].jucat2]>maxLocal)
                {
                    maxLocal=jucatori[meciuri[j].jucat2];
                    indMeci=j;
                }
                else if(meciuri[j].jucat2==jucator && jucatori[meciuri[j].jucat1]>maxLocal)
                {
                    maxLocal=jucatori[meciuri[j].jucat1];
                    indMeci=j;
                }
            }
            jucatori[meciuri[indMeci].jucat1]--;
            jucatori[meciuri[indMeci].jucat2]--;
            scorFinal[jucator]++;
            meciuri[indMeci].jucat1=-1;
            meciuri[indMeci].jucat2=-1;
            nr-=2;
        }
        for(int j=1; j<=N; j++)
        {
           /* if(scorFinal[j]>maxim)
                maxim=scorFinal[j];
            else if(scorFinal[j]<minim)
                minim=scorFinal[j];*/
            cout<<scorFinal[j]<<' ';
        }
      //  cout<<minim<<' '<<maxim<<'\n';
    }
    return 0;
}
