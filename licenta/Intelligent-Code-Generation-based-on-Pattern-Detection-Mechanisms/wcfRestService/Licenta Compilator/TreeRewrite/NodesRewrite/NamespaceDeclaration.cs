using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class NamespaceDeclaration : BasicNodeRewrite
    {
        public static readonly NamespaceDeclaration Instance = new NamespaceDeclaration();
        private NamespaceDeclaration()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
