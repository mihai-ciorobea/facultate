using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using Licenta_Compilator.Pattern;
using Licenta_Compilator.Text;
using Licenta_Compilator.TreeRewrite;
using Licenta_Compilator.UI;
using System.Linq;
using CompareMethod = Licenta_Compilator.Common.CompareMethod;

namespace Licenta_Compilator.Analize
{
    public class Analize
    {
        private readonly string _programText1;
        private readonly string _programText2;

        private static Rewrite _p1ParseTree;
        private static Rewrite _p2ParseTree;

        private List<Dictionary<int, List<int>>> _lines;
        private readonly GreedyStringTiling _greedyStringTilting;
        private static List<string> _varPrg1;
        private static List<string> _varPrg2;
        private static List<string> _finalVarName;
        private readonly List<int>_extraLines;
        private string _finalCodeText = "";


        public Analize(string fileText1, string fileText2)
        {
            if ( fileText1 != null ) 
                _programText1 = StringUtils.ReadProgramFromFile(fileText1);
            if (fileText2 != null) 
                _programText2 = StringUtils.ReadProgramFromFile(fileText2);

            _greedyStringTilting = new GreedyStringTiling();

            _varPrg1 = new List<string>();
            _varPrg2 = new List<string>();
            _extraLines = new List<int>();
            _finalVarName = new List<string>();
        }


        private  void ParseTree()
        {
            _p1ParseTree = Rewrite.Instance.ParseTree(_programText1);
            Rewrite.NextInstance();     //TODO replace this method
            _p2ParseTree = Rewrite.Instance.ParseTree(_programText2);
        }

        public void Start()
        {

            ParseTree();
            //TODO delete printing
            foreach (Node node in _p1ParseTree.GetNodes())
            {
                Console.WriteLine(node.ToString());
            }

            Console.WriteLine("++++++++++++++++++++");
            //TODO delete printing);
            foreach (Node node in _p2ParseTree.GetNodes())
            {
                Console.WriteLine(node.ToString());
            }


            bool before = Array.Exists(Common.Common.CompareMethod, s => s.Equals(CompareMethod.Before));
            bool after = Array.Exists(Common.Common.CompareMethod, s => s.Equals(CompareMethod.After));
            _greedyStringTilting.Compare(_p1ParseTree.GetNodes(), _p2ParseTree.GetNodes(), before, after);

             _lines = _greedyStringTilting.GetMatchingLines();
            
        }

        private Dictionary<int, string> GetFinalProgramText(List<Dictionary<int, List<int>>> lines)
        {
            Dictionary<int, string> finalProgram = new Dictionary<int, string>();

            int p1LineNr = StringUtils.GetLineNumbers(_programText1);
            int p2LineNr = StringUtils.GetLineNumbers(_programText2);

            Dictionary<int, List<int>> p1MatchingLines = lines[0];
            Dictionary<int, List<int>> p2MatchingLines = lines[1];

            for (int index = 1; index <= p1LineNr || index <= p2LineNr; index++)
            {
                finalProgram.Add(index, "");

                Console.ForegroundColor = ConsoleColor.White; 
                Console.Write(index + ":");
                if (index <= p1LineNr)
                {
                    bool found = false;

                    string linePrg1 = StringUtils.GetLine(_programText1, index);
                    foreach (int colorId in p1MatchingLines.Keys)
                    {
                        List<int> p1Lines;
                        p1MatchingLines.TryGetValue(colorId, out p1Lines);
                        if (p1Lines.Contains(index))
                        {
                            Console.ForegroundColor = Color.GetColorWithId(colorId);
                            Console.Write(linePrg1.PadRight(Common.Common.Padding));
                            found = true;

                            if (Common.Common.UseDefaultProgram == 1)
                                finalProgram[index] = linePrg1;
                            break;
                        }
                    }

                    if (found == false)
                    {
                        Console.ForegroundColor = ConsoleColor.White;
                        Console.Write(linePrg1.PadRight(Common.Common.Padding));
                        if (_extraLines.Contains(index))
                            finalProgram[index] = linePrg1;
                    }
                }
                else
                {
                    Console.Write("".PadRight(Common.Common.Padding));
                }

                Console.ForegroundColor = ConsoleColor.White;
                Console.Write(index + ":");
                if (index <= p2LineNr)
                {
                    bool found = false;

                    string linePrg2 = StringUtils.GetLine(_programText2, index);
                    foreach (int colorId in p2MatchingLines.Keys)
                    {
                        List<int> p2Lines;
                        p2MatchingLines.TryGetValue(colorId, out p2Lines);
                        if (p2Lines.Contains(index))
                        {
                            Console.ForegroundColor = Color.GetColorWithId(colorId);
                            Console.WriteLine(linePrg2);

                            if (Common.Common.UseDefaultProgram == 2)
                                finalProgram[index] = linePrg2;
                            found = true;
                        }
                    }

                    if (found == false)
                    {
                        Console.ForegroundColor = ConsoleColor.White;
                        Console.WriteLine(linePrg2);
                        if (_extraLines.Contains(index))
                            finalProgram[index] = linePrg2;
                    } 
                }
            }

            return finalProgram;
        }
  
