using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class ForeachStatement : BasicNodeRewrite
    {

        public static readonly ForeachStatement Instance = new ForeachStatement();
        private ForeachStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, "", tagName, true, false);
        }
    }
}
