using System;
using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class VariableDeclarationStatement : AdvancedNodeRewrite
    {

        public static readonly VariableDeclarationStatement Instance = new VariableDeclarationStatement();
        //public static readonly int[] EqualIndexs = {0, 1};
        public static readonly int[] EqualIndexs = {0};
        public static readonly int RealName = 1;

        private VariableDeclarationStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
           // ParseWithName(node, tagName, new[] { "Keyword", "Identifier", "Name" }, new[] { SimpleName, SimpleName, SimpleName });
            ParseRecursiveWithName(node, tagName, new[] { "Keyword", "Name" }, new[] { SimpleName, SimpleName });
        }
    }
}


//TODO remove comments