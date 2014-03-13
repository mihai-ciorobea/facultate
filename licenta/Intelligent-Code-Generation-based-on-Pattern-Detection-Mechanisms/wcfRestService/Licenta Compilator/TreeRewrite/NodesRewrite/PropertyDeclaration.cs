using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class PropertyDeclaration : BasicNodeRewrite
    {
        public static readonly PropertyDeclaration Instance = new PropertyDeclaration();
        private PropertyDeclaration()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.Parse(node, tagName);
        }
    }
}
