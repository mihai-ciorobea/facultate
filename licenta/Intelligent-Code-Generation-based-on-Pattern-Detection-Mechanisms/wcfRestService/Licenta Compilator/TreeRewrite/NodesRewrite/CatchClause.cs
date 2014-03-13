using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class CatchClause : BasicNodeRewrite
    {

        public static readonly CatchClause Instance = new CatchClause();
        private CatchClause()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
