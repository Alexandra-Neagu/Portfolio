#include <iostream>

using namespace std;

//GRESIT!!

int main()
{
    int n;
    long long nrPosib=1;

    cin>>n;
    for(int i=1; i<=n; i++)
    {
        if(i%4==0)
            nrPosib*=25;
        else
            nrPosib*=26;
        nrPosib%=777013;
    }
    cout<<nrPosib;
    return 0;
}
