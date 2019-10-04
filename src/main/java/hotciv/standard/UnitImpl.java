package hotciv.standard;
import hotciv.framework.*;

public class UnitImpl implements Unit {
    private String type;
    private Player owner;
    private int attStrength;
    private int defStrength;
    private int movement; // number of movement points for the unit
    private int moveCount; // current number of movement points for the unit
    private int cost;
    private boolean isFortified = false; // is the unit fortified

    public UnitImpl(String type, Player owner){
        this.type = type;
        this.owner = owner;
        this.attStrength = GameConstants.UNITS.toClass(type).getAttStrength();
        this.defStrength = GameConstants.UNITS.toClass(type).getDefStrength();
        this.movement = GameConstants.UNITS.toClass(type).getMovement();
        this.cost = GameConstants.UNITS.toClass(type).getCost();
        moveCount = movement;
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public int getAttackingStrength() {
        return attStrength;
    }

    @Override
    public int getDefensiveStrength() {
        return defStrength;
    }

    @Override
    public int getMoveCount() {
        return moveCount;
    }

    @Override
    public String getTypeString() {
        return type;
    }

    public void refreshMoveCount() {
        moveCount = movement;
    }

    public void setMoveCount(int i) {
        moveCount = i;
    }

    public boolean isFortified() {
        return isFortified;
    }

    public void toggleFortify() {
        if (!isFortified) {
            isFortified = true;
            defStrength = 2*defStrength;
            movement = 0;
            moveCount = 0;
        }
        else {
            isFortified = false;
            defStrength = defStrength/2;
            movement = GameConstants.UNITS.ARCHER.movement;
        }
    }

    public int getCost() {
        return cost;
    }
}