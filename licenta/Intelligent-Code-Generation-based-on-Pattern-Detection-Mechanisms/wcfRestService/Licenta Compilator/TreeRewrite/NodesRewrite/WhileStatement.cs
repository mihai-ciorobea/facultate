using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class WhileStatement : BasicNodeRewrite
    {

        public static readonly WhileStatement Instance = new WhileStatement();
        private WhileStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
