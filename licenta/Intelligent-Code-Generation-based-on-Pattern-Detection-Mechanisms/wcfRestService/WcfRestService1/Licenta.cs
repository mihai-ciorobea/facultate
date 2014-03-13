using System;
using System.Collections.Generic;
using System.IO;
using System.ServiceModel;
using System.ServiceModel.Activation;
using System.ServiceModel.Web;
using System.Text;
using Licenta_Compilator.Analize;
using Licenta_Compilator.Common;

namespace WebService
{
    [ServiceContract]
    [AspNetCompatibilityRequirements(RequirementsMode = AspNetCompatibilityRequirementsMode.Allowed)]
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    public class Licenta
    {
        private Analize _analize;
        private string _file1;
        private string _file2;
        private string templateName = "";

        [WebGet(UriTemplate = "{id}", BodyStyle = WebMessageBodyStyle.Bare, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public Stream Get(string id)
        {
            if (_analize != null && id.ToLower().Equals("index"))
                _analize.Reset();
            string text = File.ReadAllText(@"C:\wamp\www\" + id + ".html");
            MemoryStream stream = new MemoryStream(Encoding.UTF8.GetBytes(text));

            if (WebOperationContext.Current != null)
                WebOperationContext.Current.OutgoingResponse.ContentType = "text/html";
            return stream;
        }


        /*
        [WebGet(UriTemplate = "/getSettings", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public SampleItem GetCollection()
        {
            return new SampleItem { Id = 1, StringValue = "Hello" };
        }
*/

        [WebInvoke(UriTemplate = "/setSettings",
                Method = "POST",
                RequestFormat = WebMessageFormat.Json,
                ResponseFormat = WebMessageFormat.Json)]
        public String Create(SettingsGenerate settingsGenerate)
        {
            Common.VariableNameMatching = settingsGenerate.WordCount
                                              ? VariableNameMatching.WordCount
                                              : VariableNameMatching.CharacterCount;
            Common.VariableNameSimilarity = settingsGenerate.NameSimilarity;
            Common.VariableStackSimilarity = settingsGenerate.StackSimilarity;
            List<CompareMethod> compareMethods = new List<CompareMethod>();
            if (settingsGenerate.Before) compareMethods.Add(CompareMethod.Before);
            if (settingsGenerate.Before) compareMethods.Add(CompareMethod.After);
            Common.CompareMethod = compareMethods.ToArray();

            _file1 = settingsGenerate.File1;
            _file2 = settingsGenerate.File2;

            _analize = new Analize(
                @"C:\Mihai\Licenta\Licenta Compilator\Licenta Compilator\Teste\Simple1\" + _file1,
                @"C:\Mihai\Licenta\Licenta Compilator\Licenta Compilator\Teste\Simple1\" + _file2
                );
            _analize.Start();
            templateName = "";
            return "Ok";
        }


        [WebGet(UriTemplate = "/file/{name}", BodyStyle = WebMessageBodyStyle.Bare, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public Stream GetFile(string name)
        {
            string fileName = "";
            if (name.Equals("file1"))
                fileName = _file1;
            if (name.Equals("file2"))
                fileName = _file2;

            string text;
            if (!fileName.Equals(""))
            {
                
                text = fileName + "\n" + File.ReadAllText(@"C:\Mihai\Licenta\Licenta Compilator\Licenta Compilator\Teste\Simple1\" + fileName);
            }
            else
                text = templateName + "\n" + _analize.FinalProgramText();

            MemoryStream stream = new MemoryStream(Encoding.UTF8.GetBytes(text));
            if (WebOperationContext.Current != null)
                WebOperationContext.Current.OutgoingResponse.ContentType = "text/html";
           
            return stream;
        }


        [WebGet(UriTemplate = "/save/templateName/{name}", BodyStyle = WebMessageBodyStyle.Bare, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public void SaveTemplateName(string name)
        {
            templateName = name;
        }

        [WebGet(UriTemplate = "/save/defaultProgram/{programId}", BodyStyle = WebMessageBodyStyle.Bare, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public Stream SetDefaultProgram(string programId)
        {
            
            _analize.ChangeDefaultProgram(Convert.ToInt32(programId));
            string text = templateName + "\n" + _analize.FinalProgramText();

            MemoryStream stream = new MemoryStream(Encoding.UTF8.GetBytes(text));
            if (WebOperationContext.Current != null)
                WebOperationContext.Current.OutgoingResponse.ContentType = "text/html";

            return stream;
        }

        [WebGet(UriTemplate = "/save/addLine/{lineNr}", BodyStyle = WebMessageBodyStyle.Bare, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public Stream AddExtraLines(string lineNr)
        {
            _analize.ExtraLines(Convert.ToInt32(lineNr), true);
            return GetStream();
        }

        private Stream GetStream()
        {
            string text = templateName + "\n" + _analize.FinalProgramText();

            MemoryStream stream = new MemoryStream(Encoding.UTF8.GetBytes(text));
            if (WebOperationContext.Current != null)
                WebOperationContext.Current.OutgoingResponse.ContentType = "text/html";

            return stream;
        }


        [WebGet(UriTemplate = "/save/removeLine/{lineNr}", BodyStyle = WebMessageBodyStyle.Bare, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public void RemoveExtraLines(string lineNr)
        {
            _analize.ExtraLines(Convert.ToInt32(lineNr), false);
        }

        [WebGet(UriTemplate = "/save/rename/{old}/{newVar}", BodyStyle = WebMessageBodyStyle.Bare, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public Stream RenameVar(string old, string newVar)
        {
            _analize.RenameVar(old, newVar);
            return GetStream();
        }

        [WebGet(UriTemplate = "/final/rename/{old}/{newVar}", BodyStyle = WebMessageBodyStyle.Bare, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public Stream RenameVarAfterFianl(string old, string newVar)
        {
            _analize.RenameVarAfterFinal(old, newVar);
            return GetStream();
        }


     
        [WebInvoke(UriTemplate = "/save/makeFinal",
                    Method = "POST",
                    RequestFormat = WebMessageFormat.Json,
                    ResponseFormat = WebMessageFormat.Json)]
        public string MakeFinal(string text)
        {
            _analize.MakeFinal(text);
            return "ok";
        }







        [WebGet(UriTemplate = "/final/save", BodyStyle = WebMessageBodyStyle.Bare, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public void Save()
        {
            _analize.SaveTemplate(templateName);
        }

        [WebGet(UriTemplate = "/final/setName/{name}", BodyStyle = WebMessageBodyStyle.Bare, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public void SetTemplateName(string name)
        {
            templateName = name;
            Save();
        }

        [WebGet(UriTemplate = "/final/setNameWithOutSave/{name}", BodyStyle = WebMessageBodyStyle.Bare, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public void SetNameWithOutSave(string name)
        {
            _analize = new Analize(null,null);
            templateName = name;
        }

        [WebGet(UriTemplate = "/final/getTemplateText", BodyStyle = WebMessageBodyStyle.Bare, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        public Stream GetTemplateText()
        {
            string text = templateName + "\n" + File.ReadAllText(@"C:\Mihai\Licenta\Licenta Compilator\Licenta Compilator\savedTemplate\" + templateName);
         
            MemoryStream stream = new MemoryStream(Encoding.UTF8.GetBytes(text));
            if (WebOperationContext.Current != null)
                WebOperationContext.Current.OutgoingResponse.ContentType = "text/html";

            return stream;
        }

        [WebInvoke(UriTemplate = "/final/setTemplateText/",
                    Method = "POST",
                    RequestFormat = WebMessageFormat.Json,
                    ResponseFormat = WebMessageFormat.Json)]
        public void SetTemplateText(string text)
        {
            System.IO.File.WriteAllText(@"C:\Mihai\Licenta\Licenta Compilator\Licenta Compilator\savedTemplate\" + templateName, text);
        
        }
    
    }

    public class StreamAndName
    {
        public string Name { get; set; }
        public Stream Stream { get; set; }
    }
}
