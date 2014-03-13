using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Licenta_Compilator.UI
{
    class Color
    {
        private static readonly ConsoleColor[] Values = (ConsoleColor[])Enum.GetValues(typeof(ConsoleColor));

        public static ConsoleColor GetColorWithId(int id)
        {
            int index = Math.Min(id + 1, Values.Length-1);
            return Values[index];
        }
    }
}