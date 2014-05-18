package org.mihigh.acc.project.communicator.communication;

import java.util.Objects;

import org.mihigh.acc.project.commons.Action;

public interface Protocol {

  void receiveMessage(Object doptMessage);

  void typedMessage(Action action);
}
