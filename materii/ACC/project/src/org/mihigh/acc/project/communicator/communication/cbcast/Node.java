package org.mihigh.acc.project.communicator.communication.cbcast;

import org.mihigh.acc.project.communicator.communication.Message;

public interface Node extends Runnable  {

  /**
   * Receive a message from other node
   */
  void receiveMessage(Message message);
}
