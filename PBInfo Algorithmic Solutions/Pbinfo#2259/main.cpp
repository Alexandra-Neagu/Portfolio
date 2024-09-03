#include <iostream>

using namespace std;

int main()
{
    int n;
    long long incepePar=1;
    long long incepeImpar=1;

    cin>>n;
    for(int i=0; i<n; i++)
    {
        if(i%2==0)
        {
            incepePar*=4;
            incepeImpar*=5;
        }
        else
        {
            incepePar*=5;
            incepeImpar*=4;
        }
        incepePar%=30103;
        incepeImpar%=30103;
    }
    cout<<(incepePar+incepeImpar)%30103;
    return 0;
}
