using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class EnumMemberDeclaration : BasicNodeRewrite
    {

        public static readonly EnumMemberDeclaration Instance = new EnumMemberDeclaration();
        private EnumMemberDeclaration()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
