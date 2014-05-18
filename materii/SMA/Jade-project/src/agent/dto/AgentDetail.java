package agent.dto;

import java.util.HashMap;

public class AgentDetail {
  private int budget;
  private HashMap<Integer, Integer> capabilities = new HashMap<Integer, Integer>();

  public AgentDetail(int budget){
    this.budget = budget;
  }

  public HashMap<Integer, Integer> getCapabilities() {
    return capabilities;
  }

  public void addCapability(int capability, int cost) {
    capabilities.put(capability, cost);
  }


  public int getBudget() {
    return budget;
  }
}
