using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class InvocationExpression : BasicNodeRewrite
    {

        public static readonly InvocationExpression Instance = new InvocationExpression();
        private InvocationExpression()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
