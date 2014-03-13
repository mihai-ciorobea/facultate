using System;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;
using Licenta_Compilator.Common;

namespace Licenta_Compilator.Text
{
    internal class StringUtils
    {
        public static string GetLine(string text, int lineNo)
        {
            string[] lines = text.Replace("\r", "").Split('\n');
            return lines.Length > lineNo-1 ? lines[lineNo-1] : null;
        }

        public static int GetLineNumbers(string text)
        {
            return text.Replace("\r", "").Split('\n').Length;
        }

        public static bool Match(string s1, string s2, double similarity)
        {
            if (Common.Common.VariableNameMatching == VariableNameMatching.CharacterCount)
                return CharacterCountMatching(s1, s2, similarity);

            if (Common.Common.VariableNameMatching == VariableNameMatching.WordCount)
                return WordCountMatching(s1, s2, similarity);

            return false;
        }

        private static bool WordCountMatching(string s1, string s2, double similarity)
        {
            string[] s1Words = Regex.Split(s1, @"(?<!^)(?=[A-Z])");
            string[] s2Words = Regex.Split(s2, @"(?<!^)(?=[A-Z])");
            int comonWords = s2Words.Count(word => Array.Exists(s1Words, s => s.Equals(word)));

            if (comonWords >= s1Words.Length * similarity
                || comonWords >= s2Words.Length * similarity)
                return true;

            return false;
        }

        private static bool CharacterCountMatching(string s1, string s2, double similarity)
        {
            string commonSubstring = LCS(s1, s2);
            if (commonSubstring.Length >= s1.Length * similarity
                || commonSubstring.Length >= s2.Length * similarity)
                return true;
            return false;    
        }


        private static string LCS(string s1, string s2)
        {

            s1 = s1.ToLower();
            s2 = s2.ToLower();

            if (s1.Equals(s2))
                return s1;

            var limits = new int[s1.Length,s2.Length];
          
            for (int is1 = 0; is1 < s1.Length; is1++)
            {
                if (s1[is1] == s2[0]) limits[is1,0] = 1;
                // else it's by default 0
            }
            for (int is2 = 0; is2 < s2.Length; is2++)
            {
                if (s2[is2] == s1[0]) limits[0,is2] = 1;
                // else it's by default 0
            }

            /*calculam lungimea pentru toate celelalte elemente din L */
            for (int is1 = 1; is1 < s1.Length; is1++)
            {
                for (int is2 = 1; is2 < s2.Length; is2++)
                {
                    if (s1[is1] == s2[is2])
                    {
                        limits[is1,is2] = limits[is1 - 1, is2 - 1] + 1;
                    }
                    else
                    {
                        limits[is1,is2] = Math.Max(limits[is1 - 1,is2], limits[is1,is2 - 1]);
                    }
                }
            }

            int len = limits[s1.Length - 1,s2.Length - 1];
            var rez = new char[len];
            
            /*reconstituim subsirul in rez */
            for (int is1 = s1.Length - 1, is2 = s2.Length - 1; is1 >= 0 && is2 >= 0; )
            {
                if (s1[is1] == s2[is2])
                {
                    rez[--len] = s1[is1];
                    is1--;
                    is2--;
                }
                else
                {
                    if (is1 - 1 < 0 || is2 - 1 < 0)
                        break;
                    if (limits[is1 - 1,is2] > limits[is1,is2 - 1])
                    {
                        is1--;
                    }
                    else
                    {
                        is2--;
                    }
                }
            }

            return new string(rez);
        }

        public static string ReadProgramFromFile(string fileText1)
        {
            return File.ReadAllText(fileText1);
        }

        public static string CombineVarNames(string s1, string s2)
        {
            if (Common.Common.VariableNameMatching == VariableNameMatching.CharacterCount)
               return "#" + LCS(s1, s2) + "#"; 

            if (Common.Common.VariableNameMatching == VariableNameMatching.WordCount)
            {
                string[] s1Words = Regex.Split(s1, @"(?<!^)(?=[A-Z])");
                string[] s2Words = Regex.Split(s2, @"(?<!^)(?=[A-Z])");

                return "#" + s1Words.Where(subWord => s2Words.Contains(subWord)).Aggregate("", (current, subWord) => current + subWord) + "#";
            }

            return "";
        }
    }
}
