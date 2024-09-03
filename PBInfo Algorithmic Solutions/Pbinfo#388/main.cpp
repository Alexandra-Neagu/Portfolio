#include <iostream>
#include <cmath>

using namespace std;

int main()
{
    int n;
    int ctr=0;

    cin>>n;
    for(int div=2; div<=n; div++)
    {
        if(n%div==0 && div%2==0)
            ctr++;
    }
    cout<<ctr;
    return 0;
}
