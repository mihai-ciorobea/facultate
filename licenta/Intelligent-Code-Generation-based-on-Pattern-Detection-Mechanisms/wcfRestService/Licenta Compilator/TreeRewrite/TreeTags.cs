using System;
using System.Reflection;
using ICSharpCode.NRefactory.CSharp;
using Attribute = System.Attribute;

namespace Licenta_Compilator.TreeRewrite
{

    static class StringEnum
    {
        public static StringValue GetStringValue(Enum value)
        {
            StringValue output = null;
            Type type = value.GetType();

            FieldInfo fi = type.GetField(value.ToString());
            var attrs = fi.GetCustomAttributes(typeof(StringValue), false) as StringValue[];
            if (attrs != null && attrs.Length > 0)
            {
                output = attrs[0];
            }

            return output;
        }


        public static CompareMethod GetCompareMethod(Enum value)
        {
            CompareMethod compareMethod = (CompareMethod)GetAttributeValue(value, typeof(CompareMethod));

            return compareMethod == null ? CompareMethod.Default : compareMethod;
        }

        private static object GetAttributeValue(Enum value, Type returnType)
        {
            Object output = null;
            Type type = value.GetType();

            FieldInfo fi = type.GetField(value.ToString());
            var attrs = fi.GetCustomAttributes(returnType, false) as Object[];
            if (attrs != null && attrs.Length > 0)
            {
                output = attrs[0];
            }

            return output;
        }
    }
    enum TreeTagName
    {
        [StringValue("UsingAliasDeclaration")]
        UsingAliasDeclaration,

        [StringValue("UsingDeclaration")]
        UsingDeclaration,

        [StringValue("NamespaceDeclaration")]
        NamespaceDeclaration,

        [StringValue("TypeDeclaration")]
        [CompareMethod("TypeCompareMethod")]
        TypeDeclaration,

        [StringValue("FieldDeclaration")]
        [CompareMethod("FieldCompareMethod")]
        FieldDeclaration,

        [StringValue("PropertyDeclaration")]
        PropertyDeclaration,

        [StringValue("ConstructorDeclaration")]
        ConstructorDeclaration,

        [StringValue("MethodDeclaration")]
        [CompareMethod("MethodCompareMethod")]
        MethodDeclaration,

        [StringValue("BlockStatement")]
        BlockStatement,

        //[StringValue("BinaryStatement")]
        //BinaryStatement,

        [StringValue("ExpressionStatement")]
        ExpressionStatement,

        [StringValue("InvocationExpression")]
        InvocationExpression,

        [StringValue("AssignmentExpression")]
        AssignmentExpression,

        [StringValue("VariableDeclarationStatement")]
        [CompareMethod("VariableCompareMethod")]
        VariableDeclarationStatement,

        [StringValue("ReturnStatement")]
        ReturnStatement,

        [StringValue("ThrowStatement")]
        ThrowStatement,

        [StringValue("BreakStatement")]
        BreakStatement,

        [StringValue("TryCatchStatement")]
        TryCatchStatement,

        [StringValue("CatchClause")]
        CatchClause,

        [StringValue("ForeachStatement")]
        ForeachStatement,

        [StringValue("ForStatement")]
        ForStatement,

        [StringValue("WhileStatement")]
        WhileStatement,

        [StringValue("DoWhileStatement")]
        DoWhileStatement,

        [StringValue("SwtichStatement")]
        SwtichStatement,

        [StringValue("SwitchSection")]
        SwitchSection,

        [StringValue("CaseLabel")]
        CaseLabel,

        [StringValue("ExpresionStatement")]
        ExpresionStatement,

        [StringValue("IfElseStatement")]
        IfElseStatement,

        [StringValue("ContinueStatement")]
        ContinueStatement,

        [StringValue("DelegateDeclaration")]
        DelegateDeclaration,

        [StringValue("EnumMemberDeclaration")]
        EnumMemberDeclaration,

        [StringValue("EventDeclaration")]
        EventDeclaration,

        [StringValue("GotoStatement")]
        GotoStatement,

        [StringValue("PrimitiveExpression")]
        PrimitiveExpression,


        [StringValue("IdentifierExpression")]
        IdentifierExpression,


        [StringValue("ObjectCreateExpression")]
        ObjectCreateExpression,


        [StringValue("VariableInitializer")]
        VariableInitializer,

        [StringValue("ParameterDeclaration")]
        ParameterDeclaration,


        //



        [StringValue("GotoStatement")]
        LoopDeclaration

        /* CompareMethod :
            * VariableDeclarationStatement
            * FieldDeclaration
        */
    }

    internal class CompareMethod : Attribute
    {
        private Func<string[], string[], bool> _func;
        public static CompareMethod Default = new CompareMethod("");

        public CompareMethod(string funcName)
        {
            _func = CompareMethods.GetFunctionWithName(funcName);
        }

        public Func<string[], string[], bool> GetFunc
        {
            get { return _func; }
        }

        public bool Invoke(string[] s1, string[] s2)
        {
            return _func.Invoke(s1, s2);
        }
    }

    class StringValue : Attribute
    {
        private readonly string _value;

        public StringValue(string value)
        {
            _value = value;
        }

        public string Value
        {
            get { return _value; }
        }

        public void Invoke(AstNode node, TreeTagName treeTag)
        {

            Type type = Type.GetType("Licenta_Compilator.TreeRewrite.NodesRewrite." + _value, true);

            if (type == null)
                throw new Exception("While retrieving the type" + _value);

            MethodInfo method = type.GetMethod("Parse");
            if (method == null)
                throw new MissingMethodException("While retrieving the method: Parse from " + _value);


            FieldInfo field = type.GetField("Instance", BindingFlags.Static | BindingFlags.Public);
            if (field != null)
            {
                object instance = field.GetValue(null);
                var mParam = new object[] { node, treeTag };

                method.Invoke(instance, mParam);
            }
        }

    }

}
