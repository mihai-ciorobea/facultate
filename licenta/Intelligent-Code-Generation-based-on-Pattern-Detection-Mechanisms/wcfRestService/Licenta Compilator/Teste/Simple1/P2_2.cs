using System;
class MyTest
{
    public int index = 5;
    public bool isOk = true;
    public Position WalkNorth()
    {
       var player = GetPlayer();
       player.Move("N");
       return player.NewPosition;
    }
     
  public void Method2()
    {
        DoX();

        if (value)
        {
            var input = GetB();
            DoB(input);
        }

        DoY();
    }
     
     
    public Position WalkSouth()
    {
       var player = GetPlayer();
       player.Move("S");
       return player.NewPosition;
    }
     
    
}