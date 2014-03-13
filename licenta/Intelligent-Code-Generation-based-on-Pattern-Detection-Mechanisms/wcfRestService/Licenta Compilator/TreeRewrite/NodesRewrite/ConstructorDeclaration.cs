using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class ConstructorDeclaration : BasicNodeRewrite
    {

        public static readonly ConstructorDeclaration Instance = new ConstructorDeclaration();
        private ConstructorDeclaration()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
