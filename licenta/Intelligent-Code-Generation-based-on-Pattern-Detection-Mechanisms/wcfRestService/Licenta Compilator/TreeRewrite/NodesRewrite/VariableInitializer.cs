using System;
using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class VariableInitializer : AdvancedNodeRewrite
    {

        public static readonly VariableInitializer Instance = new VariableInitializer();
        //public static readonly int[] EqualIndexs = {0, 1};
       // public static readonly int[] EqualIndexs = {0};
       // public static readonly int RealName = 1;

        private VariableInitializer()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
           // ParseWithName(node, tagName, new[] { "Keyword", "Identifier", "Name" }, new[] { SimpleName, SimpleName, SimpleName });
            //ParseWithName(node, tagName, new[] { "Keyword", "Name" }, new[] { SimpleName, SimpleName });
            base.ParseRecursive(node, tagName);
        }
    }
}


//TODO remove comments