using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class EventDeclaration : BasicNodeRewrite
    {

        public static readonly EventDeclaration Instance = new EventDeclaration();
        private EventDeclaration()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
