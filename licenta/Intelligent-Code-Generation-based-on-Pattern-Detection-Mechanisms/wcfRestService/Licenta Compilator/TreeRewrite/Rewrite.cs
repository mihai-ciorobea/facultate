using System.Collections;
using System.Collections.Generic;
using ICSharpCode.NRefactory.CSharp;
using System;

namespace Licenta_Compilator.TreeRewrite
{
    class Rewrite
    {
        public static Rewrite Instance;
        private readonly List<Node> _programNodes;
        public Dictionary<string, List<string[]>> IdentifiersUsages { get; set; }
        private readonly Stack _labelStack;

        #region Instances Pool


        static Rewrite()
        {
            Instance = new Rewrite();
        }

       
        public static void NextInstance()
        {
            Instance = new Rewrite();
        }

        #endregion


        private Rewrite()
        {
            _programNodes = new List<Node>();
            _labelStack = new Stack();
            IdentifiersUsages = new Dictionary<string, List<string[]>>();
        }


        public Rewrite ParseTree(String text)
        {
            _programNodes.Clear();
            SyntaxTree syntaxTree = new CSharpParser().Parse(text, "demo.cs");

            foreach (var element in syntaxTree.Children)
            {
                MakeTreeNode(element);
            }

            foreach (var element in syntaxTree.Children)
            {
                CreateVariableBluePrint(element);
            }


            return this;
        }

        public bool MakeTreeNode(AstNode node)
        {

            Array treeTags = Enum.GetValues(typeof(TreeTagName));
            foreach (TreeTagName treeTag in treeTags)
            {
                if (GetNodeTitle(node).Contains(treeTag.ToString()))
                {
                    StringEnum.GetStringValue(treeTag).Invoke(node, treeTag);
                    return true;
                }
            }
            return false;
        }

        private static string GetNodeTitle(AstNode node)
        {
            return node.GetType().Name;
        }

        public void AddNode(Node node)
        {
            _programNodes.Add(node);
        }

        public void PrintNodes()
        {
            foreach (var programNode in _programNodes)
            {
                Console.WriteLine(programNode.ToString());
            }
        }

        public List<Node> GetNodes()
        {
            return _programNodes;
        }





        void CreateVariableBluePrint(AstNode node)
        {
            string tagName = GetNodeTitle(node);

            if (tagName.Equals("Identifier"))
            {
                if (IdentifiersUsages.ContainsKey(node.GetText()))
                {
                    List<string[]> stacksList = IdentifiersUsages[node.GetText()];
                    string[] stack = StackToStringArray(_labelStack);

                    stacksList.Add(stack);
                }
                else
                {
                    List<string[]> stackList = new List<string[]>();
                    string[] stack = StackToStringArray(_labelStack);
                    stackList.Add(stack);

                    IdentifiersUsages.Add(node.GetText(), stackList);
                }
            }

            _labelStack.Push(tagName);

            foreach (AstNode child in node.Children)
            {
                CreateVariableBluePrint(child);
            }

            _labelStack.Pop();
        }

        private string[] StackToStringArray(Stack stack)
        {
            string[] res = new string[stack.Count];
            stack.ToArray().CopyTo(res, 0);
            return res;
        }

    }


}
