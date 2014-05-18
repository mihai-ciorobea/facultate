package org.mihigh.acc.project.communicator.communication;

public interface Node extends Runnable  {

  /**
   * Receive a message from other node
   */
  void receiveMessage(Message message);

  int getId();
}
