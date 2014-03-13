using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class TypeDeclaration : AdvancedNodeRewrite
    {
        public static readonly TypeDeclaration Instance = new TypeDeclaration();
        public static readonly int[] EqualIndexs = {};

        private TypeDeclaration()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            ParseRecursiveWithName(node, tagName, new[] { "Name" }, new[] { SimpleName });
        }
    }
}
