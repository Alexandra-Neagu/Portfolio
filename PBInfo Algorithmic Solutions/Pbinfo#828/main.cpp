#include <iostream>

using namespace std;

int mp(int x)
{
    if(x>=12) return x-1;
    return mp(mp(x+2));
}

int main()
{
    cout<<mp(7);
    return 0;
}
