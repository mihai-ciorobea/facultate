package agent.dto;

public class Alternative {

  public static final int NOT_SET = -1;
  public int agent;
  public int cost;

  public Alternative(int agent, int cost) {
    this.agent = agent;
    this.cost = cost;
  }


}
