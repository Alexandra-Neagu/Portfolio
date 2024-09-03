#include <iostream>

using namespace std;

struct node
{
    char key;
    node *next;
};

bool palindrom(node *l)
{
    int length=0;
    int copyLength;
    node *currentNode=l;
    node *l2;

    for(node *curNode=l; curNode!=NULL; curNode=curNode->next)
        length++;
    copyLength=length/2;
    if(length%2==0) length/=2;
    else length=length/2+1;
    while(length)
    {
        currentNode=currentNode->next;
        length--;
    }
    l2=currentNode;
    currentNode=currentNode->next;
    for(; currentNode!=NULL; currentNode=currentNode->next)
    {
        node *newNode=new node;
        newNode->key=currentNode->key;
        newNode->next=l2;
        l2=newNode;
    }
    currentNode=l;
    while(copyLength)
    {
        if(currentNode->key!=l2->key)
            return false;
        currentNode=currentNode->next;
        l2=l2->next;
        copyLength--;
    }
    return true;
}

int main()
{
    node *l=new node;
    cin>>l->key;
    l->next=NULL;
    node *p=l;
    for(int i=1; i<10; i++)
    {
        node *nextNode=new node;
        cin>>nextNode->key;
        nextNode->next=NULL;
        p->next=nextNode;
        p=nextNode;
    }
    cout<<palindrom(l);
    return 0;
}
