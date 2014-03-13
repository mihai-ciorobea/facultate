using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class CaseLabel : BasicNodeRewrite
    {

        public static readonly CaseLabel Instance = new CaseLabel();
        private CaseLabel()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.Parse(node, tagName);
        }
    }
}
