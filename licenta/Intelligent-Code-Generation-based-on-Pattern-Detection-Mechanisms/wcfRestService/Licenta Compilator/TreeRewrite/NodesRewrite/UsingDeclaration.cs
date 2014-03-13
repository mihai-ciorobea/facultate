using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class UsingDeclaration : AdvancedNodeRewrite
    {
        public static readonly UsingDeclaration Instance = new UsingDeclaration();
        private UsingDeclaration()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseWithName(node, tagName, new[] { "Parent" }, new[] { base.UsingName });
        }
    }

}
