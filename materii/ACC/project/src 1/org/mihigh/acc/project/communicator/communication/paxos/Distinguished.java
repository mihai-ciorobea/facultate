package org.mihigh.acc.project.communicator.communication.paxos;

import java.util.Date;

import org.mihigh.acc.project.application.Main;

public class Distinguished {

  private static int timeToBeLeader = 100000;
  private static int delta = timeToBeLeader / 10;
  private static int currentId = 0;
  private static int currentDigit = -1;
  private static int lastDigit = -1;

  synchronized public static boolean isLeader(int id) {
    long time = new Date().getTime();
    if ((time % timeToBeLeader) / (delta) != currentDigit) {
      lastDigit = currentDigit;
      currentDigit = (int) ((time % timeToBeLeader) / (delta));
      currentId = (currentId + 1) % Main.NODES_NR;
    }

    if (id == currentId) {
      return true;
    }

    if (id == currentId - 1) {
      //check if delta
      if (((time - delta / 10) % timeToBeLeader) / (delta) == lastDigit) {
        return true;
      }
    }

    return false;

  }
}
