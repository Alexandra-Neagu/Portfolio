#include <iostream>
#include <cstring>
#include <fstream>

using namespace std;

int main()
{
    ifstream in("s_p_c.in");
    ofstream out("s_p_c.out");

    int indiciS[300]={0};
    int indiciP[300]={0};
    int indiciC[300]={0};
    char cuv[40];
    char tot[126000];


    while(in>>cuv)
    {
        strcat(tot,cuv);
    }
    cout<<tot;
    return 0;
}
