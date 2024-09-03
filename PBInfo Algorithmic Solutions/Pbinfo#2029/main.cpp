#include <fstream>

using namespace std;

int main()
{
    ifstream in("afisaren.in");
    ofstream out("afisaren.out");

    int n;
    char cuv[1000001];

    in.getline(cuv,1000001);
    in>>cuv;
    for(int i=0; i<n; i++)
        out<<cuv<<"\n";
    return 0;
}
