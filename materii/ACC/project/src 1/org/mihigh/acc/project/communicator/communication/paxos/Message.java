package org.mihigh.acc.project.communicator.communication.paxos;

import org.mihigh.acc.project.commons.Action;

public class Message {

  PrepareRequest prepareRequest;
  PrepareResponse prepareResponse;
  AcceptRequest acceptRequest;
  AcceptResponse acceptResponse;
  Action action;

  Type type;
  int sourceId;

  public Message(int sourceId, Type type, PrepareResponse prepareResponse) {
    this.sourceId = sourceId;
    this.type = type;
    this.prepareResponse = prepareResponse;
  }

  public Message(int sourceId, Type type, PrepareRequest prepareRequest) {
    this.sourceId = sourceId;
    this.type = type;
    this.prepareRequest = prepareRequest;
  }

  public Message(int sourceId, Type type, AcceptRequest acceptRequest) {
    this.sourceId = sourceId;
    this.type = type;
    this.acceptRequest = acceptRequest;
  }

  public Message(int sourceId, Type type, AcceptResponse acceptResponse) {
    this.sourceId = sourceId;
    this.type = type;
    this.acceptResponse = acceptResponse;
  }

  public Message(int sourceId, Type type, Action action) {

    this.sourceId = sourceId;
    this.type = type;
    this.action = action;
  }

  public int getSourceId() {
    return sourceId;
  }

  public enum Type {
    prepareRequest,
    prepareResponse,
    acceptRequest,
    acceptResponse,
    accepted;
  }

  @Override
  public String toString() {
    return "Message{" +
           "prepareRequest=" + (prepareRequest != null) +
           ", prepareResponse=" + (prepareResponse != null) +
           ", acceptRequest=" + (acceptRequest != null) +
           ", acceptResponse=" + (acceptResponse != null) +
           ", type=" + type +
           ", sourceId=" + sourceId +
           '}';
  }
}

class PrepareRequest {

  final int requestN;
  final Action action;

  public PrepareRequest(int requestN, Action action) {
    this.requestN = requestN;
    this.action = action;
  }
}

class PrepareResponse {

  boolean accepted;
  int requestN;
  final Action action;
  final int destinationId;

  public PrepareResponse(boolean accepted, int requestN, Action action, int destinationId) {
    this.accepted = accepted;
    this.requestN = requestN;
    this.action = action;
    this.destinationId = destinationId;
  }
}

class AcceptRequest {

  final int currentProposerRequestNumber;
  final Action actionExtended;

  public AcceptRequest(int currentProposerRequestNumber, Action actionExtended) {

    this.currentProposerRequestNumber = currentProposerRequestNumber;
    this.actionExtended = actionExtended;
  }
}

class AcceptResponse {

  boolean acceped;
  final int destinationId;

  public AcceptResponse(boolean acceped, int destinationId) {

    this.acceped = acceped;
    this.destinationId = destinationId;
  }
}