using System;
using nVarName;
class Test
{
    public int numeVariabilaInt = 5;
    public bool numeVariabilaBool = true;
    public void Main(string[] args, int b)
    {
        int a = 10 + 10;
        if ( 1 > 2)                    
       // for(int j = 0; j < 10; j++)
       for(int i = 0; i < 10; i++)
            Console.WriteLine("Hello, World");
           if ( a > 10 ) {
            a = 10;
        } else {
            a  ++; 
        }
        try{
           a++;
        } catch(Exception e){
        	a++;
        }
    }
}