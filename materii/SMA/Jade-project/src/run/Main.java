package run;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import agent.Agent;
import agent.dto.AgentDetail;
import agent.Facilitator;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;


public class Main {

  public static int agentsNr;
  public static int cyclesNr;
  public static double penalty;
  public static ArrayList<AgentDetail> agentDetails;
  public static ArrayList<ArrayList<Integer>> cyclesDetails;

  public static void main(String args[]) throws Exception {

    readConfigFile(args);

    AgentContainer agentContainer = getAgentContainer();

    AgentController agent = agentContainer.createNewAgent("AF", Facilitator.class.getName(), new Object[0]);
    agent.start();

    for (int agentId = 0; agentId < agentsNr; ++agentId) {
      AgentController agent1 = agentContainer.createNewAgent("AP_" + agentId, Agent.class.getName(),
                                                             new Object[0]);
      agent1.start();
    }

  }

  private static void readConfigFile(String[] args) throws IOException {
    String configFile = "system.txt";
    if (args != null && args.length > 0) {
      configFile = args[0];
    }

    BufferedReader br = new BufferedReader(new FileReader(configFile));

    StringTokenizer tokenizer = new StringTokenizer(br.readLine(), " ");

    //First line
    agentsNr = Integer.parseInt(tokenizer.nextToken());
    cyclesNr = Integer.parseInt(tokenizer.nextToken());
    penalty = Double.parseDouble(tokenizer.nextToken());

    //Second line -- empty
    br.readLine();

    //Agents lines
    agentDetails = new ArrayList<AgentDetail>();
    for (int agentNr = 0; agentNr < agentsNr; ++agentNr) {
      tokenizer = new StringTokenizer(br.readLine(), " ");

      int budget = Integer.parseInt(tokenizer.nextToken());
      AgentDetail agentDetail = new AgentDetail(budget);

      while (tokenizer.hasMoreTokens()) {
        int capability = Integer.parseInt(tokenizer.nextToken());
        int cost = Integer.parseInt(tokenizer.nextToken());
        agentDetail.addCapability(capability, cost);
      }

      agentDetails.add(agentDetail);
    }

    //line -- empty
    br.readLine();

    //Cycle lines
    cyclesDetails = new ArrayList<ArrayList<Integer>>();
    for (int cycle = 0; cycle < cyclesNr; ++cycle) {
      tokenizer = new StringTokenizer(br.readLine(), " ");

      ArrayList<Integer> capabilities = new ArrayList<Integer>();
      while (tokenizer.hasMoreTokens()) {
        capabilities.add(Integer.parseInt(tokenizer.nextToken()));
      }

      cyclesDetails.add(capabilities);
    }

    br.close();
  }

  private static AgentContainer getAgentContainer() {
    Runtime runtime = Runtime.instance();
    ExtendedProperties mainProperties = new ExtendedProperties();
    mainProperties.setProperty(Profile.GUI, "false"); // start the JADE GUI
    mainProperties.setProperty(Profile.MAIN, "true"); // is main container
    mainProperties.setProperty(Profile.PLATFORM_ID, "platformID"); // same as main
    mainProperties.setProperty(Profile.CONTAINER_NAME, "client1"); // you name it
    ProfileImpl mainProfile = new ProfileImpl(mainProperties);
    return runtime.createMainContainer(mainProfile);
  }

}
