using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class ContinueStatement : BasicNodeRewrite
    {

        public static readonly ContinueStatement Instance = new ContinueStatement();
        private ContinueStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.Parse(node, tagName);
        }
    }
}
