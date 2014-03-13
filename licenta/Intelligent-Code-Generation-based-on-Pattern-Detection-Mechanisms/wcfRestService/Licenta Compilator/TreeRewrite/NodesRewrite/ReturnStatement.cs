using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class ReturnStatement : BasicNodeRewrite
    {

        public static readonly ReturnStatement Instance = new ReturnStatement();
        private ReturnStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
