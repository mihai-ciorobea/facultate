using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class SwtichStatement : BasicNodeRewrite
    {

        public static readonly SwtichStatement Instance = new SwtichStatement();
        private SwtichStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
