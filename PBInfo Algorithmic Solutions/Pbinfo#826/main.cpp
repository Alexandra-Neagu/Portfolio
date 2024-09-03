#include <iostream>

using namespace std;

int cifminpar(int n)
{
    if(n==0) return -1;
    if(n%2==0 && (cifminpar(n/10)==-1 || n%10<cifminpar(n/10))) return n%10;
    return cifminpar(n/10);
}

int main()
{
    cout<<cifminpar(2154)<<' '<<cifminpar(1157);
    return 0;
}
