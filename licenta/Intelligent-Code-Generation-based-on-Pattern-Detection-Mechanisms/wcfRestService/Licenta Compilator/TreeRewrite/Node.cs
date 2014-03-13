using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Licenta_Compilator.Text;

namespace Licenta_Compilator.TreeRewrite
{
    class Node
    {
        public string Ending { get; set; }
        public string[] Properties { get; set; }
        public int Line { get; set; }
        public TreeTagName Tag { get; set; }

        public Node(TreeTagName tag, int line): this(tag, tag.ToString(), line)
        {
        }
        public Node(TreeTagName tag, string fullName, int line): this(tag, fullName, line, new string[] {})
        {
        }
      

        public Node(TreeTagName tag, string fullName, int line, string[] names)
        {
            Tag = tag;
            Line = line;
            Properties = names;
            Ending = fullName;
        }

        public override bool Equals(object obj)
        {
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }

            var node = (Node)obj;
            return Tag.Equals(node.Tag);
        }

        public override int GetHashCode()
        {
            return base.GetHashCode();
        }

        public override String ToString()
        {
            return (Tag + ""+ Ending +" " + string.Join(",", Properties) + " " + Line).PadRight(40, ' ');
        }

        public bool Match(Node n)
        {
            if (!n.Tag.Equals(Tag))
                return false;

            if (n.Properties.Length != Properties.Length)
                return false;


            return StringEnum.GetCompareMethod(Tag).Invoke(Properties, n.Properties);
        }

    }
}
