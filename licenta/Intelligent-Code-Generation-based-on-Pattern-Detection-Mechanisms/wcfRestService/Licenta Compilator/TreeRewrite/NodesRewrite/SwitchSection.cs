using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class SwitchSection : BasicNodeRewrite
    {

        public static readonly SwitchSection Instance = new SwitchSection();
        private SwitchSection()
        {
        }

        public void Parse(AstNode node, TreeTagName tagName)
        {
            base.ParseRecursive(node, tagName);
        }
    }
}
