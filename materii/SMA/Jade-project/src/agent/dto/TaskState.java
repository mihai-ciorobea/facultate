package agent.dto;

public class TaskState {

  public State state;
  public int proposalIndex;

  public TaskState(State state) {

    this.state = state;
  }

  public enum State {
    SEND_REQUEST,
    NOT_ANALYZED,
    LEFT_OVER, ACCEPTED, DECLINED;
  }
}
