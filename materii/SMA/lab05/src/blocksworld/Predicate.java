package blocksworld;

import blocksworld.Blocks.Block;

public class Predicate implements Comparable<Predicate> {

  @Override
  public int compareTo(Predicate o) {
    if (o.getType() != getType()) {
      return o.getType().size - getType().size;
    }

    int ofirst = o.getFirstArgument() == null ? 0 : o.getFirstArgument().getLabel();
    int osecond = o.getSecondArgument() == null ? 0 : o.getSecondArgument().getLabel();

    int first = getFirstArgument() == null ? 0 : getFirstArgument().getLabel();
    int second = getSecondArgument() == null ? 0 : getSecondArgument().getLabel();

    return ofirst * 1000 + osecond - (first * 1000 + second);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Predicate predicate1 = (Predicate) o;

    if (firstArg != null ? !firstArg.equals(predicate1.firstArg) : predicate1.firstArg != null) {
      return false;
    }
    if (predicate != predicate1.predicate) {
      return false;
    }
    if (secondArg != null ? !secondArg.equals(predicate1.secondArg) : predicate1.secondArg != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = predicate != null ? predicate.hashCode() : 0;
    result = 31 * result + (firstArg != null ? firstArg.hashCode() : 0);
    result = 31 * result + (secondArg != null ? secondArg.hashCode() : 0);
    return result;
  }

  public static enum Type {
    ARMEMPTY(0),

    HOLD(100),

    ON(200),

    ONTABLE(300),

    CLEAR(400);
    private int size;

    Type(int size) {

      this.size = size;
    }

  }

  Type predicate;
  Block firstArg = null;
  Block secondArg = null;

  public Predicate(Type type) {
    if (type != Type.ARMEMPTY) {
      throw new IllegalArgumentException("Predicate has more than zero arguments");
    }
    predicate = type;
  }

  public Predicate(Type type, Block argument) {
    if (type == Type.ON) {
      throw new IllegalArgumentException("Predicate ON has two arguments");
    }
    if (type == Type.ARMEMPTY) {
      throw new IllegalArgumentException("Predicate ARMEMPTY has no arguments");
    }
    predicate = type;
    firstArg = argument;
  }

  public Predicate(Type type, Block firstArgument, Block secondArgument) {
    if (type != Type.ON) {
      throw new IllegalArgumentException("Predicate has less than two arguments");
    }
    predicate = type;
    firstArg = firstArgument;
    secondArg = secondArgument;
  }

  public Type getType() {
    return predicate;
  }

  public Block getArgument() {
//    if (predicate == Type.ON || predicate == Type.ARMEMPTY) {
//      return null;
//    }
    return firstArg;
  }

  public Block getFirstArgument() {
//    if (predicate != Type.ON) {
//      return null;
//    }
    return firstArg;
  }

  public Block getSecondArgument() {
//    if (predicate != Type.ON) {
//      return null;
//    }
    return secondArg;
  }

  @Override
  public String toString() {
    String ret = predicate.toString();
    switch (predicate) {
      case ARMEMPTY:
        break;
      case CLEAR:
      case HOLD:
      case ONTABLE:
        ret += "(" + firstArg.toString() + ")";
        break;
      case ON:
        ret += "(" + firstArg.toString() + ", " + secondArg + ")";
        break;
    }
    return ret;
  }
}
