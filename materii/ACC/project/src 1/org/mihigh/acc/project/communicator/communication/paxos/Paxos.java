package org.mihigh.acc.project.communicator.communication.paxos;

import java.util.ArrayList;

import org.mihigh.acc.project.application.Main;
import org.mihigh.acc.project.commons.Action;

import static org.mihigh.acc.project.communicator.communication.paxos.Message.Type.prepareRequest;

public class Paxos implements Runnable {

  private NetworkManager networkManager;
  private Node node;
  private ArrayList<ActionExtended> messageNotDelivered = new ArrayList<ActionExtended>();
  private boolean processInPending = false;


  public Paxos(NetworkManager networkManager, Node node) {
    this.networkManager = networkManager;
    this.node = node;
  }


  public int acceptorMaxN = -1;
//  public Action acceptorMaxValue = null;

  public void receiveMessage(Message receivedMessage) {

    switch (receivedMessage.type) {
      case prepareRequest:
        // as ACCEPTOR
        if (acceptorMaxN <= receivedMessage.prepareRequest.requestN) {
          PrepareResponse prepareResponse = new PrepareResponse(true, acceptorMaxN,
                                                                null,
                                                                receivedMessage.sourceId);
          networkManager.broadcastMessage(new Message(node.getId(), Message.Type.prepareResponse, prepareResponse));
          acceptorMaxN = receivedMessage.prepareRequest.requestN;
//          acceptorMaxValue = receivedMessage.prepareRequest.action;
        } else {
          PrepareResponse prepareResponse = new PrepareResponse(false, acceptorMaxN,
                                                                null,
                                                                receivedMessage.sourceId);
          networkManager.broadcastMessage(new Message(node.getId(), Message.Type.prepareResponse, prepareResponse));
        }
        break;

      case prepareResponse:
        //message not for me
        if (receivedMessage.prepareResponse.destinationId != node.getId()) {
          return;
        }

        //as PROPOSER
        //ignore the max value from prepareResponse message
        if (receivedMessage.prepareResponse.accepted == false) {
          messageNotDelivered.get(0).acceptorsFalse++;
        } else {
          messageNotDelivered.get(0).acceptorsTrue++;

        }
        break;

      case acceptRequest:
        // as ACCEPTOR
        Message message = new Message(node.getId(), Message.Type.acceptResponse,
                                      new AcceptResponse(true, receivedMessage.sourceId));
        if (receivedMessage.acceptRequest.currentProposerRequestNumber < acceptorMaxN) {
          message.acceptResponse.acceped = false;
          networkManager.broadcastMessage(message);
        }
        networkManager.broadcastMessage(message);

        break;

      case acceptResponse:

        //as Learner

        //message not for me
        if (receivedMessage.acceptResponse.destinationId != node.getId()) {
          return;
        }

        messageNotDelivered.get(0).messageState = Message.Type.acceptRequest;
        if (receivedMessage.acceptResponse.acceped == false) {
          messageNotDelivered.get(0).learnerFalse++;
        } else {
          messageNotDelivered.get(0).learnerTrue++;
        }
        break;

      case accepted:
        //Update the rest of the messages
        for (Action action : messageNotDelivered) {
          updateUndeliveredMessage(receivedMessage.action, action);
        }

        //Print the message
        if (receivedMessage.action.type == Action.Type.INSERT) {
          node.getUi().insertAsDisplay(receivedMessage.action.text, receivedMessage.action.position);
        } else {
          node.getUi().removeAsDisplay(receivedMessage.action.position);
        }

        break;
    }

  }

  private void updateUndeliveredMessage(Action receivedAction, Action myAction) {
    int receivedPosition = receivedAction.position;
    int myPosition = myAction.position;

    if (receivedPosition > myPosition) {
      return;
    }

    if (Action.Type.INSERT == receivedAction.type) {
      myAction.position++;
    } else {
      myAction.position--;
    }
  }

  synchronized public void typedMessage(Action action) {
    messageNotDelivered.add(new ActionExtended(action));
  }


  @Override
  public void run() {

    node.getUi().insertAsDisplay(node.getId()+"", 0);
    while (true) {
      try {
        Thread.sleep(300);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      Message message = node.receiveMessage(null);
      if (message != null) {
        receiveMessage(message);
      }

      if (Distinguished.isLeader(node.getId())) {
        if (!processInPending) {
          processUndeliveredMessages();
        } else {
          //Check if all Acceptors have accepted
          ActionExtended action = messageNotDelivered.get(0);

          if (action.messageState == Message.Type.prepareRequest) {
            if (action.acceptorsTrue + action.acceptorsFalse == Main.NODES_NR - 1) {
              //check if majority have accepted
              if (action.acceptorsTrue > Main.NODES_NR / 2) {
                //send accepted request
                action.messageState = Message.Type.acceptRequest;
                networkManager.broadcastMessage(new Message(node.getId(), Message.Type.acceptRequest,
                                                            new AcceptRequest(getCurrentProposerRequestNumber(),
                                                                              messageNotDelivered.get(0))));
              } else {
                //proposal denied
                action.acceptorsFalse = 0;
                action.acceptorsTrue = 0;
                action.learnerFalse = 0;
                action.learnerTrue = 0;
                processInPending = false;
              }
            }
          }

          if (action.messageState == Message.Type.acceptRequest) {
            if (action.learnerFalse + action.learnerTrue == Main.NODES_NR - 1) {
              if (action.acceptorsTrue > Main.NODES_NR / 2) {
                if (action.type == Action.Type.INSERT) {
                  node.getUi().insertAsDisplay(action.text, action.position);
                }
                if (action.type == Action.Type.DELETE) {
                  node.getUi().removeAsDisplay(action.position);
                }
                networkManager.broadcastMessage(new Message(node.getId(), Message.Type.accepted, action));
                messageNotDelivered.remove(0);
                processInPending = false;
              }

            }


          }


        }
      } else {
        //Is not leader right now
        processInPending = false;
        if (messageNotDelivered.isEmpty()) {
          continue;
        }

        ActionExtended action = messageNotDelivered.get(0);
        action.acceptorsFalse = 0;
        action.acceptorsTrue = 0;
        action.learnerFalse = 0;
        action.learnerTrue = 0;
      }
    }
  }

  public int proposerRequestNumber = 1;

  private int getCurrentProposerRequestNumber() {
    return proposerRequestNumber * Main.NODES_NR + node.getId();
  }

  private int getNextProposerRequestNumber() {
    proposerRequestNumber++;
    return getCurrentProposerRequestNumber();
  }

  private void processUndeliveredMessages() {
    if (messageNotDelivered.isEmpty()) {
      return;
    }

    processInPending = true;
    ActionExtended action = messageNotDelivered.get(0);
    action.messageState = prepareRequest;
    networkManager.broadcastMessage(new Message(node.getId(), prepareRequest,
                                                new PrepareRequest(getNextProposerRequestNumber(), action)));

  }

  private class ActionExtended extends Action {

    public int acceptorsTrue;
    public int acceptorsFalse;
    public int learnerTrue;
    public int learnerFalse;
    public Message.Type messageState = null;

    public ActionExtended(Type type, String text, int position) {
      super(type, text, position);
      this.acceptorsTrue = 0;
      this.acceptorsFalse = 0;
      this.learnerTrue = 0;
      this.learnerFalse = 0;
    }

    private ActionExtended(Action action) {
      this(action.type, action.text, action.position);
    }
  }
}




