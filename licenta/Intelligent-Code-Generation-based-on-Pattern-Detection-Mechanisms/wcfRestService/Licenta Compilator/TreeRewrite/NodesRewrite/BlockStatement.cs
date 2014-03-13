using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class BlockStatement : BasicNodeRewrite
    {

        public static readonly BlockStatement Instance = new BlockStatement();
        private BlockStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
