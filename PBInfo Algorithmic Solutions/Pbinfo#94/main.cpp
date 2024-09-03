#include <iostream>
#include <string.h>
#include <stdio.h>

using namespace std;

int main()
{
    char a[21];

    int lungime;

    cin>>a;
    lungime=strlen(a);

    for(int i=0; i<lungime; i++)
    {
        if(a[i]!='a' && a[i]!='e' && a[i]!='i' && a[i]!='o' && a[i]!='u')
            cout<<a[i];
    }
    return 0;
}
