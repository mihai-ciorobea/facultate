using System.Diagnostics;

namespace Licenta_Compilator.TreeRewrite
{
    class UtilRewrite
    {
        public static string GetCurrentMethod()
        {
            var st = new StackTrace();
            StackFrame sf = st.GetFrame(1);

            return sf.GetMethod().DeclaringType.Name;
        }
    }
}
