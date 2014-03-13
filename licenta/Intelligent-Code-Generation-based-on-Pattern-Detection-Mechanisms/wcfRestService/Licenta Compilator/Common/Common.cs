using ICSharpCode.NRefactory.Demo.Logging;

namespace Licenta_Compilator.Common
{
    public class Common
    {
        public static readonly Logging.Type LoggingType = Logging.Type.Test;
        public static VariableNameMatching VariableNameMatching =
                                                                            VariableNameMatching.WordCount;
                                                                            //VariableNameMatching.CharacterCount;
        public static double VariableNameSimilarity = 50.0 / 100;
        public static double VariableStackSimilarity = 40.0 / 100;
        public static decimal MinimumMatchLength = 3;
        public static int Padding = 80;
        public static CompareMethod[] CompareMethod = {
                                                           Licenta_Compilator.Common.CompareMethod.Before,
                                                           Licenta_Compilator.Common.CompareMethod.After
                                                      };

        public static int UseDefaultProgram = 1;

    }

    public enum VariableNameMatching
    {
        CharacterCount,
        WordCount
    }

    public enum CompareMethod
    {
        After,
        Before
    }

}
