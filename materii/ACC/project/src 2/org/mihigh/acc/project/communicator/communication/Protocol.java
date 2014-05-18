package org.mihigh.acc.project.communicator.communication;

import org.mihigh.acc.project.commons.Action;

public interface Protocol {

  void receiveMessage(Message message);

  void typedMessage(Action action);
}
