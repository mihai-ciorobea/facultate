using System;
using System.ServiceModel.Activation;
using System.Web;
using System.Web.Routing;
using WebService;

namespace WcfRestService1
{
    public class Global : HttpApplication
    {
        void Application_Start(object sender, EventArgs e)
        {
            RegisterRoutes();
        }

        private void RegisterRoutes()
        {
            // Edit the base address of Licenta by replacing the "Licenta" string below
            RouteTable.Routes.Add(new ServiceRoute("Licenta", new WebServiceHostFactory(), typeof(Licenta)));
        }
    }
}
