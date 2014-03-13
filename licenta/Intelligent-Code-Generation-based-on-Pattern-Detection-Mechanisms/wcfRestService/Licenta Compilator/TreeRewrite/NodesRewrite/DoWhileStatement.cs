using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class DoWhileStatement : BasicNodeRewrite
    {

        public static readonly DoWhileStatement Instance = new DoWhileStatement();
        private DoWhileStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
