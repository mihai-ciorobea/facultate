using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class FieldDeclaration : AdvancedNodeRewrite
    {

        public static readonly FieldDeclaration Instance = new FieldDeclaration();
        //public static readonly int[] EqualIndexs = {0, 1, 2};
        public static readonly int[] EqualIndexs = {0, 1};
        public static readonly int RealName = 2;

        private FieldDeclaration()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            //ParseWithName(node, tagName, new[] { "Modifier", "Keyword", "Identifier", "Name" }, new[] { SimpleName, SimpleName, SimpleName, SimpleName });
            ParseWithName(node, tagName, new[] { "Modifier", "Keyword", "Name" }, new[] { SimpleName, SimpleName, SimpleName });
        }
    }
}