        public static bool MatchBluePrint(string var1, string var2)
        {
            List<string[]> varUsage1 = _p1ParseTree.IdentifiersUsages[var1];
            List<string[]> varUsage2 = _p2ParseTree.IdentifiersUsages[var2];

            int equal = (from stack1 in varUsage1 from stack2 in varUsage2 where stack1.SequenceEqual(stack2) select stack1).Count();
            if (equal >= varUsage1.Count * Common.Common.VariableStackSimilarity || equal >= varUsage2.Count * Common.Common.VariableStackSimilarity)
                return true;
            return false;
        }

        public string FinalProgramText()
        {
            if (_finalCodeText != "")
                return _finalCodeText;

            Dictionary<int, string> finalPrg = GetFinalProgramText(_lines);
            string finalText = "";
            foreach (int lineNr in finalPrg.Keys.ToList())
            {
                string lineText = finalPrg[lineNr];
                List<string> renameVariables = Common.Common.UseDefaultProgram == 1 ? _varPrg1 : _varPrg2;
                foreach (string renameVariable in renameVariables.Distinct())
                {
                    string pattern = @"\b" + renameVariable + @"\b";
                    lineText = new Regex(pattern).Replace(lineText, _finalVarName[renameVariables.IndexOf(renameVariable)]);
                }

                finalText += lineText + "\n";
            }

            return finalText;
        }

        public static void AddVariable(string s1, string s2)
        {
            int indexVar1 = _varPrg1.IndexOf(s1);
            int indexVar2 = _varPrg2.IndexOf(s2);

            if (indexVar1 == indexVar2 && indexVar1 != -1)
                return;
            
            _varPrg1.Add(s1);
            _varPrg2.Add(s2);

            _finalVarName.Add(StringUtils.CombineVarNames(s1, s2));
        }

        public void ExtraLines(int line, bool add)
        {
            if (add)
                _extraLines.Add(line);
            else
                _extraLines.Remove(line);
        }

        public void ChangeDefaultProgram(int id)
        {
            Common.Common.UseDefaultProgram = id;
            _extraLines.Clear();
            _finalCodeText = "";
        }

        public void RenameVar(string varName, string newVarName)
        {
            int index1 = _varPrg1.IndexOf(varName);
            int index2 = _varPrg2.IndexOf(varName);
            if (index1 != -1 || index2 != -1)
                _finalVarName[index1 != -1 ? index1 : index2] = newVarName;
            else
            {
                _varPrg1.Add(varName);
                _varPrg2.Add(varName);
                _finalVarName.Add(newVarName);
            }
        }

        public void SaveTemplate(string templateName)
        {
            string text = _finalCodeText;
            System.IO.File.WriteAllText(@"C:\Mihai\Licenta\Licenta Compilator\Licenta Compilator\savedTemplate\" + templateName, text);
        }

        public void MakeFinal(string text)
        {
            _finalCodeText = text;
        }

        public void RenameVarAfterFinal(string varName, string newVarName)
        {
            string pattern = @"\b" + varName + @"\b";
            _finalCodeText = new Regex(pattern).Replace(_finalCodeText, newVarName);
        }

        public void EditCode(string newText)
        {
            _finalCodeText = newText;
        }

        public void Reset()
        {
            _finalCodeText = "";
            _extraLines.Clear();
        }
    }
}
