#include <iostream>

using namespace std;

int main()
{
    int i=0;
    int contor=0;
    char a[256];

    cin.get(a,256);
    while(a[i+1]!='\0')
    {
        if((a[i]=='a' || a[i]=='e' || a[i]=='i' || a[i]=='o' || a[i]=='u') && (a[i+1]=='a' || a[i+1]=='e' || a[i+1]=='i' || a[i+1]=='o' || a[i+1]=='u'))
            contor++;
        i++;
    }
    cout<<contor;
    return 0;
}
