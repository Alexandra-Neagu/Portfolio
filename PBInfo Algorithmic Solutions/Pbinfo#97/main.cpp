#include <iostream>

using namespace std;

int main()
{
    int i=0;

    char a[21];
    char b[21];

    int frecventa_a[123]={0};
    int frecventa_b[123]={0};

    bool suntAnagrame=true;

    cin>>a;
    cin>>b;
    while(a[i]!='\0')
    {
        frecventa_a[(int)(a[i])]++;
        i++;
    }
    i=0;
    while(b[i]!='\0')
    {
        frecventa_b[(int)(b[i])]++;
        i++;
    }
    for(int i=97; i<=122; i++)
        if(frecventa_a[i]!=frecventa_b[i])
        {
            suntAnagrame=false;
            break;
        }
    if(suntAnagrame) cout<<1;
    else cout<<0;
    return 0;
}
