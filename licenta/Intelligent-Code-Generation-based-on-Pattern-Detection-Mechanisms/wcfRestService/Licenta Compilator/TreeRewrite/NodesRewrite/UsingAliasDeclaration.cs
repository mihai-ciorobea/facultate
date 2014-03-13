using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class UsingAliasDeclaration : BasicNodeRewrite
    {

        public static readonly UsingAliasDeclaration Instance = new UsingAliasDeclaration();
        private UsingAliasDeclaration()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.Parse(node, tagName);
            //TODO
        }
    }
}
