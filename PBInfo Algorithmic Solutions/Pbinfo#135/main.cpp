#include <iostream>
#include <cstring>

using namespace std;

int main()
{
    char a[31];
    char b[31];

    cin>>a;
    cin>>b;
    for(int i=0; i<strlen(a); i++)
    {
        if((a[i]=='a' || a[i]=='e' || a[i]=='i' || a[i]=='o' || a[i]=='u') && (b[i]=='a' || b[i]=='e' || b[i]=='i' || b[i]=='o' || b[i]=='u')) b[i]='*';
        else if(a[i]!='a' && a[i]!='e' && a[i]!='i' && a[i]!='o' && a[i]!='u' && b[i]!='a' && b[i]!='e' && b[i]!='i' && b[i]!='o' && b[i]!='u') b[i]='#';
        else b[i]='?';

    }
    cout<<b;
    return 0;
}
