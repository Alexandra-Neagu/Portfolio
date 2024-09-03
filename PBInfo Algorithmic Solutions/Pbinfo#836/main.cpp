#include <iostream>

using namespace std;

int vec[]={3,6,5,4,2};

void afisvec(int v[], int n)
{
    if(n>=1)
    {
        cout<<v[n-1]<<' ';
        afisvec(v, n-1);
    }

}

int main()
{
    afisvec(vec, 5);
    return 0;
}
