#include <iostream>
#include <cmath>

using namespace std;

int a_prim(int n)
{
    bool unDiv=false;

    if(sqrt(n)==(int)(sqrt(n))) return 0;
    for(int div=2; din<=sqrt(n); div++)
    {
        if(n%div==0)
        {
            unDiv=true;
            n/=div;
            break;
        }
    }
    if(unDiv)
    {
        for
    }
    return 0;
}

int main()
{

    return 0;
}
