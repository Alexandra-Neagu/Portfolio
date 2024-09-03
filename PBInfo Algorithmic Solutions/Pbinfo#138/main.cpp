#include <iostream>

using namespace std;

int main()
{
    char a[256];

    int frecventa[123]={0};

    int indice;
    int maxim=0;
    int i=0;

    cin.get(a,256);
    while(a[i]!='\0')
    {
        if(a[i]>='a' && a[i]<='z') frecventa[(int)(a[i])]++;
        i++;
    }
    for(int i=97; i<=122; i++)
    {
        if(frecventa[i]>maxim)
        {
            maxim=frecventa[i];
            indice=i;
        }
    }
    cout<<(char)indice;
    return 0;
}
