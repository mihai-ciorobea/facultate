package blocksworld;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import blocksworld.Predicate.Type;

/**
 * Class representing the (a) world state in blocks world.
 *
 * @author Andrei Olaru
 */
public class Blocks {

  /**
   * One block.
   *
   * @author Andrei Olaru
   */
  public static class Block {

    /**
     * The label of the block (1 character).
     */
    char label;

    /**
     * Constructor.
     *
     * @param label - the label.
     */
    public Block(char label) {
      this.label = label;
    }

    @Override
    public String toString() {
      return new Character(label).toString();
    }

    /**
     * @return the label.
     */
    public char getLabel() {
      return label;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Block) {
        return ((Block) obj).getLabel() == label;
      }
      return false;
    }

    /**
     * Compares label to one character.
     *
     * @param c - comparison target.
     * @return <code>true</code> if equal.
     */
    public boolean equals(char c) {
      return label == c;
    }

    @Override
    public int hashCode() {
      return new Character(label).hashCode();
    }
  }

  /**
   * Character for representing empty space in the input file.
   */
  public static final char EMPTY_SPACE = '.';

  /**
   * List of all blocks in the world.
   */
  public Set<Block> allBlocks = new HashSet<>();
  /**
   * List of towers in the blocks world. Each tower is a queue where the first element is the top block.
   */
  public Map<Integer, Deque<Block>> towers = new TreeMap<>();


  public <T> List<T> intersection(List<T> list1, List<T> list2) {
    List<T> list = new ArrayList<T>();

    for (T t : list1) {
      if (list2.contains(t)) {
        list.add(t);
      }
    }

    return list;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Blocks blocks = (Blocks) o;

    List<Predicate> predicates1 = blocks.toPredicates();
    List<Predicate> predicates2 = toPredicates();

    Collections.sort(predicates1);
    Collections.sort(predicates2);

    predicates1.get(1).compareTo(predicates2.get(0));

//    return predicates1.size() > predicates2.size() ?
//      intersection(predicates1, predicates2).size() == predicates2.size() :
//      intersection(predicates1, predicates2).size() == predicates1.size() ;

    return predicates1.size() > predicates2.size() ?
           predicates1.containsAll(predicates2) : predicates2.containsAll(predicates1);
  }

  /**
   * Reads the world state from the input.
   *
   * @param input - the input.
   * @throws IOException if the input is corrupted.
   */
  public Blocks(InputStream input) throws IOException {
    try (Scanner scan = new Scanner(input)) {
      int iLevel = 0;

      Map<Integer, Map<Integer, Block>> ts = new HashMap<>();
      while (scan.hasNextLine()) {
        String line = scan.nextLine().trim();
        if (line.length() == 0) {
          if (iLevel > 0) {
            break;
          }
          continue;
        }

        Integer xc = new Integer(0);
        while (line.length() > 0) {
          Block b = new Block(line.charAt(0));
          line = line.substring(1);

          if (b.getLabel() != EMPTY_SPACE) {
            if (allBlocks.contains(b)) {
              throw new IOException("duplicate blocks not allowed.");
            }
            if (ts.containsKey(xc)) {
              if (!ts.get(xc).containsKey(new Integer(iLevel - 1))) {
                throw new IOException("space found in tower.");
              }
              ts.get(xc).put(new Integer(iLevel), b);
            } else {
              Map<Integer, Block> t = new HashMap<>();
              t.put(new Integer(iLevel), b);
              ts.put(xc, t);
            }
          }
          xc = new Integer(xc.intValue() + 1);
        }
        iLevel++;
      }
      for (Map<Integer, Block> t : ts.values()) {
        if (!t.containsKey(new Integer(iLevel - 1))) {
          throw new IOException("tower doesn't reach the table.");
        }
      }
      for (Entry<Integer, Map<Integer, Block>> entry : ts.entrySet()) {
        Deque<Block> tower = new LinkedList<>();
        for (int i = 0; i < iLevel; i++) {
          if (entry.getValue().containsKey(new Integer(i))) {
            Block b = entry.getValue().get(new Integer(i));
            tower.add(b);
            if (!allBlocks.add(b)) {
              throw new IllegalStateException("duplicate block found");
            }
          }
        }
        towers.put(entry.getKey(), tower);
      }
    }
  }

  /**
   * Picks up a block from the table. Removes the tower containing it but does not remove it from the list of all
   * blocks.
   *
   * @param block - the block to pick up (or a {@link Block} instance with the same label).
   * @return the block that was just picked up (the actual instance in the world).
   * @throws IllegalArgumentException if the block does not exist in the world.
   * @throws IllegalStateException    if the block cannot be picked up (not directly on the table or not at the top of a
   *                                  tower) or is not currently anywhere on the table..
   */
  public Block pickup(Block block) {
    if (!allBlocks.contains(block)) {
      throw new IllegalArgumentException("Block [" + block + "] does not exist in the world.");
    }
    for (Entry<Integer, Deque<Block>> entry : towers.entrySet()) {
      Deque<Block> tower = entry.getValue();
      if (tower.contains(block)) {
        if (tower.peekFirst().equals(block)) {
          if (tower.size() > 1) {
            throw new IllegalStateException("Block [" + block + "] is not on the table. Use unstack.");
          }
          Block ret = tower.pollFirst();
          towers.remove(entry.getKey());
          return ret;
        }
        throw new IllegalStateException("Block [" + block + "] is under another block.");
      }
    }
    throw new IllegalStateException("Block [" + block + "] is not currently in a tower.");
  }


  public List<Action> possiblePickups() {
    List<Action> res = new ArrayList<>();
    for (Entry<Integer, Deque<Block>> entry : towers.entrySet()) {
      Deque<Block> tower = entry.getValue();
      if (tower.size() != 1) {
        continue;
      }
      res.add(new Action(Action.ActionType.PICKUP, tower.peek()));
    }
    return res;
  }

  /**
   * Puts the block on the table, forming a new tower. The block must be one that has existed in the initial state of
   * the world.
   *
   * @param block - the block to put down.
   * @throws IllegalArgumentException if the block did not exist in the initial state.
   * @throws IllegalStateException    if the block is already in a tower.
   */
  public void putDown(Block block) {
    if (!allBlocks.contains(block)) {
      throw new IllegalArgumentException("Block [" + block + "] does not exist in the world.");
    }
    for (Deque<Block> tower : towers.values()) {
      if (tower.contains(block)) {
        throw new IllegalStateException("Block [" + block + "] is already in a tower.");
      }
    }
    // create new tower
    Deque<Block> tower = new LinkedList<>();
    tower.offerFirst(block);
    int xc = 0;
    while (true) {
      if (!towers.containsKey(new Integer(xc))) {
        towers.put(new Integer(xc), tower);
        return;
      }
      xc++;
    }
  }

  public List<Action> possiblePutDowns(Block block) {
    List<Action> res = new ArrayList<>();
    res.add(new Action(Action.ActionType.PUTDOWN, block));
    return res;
  }


  /**
   * Unstacks one block from another.
   *
   * @param toUnstack   - the block to unstack from the tower (or a {@link Block} instance with the same label).
   * @param unstackFrom - the block that is under it (or a {@link Block} instance with the same label).
   * @return the block that was unstacked (the actual instance in the world).
   * @throws IllegalArgumentException if the block does not exist in the world.
   * @throws IllegalStateException    if the block cannot be unstacked (not on top of another block or not at the top of
   *                                  a tower) or it is not over the specified block or it is not currently in a tower.
   */
  public Block unstack(Block toUnstack, Block unstackFrom) {
    if (!allBlocks.contains(toUnstack)) {
      throw new IllegalArgumentException("Block [" + toUnstack + "] does not exist in the world.");
    }
    for (Entry<Integer, Deque<Block>> entry : towers.entrySet()) {
      Deque<Block> tower = entry.getValue();
      if (tower.contains(toUnstack)) {
        if (tower.peekFirst().equals(toUnstack)) {
          if (tower.size() == 1) {
            throw new IllegalStateException("Block [" + toUnstack
                                            + "] is directly on the table. Use pickup.");
          }
          Iterator<Block> it = tower.iterator();
          it.next();
          if (!it.next().equals(unstackFrom)) {
            throw new IllegalStateException("Block [" + toUnstack + "] is is not over [" + unstackFrom
                                            + "].");
          }
          Block ret = tower.pollFirst();
          return ret;
        }
        throw new IllegalStateException("Block [" + toUnstack + "] is under another block.");
      }
    }
    throw new IllegalStateException("Block [" + toUnstack + "] is not currently in any tower.");
  }

  public List<Action> possibleUnstacks() {
    List<Action> res = new ArrayList<>();
    for (Entry<Integer, Deque<Block>> entry : towers.entrySet()) {
      Deque<Block> tower = entry.getValue();
      if (tower.size() <= 1) {
        continue;
      }
      Block over = tower.pollFirst();
      Block under = tower.peek();

      tower.push(over);

      res.add(new Action(Action.ActionType.UNSTACK, over, under));
    }
    return res;
  }


  /**
   * Stacks a block on top of another.
   *
   * @param toStack   - block to stack.
   * @param stackOver - block to stack it over.
   * @throws IllegalArgumentException if the block did not exist in the initial state.
   * @throws IllegalStateException    if the block is already in a tower or if the block to stack over is not at the top
   *                                  of a tower or not at all in a tower.
   */
  public void stack(Block toStack, Block stackOver) {
    if (!allBlocks.contains(toStack)) {
      throw new IllegalArgumentException("Block [" + toStack + "] does not exist in the world.");
    }
    for (Deque<Block> tower : towers.values()) {
      if (tower.contains(toStack)) {
        throw new IllegalStateException("Block [" + toStack + "] is already in a tower.");
      }
    }
    for (Entry<Integer, Deque<Block>> entry : towers.entrySet()) {
      Deque<Block> tower = entry.getValue();
      if (tower.contains(stackOver)) {
        if (tower.peekFirst().equals(stackOver)) {
          tower.offerFirst(toStack);
          return;
        }
        throw new IllegalStateException("Block [" + stackOver + "] is not at the top of the tower.");
      }
    }
    throw new IllegalStateException("Block [" + stackOver + "] is not currently in any tower.");
  }

  public List<Action> possibleStacks(Block block) {
    List<Action> res = new ArrayList<>();
    for (Entry<Integer, Deque<Block>> entry : towers.entrySet()) {
      Deque<Block> tower = entry.getValue();
      if (tower.size() == 0) {
        continue;
      }

      res.add(new Action(Action.ActionType.STACK, block, tower.peekFirst()));
    }
    return res;
  }


  /**
   * Checks if a block is clear.
   *
   * @param block - the block to check.
   * @return <code>true</code> if the block is clear (there is no other block on top of it).
   * @throws IllegalArgumentException if the block did not exist in the initial state.
   * @throws IllegalStateException    if the block is not in any tower.
   */
  public boolean clear(Block block) {
    if (!allBlocks.contains(block)) {
      throw new IllegalArgumentException("Block [" + block + "] does not exist in the world.");
    }
    for (Deque<Block> tower : towers.values()) {
      if (tower.contains(block)) {
        if (tower.peekFirst().equals(block)) {
          return true;
        }
        return false;
      }
    }
    throw new IllegalStateException("Block [" + block + "] is not currently in any tower.");
  }

  /**
   * Checks if a block is currently on the table or over another block.
   *
   * @param block - the block to search for.
   * @return <code>true</code> if the block is on the table; <code>false</code> if the block is over another block.
   * @throws IllegalArgumentException if the block does not exist in the world.
   * @throws IllegalStateException    if the block is not currently in any tower.
   */
  public boolean isOnTable(Block block) {
    if (!allBlocks.contains(block)) {
      throw new IllegalArgumentException("Block [" + block + "] does not exist in the world.");
    }
    for (Deque<Block> tower : towers.values()) {
      if (tower.contains(block)) {
        if (tower.peekLast().equals(block)) {
          return true;
        }
        return false;
      }
    }
    throw new IllegalStateException("Block [" + block + "] is not currently in a tower.");
  }

  /**
   * Checks if the specified block is currently anywhere in a tower.
   *
   * @param block - the block to search for.
   * @return <code>true</code> if the block is in a tower.
   * @throws IllegalArgumentException if the block was not in the initial state of the world.
   */
  public boolean contains(Block block) {
    if (!allBlocks.contains(block)) {
      throw new IllegalArgumentException("Block [" + block + "] does not exist in the world.");
    }
    for (Deque<Block> tower : towers.values()) {
      if (tower.contains(block)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the specified block is know to this world.
   *
   * @param block - the block to search for.
   * @return <code>true</code> if the block is known (existed in the initial state).
   */
  public boolean exists(Block block) {
    if (allBlocks.contains(block)) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    int maxHeight = 0;
    for (Deque<Block> tower : towers.values()) {
      if (maxHeight <= tower.size()) {
        maxHeight = tower.size();
      }
    }
    int maxX = 0;
    for (Integer xc : towers.keySet()) {
      if (maxX <= xc.intValue()) {
        maxX = xc.intValue();
      }
    }

    String ret = "\n";

    for (int y = maxHeight; y > 0; y--) {
      ret += " ";
      int x = 0;
      for (Entry<Integer, Deque<Block>> entry : towers.entrySet()) {
        int xc = entry.getKey().intValue();
        Deque<Block> tower = entry.getValue();
        for (; x < xc; x++) {
          ret += " ";
        }
        if (tower.size() >= y) {
          ret += "[" + tower.toArray()[tower.size() - y] + "]";
        } else {
          ret += "   ";
        }
        x++;
      }
      ret += "\n";
    }

    for (int i = -1; i <= (maxX + 1) * 3; i++) {
      ret += "=";
    }
    return ret;
  }

  /**
   * @return the state of the world as a set of {@link Predicate} instances.
   */
  public LinkedList<Predicate> toPredicates() {
    LinkedList<Predicate> ret = new LinkedList<>();
    for (Deque<Block> tower : towers.values()) {
      Block blockUnder = null;
      for (Iterator<Block> it = tower.descendingIterator(); it.hasNext(); ) {
        Block block = it.next();
        if (blockUnder == null) {
          ret.add(new Predicate(Type.ONTABLE, block));
        } else {
          ret.add(new Predicate(Type.ON, block, blockUnder));
        }
        blockUnder = block;
      }
      ret.add(new Predicate(Type.CLEAR, blockUnder));
    }
    return ret;
  }
}
