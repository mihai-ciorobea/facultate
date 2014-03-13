using System;
using System.Collections.Generic;
using System.Linq;
using Licenta_Compilator.TreeRewrite;

namespace Licenta_Compilator.Pattern
{
    class GreedyStringTiling
    {

        private readonly List<Section> _sections = new List<Section>();
        private readonly Dictionary<Token, List<Token>> _contextVariableMatching = new Dictionary<Token, List<Token>>();


        public void AddMatchingBluePrintAtBeginning(List<Node> nodesA, List<Node> nodesB)
        {
            List<Token> tokensA = nodesA.Select(node => new Token(node)).ToList();
            List<Token> tokensB = nodesB.Select(node => new Token(node)).ToList();

            for (int indexA = 0; indexA < tokensA.Count; indexA++)
            {
                for (int indexB = 0; indexB < tokensB.Count && indexA < tokensA.Count; indexB++)
                    if (tokensA[indexA].MatchVariableBluePrint(tokensB[indexB]))
                    {
                        if (_contextVariableMatching.ContainsKey(tokensA[indexA]))
                        {
                            _contextVariableMatching[tokensA[indexA]].Add(tokensB[indexB]);
                        }
                        else
                        {
                            _contextVariableMatching.Add(tokensA[indexA], new List<Token> { tokensB[indexB] });
                        }
                    }
            }
        }





        public void Compare(List<Node> nodesA, List<Node> nodesB, bool before, bool after)
        {
            List<Token> tokensA = nodesA.Select(node => new Token(node)).ToList();
            List<Token> tokensB = nodesB.Select(node => new Token(node)).ToList();
            int maxMatch;

            if (before) AddMatchingBluePrintAtBeginning(nodesA, nodesB);

            do
            {
                Section curentMatching = new Section();
                maxMatch = 0;
                for (int indexA = 0; indexA < tokensA.Count; indexA++)
                {
                    for (int indexB = 0; indexB < tokensB.Count; indexB++)
                    {
                        int commonMatchLength = 0;

                        while (indexA + commonMatchLength < tokensA.Count && indexB + commonMatchLength < tokensB.Count)
                        {

                            Token tokenA = tokensA[indexA + commonMatchLength];
                            Token tokenB = tokensB[indexB + commonMatchLength];

                            bool areContextVariabile = _contextVariableMatching.ContainsKey(tokenA) ? (_contextVariableMatching[tokenA].Contains(tokenB)) : false;

                            if (tokenA.Match(tokenB) || areContextVariabile)
                                commonMatchLength++;
                            else
                                break;

                        }

                        if (commonMatchLength > maxMatch)
                        {
                            curentMatching.StartIndexA = indexA;
                            curentMatching.StartIndexB = indexB;
                            curentMatching.Length = commonMatchLength;

                            maxMatch = commonMatchLength;
                        }
                    }
                }

                if (maxMatch < Common.Common.MinimumMatchLength)
                {
                    break;
                }

                curentMatching.AddNextTokenMatch(tokensA, tokensB);
                _sections.Add(curentMatching);

            } while (maxMatch > Common.Common.MinimumMatchLength);




            if ( after )
                for (int indexA = 0; indexA < tokensA.Count; indexA++)
                {
                    for (int indexB = 0; indexB < tokensB.Count && indexA < tokensA.Count; indexB++)
                        if (tokensA[indexA].MatchVariableBluePrint(tokensB[indexB]))
                        {
                            Section curentMatching = new Section();
                            curentMatching.StartIndexA = indexA;
                            curentMatching.StartIndexB = indexB;
                            curentMatching.Length = 1;
                            curentMatching.AddNextTokenMatch(tokensA, tokensB);
                            _sections.Add(curentMatching);
                        }
                }

            //TODO Delete -- matching parts
            for (int index = 0; index < _sections.Count; index++)
            {
                Console.WriteLine(_sections[index].ToString());
            }

        }




        public List<Dictionary<int, List<int>>> GetMatchingLines()
        {
            List<Dictionary<int, List<int>>> res = new List<Dictionary<int, List<int>>>();
            res.Add(new Dictionary<int, List<int>>());
            res.Add(new Dictionary<int, List<int>>());

            for (int index = 0; index < _sections.Count; index++)
            {
                Section section = _sections[index];
                foreach (Token firstDocumentToken in section.FirstDocumentTokens)
                {
                    List<int> lines;
                    res[0].TryGetValue(index, out lines);

                    if (lines != null)
                        lines.Add(firstDocumentToken.Node.Line);
                    else
                    {
                        lines = new List<int>();
                        lines.Add(firstDocumentToken.Node.Line);
                        res[0].Add(index, lines);
                    }
                }


                foreach (Token secondDocumentToken in section.SecondDocumentTokens)
                {
                    List<int> lines;
                    res[1].TryGetValue(index, out lines);

                    if (lines != null)
                        lines.Add(secondDocumentToken.Node.Line);
                    else
                    {
                        lines = new List<int>();
                        lines.Add(secondDocumentToken.Node.Line);
                        res[1].Add(index, lines);
                    }
                }
            }

            return res;
        }


    }

    class Token
    {
        public Node Node { get; set; }
        public Boolean IsMarked { get; set; }

        public Token(Node node)
        {
            Node = node;
            IsMarked = false;
        }

        public override bool Equals(object obj)
        {
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }

            Token t = (Token)obj;

            return Node.Equals(t.Node);
        }

        public override int GetHashCode()
        {
            return Node.GetHashCode();
        }

        public override string ToString()
        {
            return Node.ToString();
        }

        public bool Match(Token token)
        {
            return Node.Match(token.Node);
        }

        public bool MatchVariableBluePrint(Token token)
        {
            int indexA = -1;
            int indexB = -1;
            if (Node.Tag.ToString().Equals("VariableDeclarationStatement"))
                indexA = 1;
            if (Node.Tag.ToString().Equals("FieldDeclaration"))
                indexA = 2;

            if (token.Node.Tag.ToString().Equals("VariableDeclarationStatement"))
                indexB = 1;
            if (token.Node.Tag.ToString().Equals("FieldDeclaration"))
                indexB = 2;

            if (indexA != -1 && indexB != -1)
                return Analize.Analize.MatchBluePrint(Node.Properties[indexA], token.Node.Properties[indexB]);
            return false;
        }
    }

    class Section
    {
        public int Length { get; set; }
        public int StartIndexA { get; set; }
        public int StartIndexB { get; set; }

        public readonly List<Token> FirstDocumentTokens;
        public readonly List<Token> SecondDocumentTokens;

        public Section()
        {
            FirstDocumentTokens = new List<Token>();
            SecondDocumentTokens = new List<Token>();
        }

        public void AddNextTokenMatch(List<Token> tokensA, List<Token> tokensB)
        {
            for (int index = 0; index < Length; index++)
            {
                FirstDocumentTokens.Add(tokensA[StartIndexA + index]);
                SecondDocumentTokens.Add(tokensB[StartIndexB + index]);
            }

            tokensA.RemoveRange(StartIndexA, Length);
            tokensB.RemoveRange(StartIndexB, Length);
        }

        public override string ToString()
        {
            string text = "==================================================================\n";
            for (int index = 0; index < Length; index++)
            {
                text += FirstDocumentTokens[index].ToString().PadRight(Common.Common.Padding) + " | " + SecondDocumentTokens[index] + "\n";
            }


            return text;
        }
    }
}
