using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class TryCatchStatement : BasicNodeRewrite
    {

        public static readonly TryCatchStatement Instance = new TryCatchStatement();
        private TryCatchStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
