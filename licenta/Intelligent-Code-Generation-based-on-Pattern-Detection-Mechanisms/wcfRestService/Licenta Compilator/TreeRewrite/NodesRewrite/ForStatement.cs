using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class ForStatement : BasicNodeRewrite
    {

        public static readonly ForStatement  Instance = new ForStatement();
        private ForStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, "", tagName, true, false);
        }
    }
}
