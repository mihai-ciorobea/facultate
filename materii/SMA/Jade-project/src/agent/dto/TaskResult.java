package agent.dto;

import java.io.Serializable;

public class TaskResult implements Serializable {
  public boolean isFinished;
  public int capability;
  public double profit;

  public TaskResult(int capability, boolean isFinished, double profit) {
    this.capability = capability;
    this.isFinished = isFinished;
    this.profit = profit;
  }
}
