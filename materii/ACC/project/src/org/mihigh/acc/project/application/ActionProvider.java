package org.mihigh.acc.project.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.commons.ActionExtention;

public class ActionProvider {

  HashMap<String, List<ActionExtention>> actions =new HashMap<String, List<ActionExtention>>();

  public ActionProvider() {

    List<ActionExtention> actions1 = Arrays.asList(
        new ActionExtention(Action.EventType.INSERT, "T", 0, 0),
        new ActionExtention(Action.EventType.INSERT, "h", 1, 0),
        new ActionExtention(Action.EventType.INSERT, "i", 2, 0),
        new ActionExtention(Action.EventType.INSERT, "s", 3, 0),
        new ActionExtention(Action.EventType.INSERT, " ", 4, 100),
        new ActionExtention(Action.EventType.INSERT, "i", 5, 0),
        new ActionExtention(Action.EventType.INSERT, "s", 6, 0),
        new ActionExtention(Action.EventType.INSERT, " ", 7, 0),
        new ActionExtention(Action.EventType.INSERT, "X", 8, 0)
    );

    actions.put("1", actions1);
    List<ActionExtention> actions2 = Arrays.asList(
        new ActionExtention(Action.EventType.INSERT, "a", 0, 0),
        new ActionExtention(Action.EventType.INSERT, "b", 1, 1000)
    );

    actions.put("2", actions2);
  }


  public List<ActionExtention> getActions(int index) {
    List<ActionExtention> res = actions.get("" + index);

    return res == null ? new ArrayList<ActionExtention>() : res;
  }
}
