#include <iostream>

using namespace std;

int cifmaxpar(int n)
{
    if(n==0) return -1;
    if(n%2==0 && (cifmaxpar(n/10)==-1 || n%10>cifmaxpar(n/10))) return n%10;
    return cifmaxpar(n/10);
}

int main()
{
    cout<<cifmaxpar(2154)<<' '<<cifmaxpar(1157);
    return 0;
}
