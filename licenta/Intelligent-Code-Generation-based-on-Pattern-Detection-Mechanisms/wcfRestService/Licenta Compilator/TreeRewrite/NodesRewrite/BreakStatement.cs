using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class BreakStatement : BasicNodeRewrite
    {

        public static readonly BreakStatement Instance = new BreakStatement();
        private BreakStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.Parse(node, tagName);
        }
    }
}
