using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class ObjectCreateExpression : BasicNodeRewrite
    {

        public static readonly ObjectCreateExpression Instance = new ObjectCreateExpression();
        private ObjectCreateExpression()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.Parse(node, tagName);
        }
    }
}
