using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class PrimitiveExpression : BasicNodeRewrite
    {
        public static readonly PrimitiveExpression Instance = new PrimitiveExpression();
        private PrimitiveExpression()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.Parse(node, tagName);
        }
    }
}
