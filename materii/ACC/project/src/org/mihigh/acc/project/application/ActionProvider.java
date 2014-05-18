package org.mihigh.acc.project.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.commons.ActionExtention;

public class ActionProvider {

  HashMap<String, List<ActionExtention>> actions =new HashMap<String, List<ActionExtention>>();
  private final List<ActionExtention> actions_prof;

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
    actions.put("0", actions2);

    actions_prof = Arrays.asList(
        new ActionExtention(Action.EventType.INSERT, "a", 0, 0),
        new ActionExtention(Action.EventType.INSERT, "n", 1, 0),
        new ActionExtention(Action.EventType.INSERT, " ", 2, 0),
        new ActionExtention(Action.EventType.INSERT, "a", 3, 0),
        new ActionExtention(Action.EventType.DELETE, "", 1, 0),
        new ActionExtention(Action.EventType.DELETE, "", 2, 0),
        new ActionExtention(Action.EventType.INSERT, "n", 1, 0),
        new ActionExtention(Action.EventType.INSERT, "a", 1, 0),
        new ActionExtention(Action.EventType.INSERT, "x", 3, 0),
        new ActionExtention(Action.EventType.DELETE, "", 4, 0),
        new ActionExtention(Action.EventType.INSERT, "a", 5, 0)
    );

  }


  public List<ActionExtention> getActions(int index, boolean usesProfInput) {

    if (usesProfInput)
      return actions_prof;

    List<ActionExtention> res = actions.get("" + index);
    return res == null ? new ArrayList<ActionExtention>() : res;
  }
}

//
//  ins('a',1)
//  ins('n',2)
//  ins(' ',4)
//  ins('a',3)
//  del(1)
//  del(2)
//  ins('n',1)
//  ins('a',1)
//  ins('x',3)
//  del(4)
//  ins('a',5)