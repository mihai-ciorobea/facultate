using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class DelegateDeclaration : BasicNodeRewrite
    {

        public static readonly DelegateDeclaration Instance = new DelegateDeclaration();
        private DelegateDeclaration()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
