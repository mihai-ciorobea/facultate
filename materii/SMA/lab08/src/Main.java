import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

public class Main {

  public static final String MY_NAME1 = "MY_NAME1";
  private static final String MY_NAME2 = "MY_NAME2";
  private static final String MY_NAME3 = "MY_NAME3";

  public static final String NICKNAME_1 = "nickname1";
  public static final String NICKNAME_2 = "nickname2";
  public static final String NICKNAME_3 = "agentreceiver";

  public static void main(String[] args) throws StaleProxyException {
    ProfileImpl profile = getProfile(MY_NAME1, "true");
    AgentContainer container1 = Runtime.instance().createMainContainer(profile);
    container1.createNewAgent(NICKNAME_1, AgentSender.class.getName(), new Object[0]).start();

//    ProfileImpl profile2 = getProfile(MY_NAME2, "false");
//    AgentContainer container2 = jade.core.Runtime.instance().createMainContainer(profile2);
//    container2.createNewAgent(NICKNAME_2, Agent.class.getName(), new Object[0]).start();
//
    ProfileImpl profile3 = getProfile(MY_NAME3, "false");
    AgentContainer container3 = jade.core.Runtime.instance().createMainContainer(profile3);
    container1.createNewAgent(NICKNAME_3, AgentReceiver.class.getName(), new Object[0]).start();

  }

  private static ProfileImpl getProfile(String myName, String main) {
    Properties properties = new ExtendedProperties();
    properties.setProperty(Profile.GUI, "true"); // start the JADE GUI
    properties.setProperty(Profile.MAIN, main); // is main container
    properties.setProperty(Profile.CONTAINER_NAME, myName); // you name it
    return new ProfileImpl(properties);
  }

}


