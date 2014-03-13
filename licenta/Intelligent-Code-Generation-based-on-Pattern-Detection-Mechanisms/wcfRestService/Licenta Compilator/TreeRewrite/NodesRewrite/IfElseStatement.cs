using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class IfElseStatement : BasicNodeRewrite
    {

        public static readonly IfElseStatement Instance = new IfElseStatement();
        private IfElseStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            //base.ParseRecursive(node, tagName);
            base.ParseRecursive(node, "", tagName, true, false);
        }
    }
}
