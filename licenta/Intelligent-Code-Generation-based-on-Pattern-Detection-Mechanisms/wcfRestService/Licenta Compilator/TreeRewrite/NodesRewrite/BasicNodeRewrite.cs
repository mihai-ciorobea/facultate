using System;
using ICSharpCode.NRefactory.CSharp;
using ICSharpCode.NRefactory.Demo.Logging;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class BasicNodeRewrite
    {
        
        private void ParseInit(TreeTagName nodeType, string nodeName, int line)
        {
            ParseInit(nodeType, nodeName, line, true);
        }

        protected void ParseInit(TreeTagName nodeType, int line, bool isRecursive)
        {
            ParseInit(nodeType, "", line, isRecursive);
        }

        private void ParseInit(TreeTagName nodeType, string nodeName, int line, bool isRecursive)
        {
            Rewrite.Instance.AddNode(new Node(nodeType,  (isRecursive ? "_start" : ""), line, new[] {nodeName}));
        }


        private void ParseEnd(TreeTagName nodeType, int line)
        {
            Rewrite.Instance.AddNode(new Node(nodeType, "_end", line));
        }

        protected void ParseRecursive(AstNode node, string nodeName, TreeTagName nodeType, bool start, bool end)
        {
            if ( start ) ParseInit(nodeType, nodeName, node.StartLocation.Line);

            foreach (AstNode child in node.Children)
            {
                Rewrite.Instance.MakeTreeNode(child);
            }

            if ( end )ParseEnd(nodeType, node.EndLocation.Line);
        }

        protected void ParseRecursive(AstNode node, string nodeName, TreeTagName nodeType)
        {
            ParseRecursive(node, nodeName, nodeType, true, true);
        }


        protected void ParseRecursive(AstNode node, TreeTagName nodeType)
        {
           ParseRecursive(node, "", nodeType);
        }
      

        protected void Parse(AstNode node, TreeTagName nodeType)
        {
            ParseInit(nodeType, node.StartLocation.Line, false);
        }






        protected void ParseRecursiveWithNodeInfo(AstNode node, string[] nodeInfo, TreeTagName nodeType, bool start, bool end)
        {
            if (start) ParseInitWithNodeInfo(nodeType, nodeInfo, node.StartLocation.Line);

            foreach (AstNode child in node.Children)
            {
                Rewrite.Instance.MakeTreeNode(child);
            }

            if (end) ParseEndWithNodeInfo(nodeType, nodeInfo, node.EndLocation.Line);
        }

        private void ParseInitWithNodeInfo(TreeTagName nodeType, string[] nodeInfo, int line)
        {
            Rewrite.Instance.AddNode(new Node(nodeType,"_start", line, nodeInfo));
        }

        private void ParseEndWithNodeInfo(TreeTagName nodeType, string[] nodeInfo, int line)
        {
            Rewrite.Instance.AddNode(new Node(nodeType, "_end", line, nodeInfo));
        }
    }

}
