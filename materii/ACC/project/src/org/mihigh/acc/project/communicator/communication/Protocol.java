package org.mihigh.acc.project.communicator.communication;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.network.Message;

public interface Protocol {

  void receiveMessage(Message message);

  void typedMessage(Action action);
}
