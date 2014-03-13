using System;
using Licenta_Compilator.Pattern;
using Licenta_Compilator.TreeRewrite;

namespace Licenta_Compilator
{
    class Program
    {
        static void Main(string[] args)
        {
            int testNr = 1;

            Analize.Analize analize = new Analize.Analize(
                "C:\\Users\\mciorobe\\Documents\\Visual Studio 2010\\Projects\\Licenta Compilator\\Licenta Compilator\\Teste\\Simple" + testNr + "\\P1.cs",
                "C:\\Users\\mciorobe\\Documents\\Visual Studio 2010\\Projects\\Licenta Compilator\\Licenta Compilator\\Teste\\Simple" + testNr + "\\P2.cs"
                );

            analize.Start();
            Console.WriteLine(analize.FinalProgramText());



            while (true)
            {
              
              /*  analize.ExtraLines(15, true);
                analize.FinalProgramText();

                analize.ChangeDefaultProgram(2);
                analize.FinalProgramText();

                analize.ChangeDefaultProgram(1);
                analize.RenameVar("a", "newA");
                analize.FinalProgramText();

                analize.MakeFinal();
                analize.RenameVarAfterFinal("a", "newA");
                analize.SaveTemplate("Test" + testNr);
               
               */
                break;
            }


          //  analize.MakeFinal(TODO);
            analize.RenameVarAfterFinal("a", "newA");
            //analize.EditCode("New Text");
            analize.SaveTemplate("Test" + testNr);


        }
    }
}
