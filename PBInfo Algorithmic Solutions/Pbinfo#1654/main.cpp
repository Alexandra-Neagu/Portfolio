#include <iostream>
#include <cstring>

using namespace std;

int nr_vocale(char sir[])
{
    if(strchr(sir, 'a')) return 1 + strchr(sir, 'a');
    else if(strchr(sir, 'e')) return 1 + strchr(sir, 'e');
    else if(strchr(sir, 'i')) return 1 + strchr(sir, 'i');
    else if(strchr(sir, 'o')) return 1 + strchr(sir, 'o');
    else if(strchr(sir, 'u')) return 1 + strchr(sir, 'u');
    return 0;
}

int main()
{
    cout<<nr_vocale("Ana are mere");
    return 0;
}
