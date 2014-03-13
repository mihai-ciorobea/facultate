using System;
using System.Linq;
using System.Reflection;
using ICSharpCode.NRefactory.CSharp;

namespace Licenta_Compilator.TreeRewrite.NodesRewrite
{
    class AdvancedNodeRewrite : BasicNodeRewrite
    {

        protected Func<object, object> UsingName = val => ((ICSharpCode.NRefactory.CSharp.UsingDeclaration)val).Namespace;
        protected Func<object, object> SimpleName = val => val;
        protected Func<object, object> Parameter = val => val;



        protected void ParseRecursiveWithName(AstNode node, TreeTagName nodeType, string[] propertyNames, Func<object, object>[] parseNames)
        {
            string[] nodeInfo = Enumerable.Repeat("", propertyNames.Length).ToArray();

            foreach (AstNode astNode in node.Children)
            {
                foreach (PropertyInfo p in astNode.GetType().GetProperties(BindingFlags.Public | BindingFlags.Instance))
                {
                    PropertyInfo p1 = p;
                    int index = Array.FindIndex(propertyNames, s => s.Equals(p1.Name));
                    if (index != -1)
                        try
                        {
                            var val = p.GetValue(astNode, null);
                            val = parseNames[index].Invoke(val);
                            if (String.IsNullOrEmpty(nodeInfo[index]))
                                nodeInfo[index] = val == null ? "" : val.ToString();
                        }
                        catch (TargetInvocationException) { }
                }
            }

            ParseRecursiveWithNodeInfo(node, nodeInfo, nodeType, true, true);
        }




        protected void ParseWithName(AstNode node, TreeTagName nodeType, string[] propertyNames, Func<object, object>[] parseNames)
        {
            string[] nodeInfo = Enumerable.Repeat("", propertyNames.Length).ToArray();


            foreach (AstNode astNode in node.Children)
            {
                foreach (PropertyInfo p in astNode.GetType().GetProperties(BindingFlags.Public | BindingFlags.Instance))
                {
                    PropertyInfo p1 = p;
                    int index = Array.FindIndex(propertyNames, s => s.Equals(p1.Name));
                    if (index != -1)
                        try
                        {
                            var val = p.GetValue(astNode, null);
                            val = parseNames[index].Invoke(val);
                            nodeInfo[index] = val == null ? "" : val.ToString();
                        }
                        catch (TargetInvocationException) { }
                }
            }
            Rewrite.Instance.AddNode(new Node(nodeType, "", node.StartLocation.Line, nodeInfo));
            return;
        }
    }
}
