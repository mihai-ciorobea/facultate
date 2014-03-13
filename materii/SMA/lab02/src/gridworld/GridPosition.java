package gridworld;

import mas1.Action;
import mas1.Orientation;
import mas1.Position;
import mas1.RelativeOrientation;

public class GridPosition implements Position {
    public static enum GridOrientation implements Orientation {
        NORTH(0, 1, "^"),

        EAST(1, 0, ">"),

        SOUTH(0, -1, "v"),

        WEST(-1, 0, "<"),;

        int dx;
        int dy;
        String representation;

        private GridOrientation(int deltaX, int deltaY, String stringRepresentation) {
            dx = deltaX;
            dy = deltaY;
            representation = stringRepresentation;
        }

        public GridOrientation getNewOrientation(Action.ActionType actionType) {
            if (actionType == Action.ActionType.TURN_LEFT) {
                switch (this) {
                    case NORTH: return WEST;
                    case EAST: return NORTH;
                    case SOUTH: return EAST;
                    case WEST: return SOUTH;
                }
            }

            if (actionType == Action.ActionType.TURN_RIGH) {
                switch (this) {
                    case NORTH: return EAST;
                    case EAST: return SOUTH;
                    case SOUTH: return WEST;
                    case WEST: return NORTH;
                }
            }
            return this;
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }

        @Override
        public String toString() {
            return representation;
        }

        public int getRelativeDx(GridRelativeOrientation relative) {
            int angle = relative.getAngle();
            int straightAngle = angle / 2;
            int isHalfAngle = angle % 2;
            GridOrientation all[] = values();
            int current = 0;
            for (int i = 0; i < all.length; i++)
                if (all[i].equals(this))
                    current = i;
            int straightResult = (current + straightAngle) % 4;

            int res = all[straightResult].getDx();
            if (isHalfAngle > 0)
                res += all[(straightResult + 1) % 4].getDx();
            return res;
        }

        public int getRelativeDy(GridRelativeOrientation relative) {
            int angle = relative.getAngle();
            int straightAngle = angle / 2;
            int isHalfAngle = angle % 2;
            GridOrientation all[] = values();
            int current = 0;
            for (int i = 0; i < all.length; i++)
                if (all[i].equals(this))
                    current = i;
            int straightResult = (current + straightAngle) % 4;

            int res = all[straightResult].getDy();
            if (isHalfAngle > 0)
                res += all[(straightResult + 1) % 4].getDy();
            return res;
        }
    }

    public static enum GridRelativeOrientation implements RelativeOrientation {
        FRONT(0),

        FRONT_LEFT(7),

        FRONT_RIGHT(1),

        BACK(4),

        BACK_LEFT(5),

        BACK_RIGHT(3),

        LEFT(6),

        RIGHT(2),;

        /**
         * 0 to 7
         */
        int angle;

        /**
         * @param relativeAngle - angle relative to the front, clockwise, in increments such that 8 increments
         *                      is a full circle.
         */
        private GridRelativeOrientation(int relativeAngle) {
            angle = relativeAngle;
        }

        int getAngle() {
            return angle;
        }
    }

    int positionX;
    int positionY;

    public GridPosition(int x, int y) {
        positionX = x;
        positionY = y;
    }

    @Override
    public Position getNeighborPosition(Orientation orientation) {
        if (!(orientation instanceof GridOrientation))
            throw new IllegalArgumentException(
                    "Unable to understand provided orientation information.");
        GridOrientation or = (GridOrientation) orientation;
        switch (or) {
            case NORTH:
                return new GridPosition(positionX, positionY + 1);
            case EAST:
                return new GridPosition(positionX + 1, positionY);
            case SOUTH:
                return new GridPosition(positionX, positionY - 1);
            case WEST:
                return new GridPosition(positionX - 1, positionY);
        }
        return null;
    }

    @Override
    public Position getNeighborPosition(Orientation orientation,
                                        RelativeOrientation relativeOrientation) {
        if (!(orientation instanceof GridOrientation)
                || !(relativeOrientation instanceof GridRelativeOrientation))
            throw new IllegalArgumentException(
                    "Unable to understand provided orientation information.");
        GridOrientation or = (GridOrientation) orientation;
        GridRelativeOrientation ror = (GridRelativeOrientation) relativeOrientation;
        return new GridPosition(positionX + or.getRelativeDx(ror), positionY
                + or.getRelativeDy(ror));
    }

    @Override
    public String toString() {
        return "(" + positionX + ", " + positionY + ")";
    }

    @Override
    public boolean isNeighbor(Position neighbor) {
        if (!(neighbor instanceof GridPosition))
            throw new IllegalArgumentException("Argument is not a grid position");
        GridPosition pos = (GridPosition) neighbor;
        return (Math.abs(positionX - pos.positionX) <= 1)
                && (Math.abs(positionY - pos.positionY) <= 1);
    }

    @Override
    public boolean isNeighborStraight(Position neighbor) {
        if (!(neighbor instanceof GridPosition))
            throw new IllegalArgumentException("Argument is not a grid position");
        GridPosition pos = (GridPosition) neighbor;
        return ((Math.abs(positionX - pos.positionX) == 1) && (positionY == pos.positionY))
                || ((Math.abs(positionY - pos.positionY) == 1) && (positionX == pos.positionX));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GridPosition)
            return (positionX == ((GridPosition) obj).positionX)
                    && (positionY == ((GridPosition) obj).positionY);
        return false;
    }

    @Override
    public int hashCode() {
        return positionX + positionY;
    }

}
