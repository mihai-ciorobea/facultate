using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class AssignmentExpression : BasicNodeRewrite
    {

        public static readonly AssignmentExpression Instance = new AssignmentExpression();
        private AssignmentExpression()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.Parse(node, tagName);
        }
    }
}
