using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class ExpressionStatement : BasicNodeRewrite
    {

        public static readonly ExpressionStatement Instance = new ExpressionStatement();
        private ExpressionStatement()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            
            bool hasKnownChildren = false;

            foreach (AstNode child in node.Children)
            {
                hasKnownChildren |= Rewrite.Instance.MakeTreeNode(child);
            }

            if ( !hasKnownChildren )
                ParseInit(tagName, node.StartLocation.Line, false);
        }
    }
}
