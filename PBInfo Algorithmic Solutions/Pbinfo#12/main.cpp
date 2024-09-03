#include <iostream>

using namespace std;

int main()
{
    char a[256];
    int i=0;

    cin.get(a,256);
    while(a[i]!='\0')
    {
        if((a[i]>='a' && a[i]<='z') && (i==0 || a[i+1]=='\0' || a[i-1]==' ' || a[i+1]==' ')) a[i]=a[i]-('a'-'A');
        i++;
    }
    cout<<a;
    return 0;
}
