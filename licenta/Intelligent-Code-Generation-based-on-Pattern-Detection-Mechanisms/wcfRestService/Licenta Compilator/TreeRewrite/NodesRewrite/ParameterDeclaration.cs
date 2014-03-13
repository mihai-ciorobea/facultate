using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class ParameterDeclaration : BasicNodeRewrite
    {
        public static readonly ParameterDeclaration Instance = new ParameterDeclaration();
        private ParameterDeclaration()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
