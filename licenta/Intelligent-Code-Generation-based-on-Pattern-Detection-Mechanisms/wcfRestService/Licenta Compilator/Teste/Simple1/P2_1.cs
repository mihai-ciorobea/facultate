using System;
using uVarName;
class TestTeTdNda
{
    public int mynumeVariabilaInt = 5;
    public bool numeVariabilaBool = true;
   
     
    public Position WalkEast()
    {
       var player = GetPlayer();
       player.Move("E");
       return player.NewPosition;
    }
     
    public Position WalkWest()
    {
       var player = GetPlayer();
       player.Move("W");
       return player.NewPosition;
    }

    public void Method1()
    {
        DoX();

        if (MyVariable)
        {
            var input = GetA();
            DoA1();
            DoA2(input);
        }

        DoY();
    }
}