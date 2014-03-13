using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class IdentifierExpression : BasicNodeRewrite
    {

        public static readonly IdentifierExpression Instance = new IdentifierExpression();
        private IdentifierExpression()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.Parse(node, tagName);
        }
    }
}
