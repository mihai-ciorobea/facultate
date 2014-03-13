using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class GotoStatement : BasicNodeRewrite
    {

        public static readonly GotoStatement Instance = new GotoStatement();
        private GotoStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.Parse(node, tagName);
        }
    }
}
