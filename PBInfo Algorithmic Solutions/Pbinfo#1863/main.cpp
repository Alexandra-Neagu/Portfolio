#include <iostream>

using namespace std;

int vec[]={5, 6, 6, 3, 5, 5};

int numarare(int v[], int n)
{
    if(n==1)
    {
        if(v[n]==v[n-1]) return 1;
        return 0;
    }
    else
    {
        if(v[n]==v[n-1]) return 1 + numarare(v, n-1);
        return numarare(v, n-1);
    }


}

int main()
{
    cout<<numarare(vec, 6);
    return 0;
}
