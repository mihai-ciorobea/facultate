using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class BinaryStatement : BasicNodeRewrite
    {

        public static readonly BinaryStatement Instance = new BinaryStatement();
        private BinaryStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
