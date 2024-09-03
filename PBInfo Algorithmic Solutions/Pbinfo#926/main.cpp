#include <iostream>

using namespace std;

int vec[]={12, 7, 6, 3, 8, 5};

int sum3(int v[], int n)
{
    if(n==1)
    {
        if(v[n-1]%3==0) return v[n-1];
        return 0;
    }
    else
    {
        if(v[n-1]%3==0) return v[n-1] + sum3(v, n-1);
        return sum3(v, n-1);
    }


}

int main()
{
    cout<<sum3(vec, 6);
    return 0;
}
