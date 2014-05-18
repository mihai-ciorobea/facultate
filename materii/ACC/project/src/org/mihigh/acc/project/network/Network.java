package org.mihigh.acc.project.network;

import org.mihigh.acc.project.communicator.communication.dopt.DoptMessage;
import org.mihigh.acc.project.communicator.communication.Node;

public interface Network extends Runnable {

  void attach(Node Node);
}
