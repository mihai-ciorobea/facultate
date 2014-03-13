using System;
using System.Reflection;
using Licenta_Compilator.Text;
using Licenta_Compilator.TreeRewrite.NodesRewrite;

namespace Licenta_Compilator.TreeRewrite
{
    class CompareMethods
    {
        public static readonly CompareMethods Instance = new CompareMethods();

        public static bool VariableCompareMethod(string[] s1, string[] s2)
        {
            bool equal = ArrayComparator(s1, s2, VariableDeclarationStatement.EqualIndexs);
            if (equal)
                Analize.Analize.AddVariable(s1[VariableDeclarationStatement.RealName], s2[VariableDeclarationStatement.RealName]);
            return equal;
        }

        public static bool FieldCompareMethod(string[] s1, string[] s2)
        {
            bool equal = ArrayComparator(s1, s2, FieldDeclaration.EqualIndexs);
            if (equal)
                Analize.Analize.AddVariable(s1[FieldDeclaration.RealName], s2[FieldDeclaration.RealName]);
            return equal;
        }

        public static bool TypeCompareMethod(string[] s1, string[] s2)
        {
            return ArrayComparator(s1, s2, TypeDeclaration.EqualIndexs);
        }


        public static bool MethodCompareMethod(string[] s1, string[] s2)
        {
            return ArrayComparator(s1, s2, MethodDeclaration.EqualIndexs);
        }
        




        public static bool DefaultMethod(string[] s1, string[] s2)
        {
            return true;
            //TODO

        }


        public static bool ArrayComparator(string[] s1, string[] s2, int[] equals)
        {
            foreach (int id in equals)
                if (s1[id] == null
                    || s2[id] == null
                    || !s1[id].Equals(s2[id]))
                    return false;

            for (int id = 0; id < s1.Length; id++)
            {
                int idCopy = id;
                int index = Array.FindIndex(equals, i => i == idCopy);
                if (index == -1)
                {
                    if (!StringUtils.Match(s1[id], s2[id], Common.Common.VariableNameSimilarity))
                        return false;
                }

            }
            return true;
        }

        public static Func<string[], string[], bool> GetFunctionWithName(string funcName)
        {
            MethodInfo methodInfo = Instance.GetType().GetMethod(funcName);

            if (methodInfo == null)
                return DefaultMethod;

            var method = typeof(CompareMethods).GetMethod(funcName, new[] { typeof(string[]), typeof(string[]) });
            var del = Delegate.CreateDelegate(typeof(Func<string[], string[], bool>), method);
            Func<string[], string[], bool> caller = (instance, param) => (bool)del.DynamicInvoke(instance, param);
            return caller;
        }
    }
}
