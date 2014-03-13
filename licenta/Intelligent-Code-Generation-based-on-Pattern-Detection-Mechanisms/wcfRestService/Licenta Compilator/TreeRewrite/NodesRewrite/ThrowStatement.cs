using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class ThrowStatement : BasicNodeRewrite
    {

        public static readonly ThrowStatement Instance = new ThrowStatement();
        private ThrowStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.Parse(node, tagName);
        }
    }
}
