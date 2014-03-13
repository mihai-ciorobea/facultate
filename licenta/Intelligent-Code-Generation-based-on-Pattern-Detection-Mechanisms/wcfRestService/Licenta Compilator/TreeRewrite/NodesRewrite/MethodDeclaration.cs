using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class MethodDeclaration : AdvancedNodeRewrite
    {
        public static readonly MethodDeclaration Instance = new MethodDeclaration();
        public static readonly int[] EqualIndexs = { 0, 1 };

        private MethodDeclaration()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
           // ParseRecursiveWithName(node, tagName, "Name", base.SimpleName);

            //Keyword
                                                                        //KnownTypeCode    Name
            //TODO -- parameter ???
            ParseRecursiveWithName(node, tagName, new[] { "Modifier", "Keyword", "Name", "Parameter" }, new[] { SimpleName, SimpleName, SimpleName, Parameter });
        }
    }
}
