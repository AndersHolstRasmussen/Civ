package hotciv.standard;

import hotciv.framework.*;

/**
 * Skeleton implementation of HotCiv.
 * <p>
 * This source code is from the book
 * "Flexible, Reliable Software:
 * Using Patterns and Agile Development"
 * published 2010 by CRC Press.
 * Author:
 * Henrik B Christensen
 * Department of Computer Science
 * Aarhus University
 * <p>
 * Please visit http://www.baerbak.com/ for further information.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class GameImpl implements Game {
    // ---------- Initialize the world ---------- \\
    private int worldAge = -4000;
    private Player currentPlayer = Player.RED;
    private TileImpl[][] map;
    private String version;
    // ------------------------------------------ \\

    public GameImpl(String version){
        this.version = version;
        this.map = generateMap(null);
    }

    public GameImpl(String version, String[][] customMap){
        this.version = version;
        this.map = generateMap(customMap);
    }

    public Tile getTileAt(Position p) {
        return map[p.getRow()][p.getColumn()];
    }

    public Unit getUnitAt(Position p) {
        return map[p.getRow()][p.getColumn()].getUnit();
    }

    public City getCityAt(Position p) {
        return map[p.getRow()][p.getColumn()].getCity();
    }

    public Player getPlayerInTurn() {
        return currentPlayer;
    }

    public Player getWinner() {
        return worldAge == -3000 ? Player.RED : null;
    }

    public int getAge() {
        return worldAge;
    }

    public boolean moveUnit(Position from, Position to) {
        UnitImpl unit = map[from.getRow()][from.getColumn()].getUnit();

        // Check if unit exists
        if (unit == null)
            return false;

        // Checking if the unit has the right owner
        if(unit.getOwner() != currentPlayer){ return false;}

        // Check if unit has enough movement points left
        if (unit.getMoveCount() > 0)
            unit.setMoveCount(unit.getMoveCount() - 1);
        else
            return false;

        // Check if the "to" position is a valid position
        if (!validUnitPosition(to))
            return false;

        // Check friendly unit collision
        Unit toUnit = map[to.getRow()][to.getColumn()].getUnit();
        if (toUnit != null && toUnit.getOwner() == currentPlayer)
            return false;

        // Check if destination is a single tile away
        boolean valid = false;
        if (Math.abs(from.getColumn() - to.getColumn()) == 1) {
            if (Math.abs(from.getRow() - to.getRow()) <= 1)
                valid = true;
        }
        if (Math.abs(from.getRow() - to.getRow()) == 1) {
            if (Math.abs(from.getColumn() - to.getColumn()) == 0)
                valid = true;
        }
        if (!valid)
                return false;

        // ANOTHER VERSION OF CALCULATING THE LENGTH OF MOVEMENT //
        // The unit has moved more than one tile
        //double d = Math.sqrt(java.lang.Double.sum(Math.pow(to.getRow()-from.getRow(), 2) , Math.pow(to.getColumn()-from.getColumn(), 2)));
        //if (d > 2){ return false;}

        map[to.getRow()][to.getColumn()].setUnit(unit); // replaces unit on to
        map[from.getRow()][from.getColumn()].setUnit(null); // removes unit on from
        return true;
    }

    public void endOfTurn() {
        if (currentPlayer == Player.RED)
            currentPlayer = Player.BLUE;
        else { // resolve end-of-turn stuff and begin the next turn
            endOfRound();
            currentPlayer = Player.RED;
        }
    }

    private Position getNearestAvailableTile(Position pos) {
        Position[] posList = new Position[9];
        // positions start at the right and runs clockwise
        posList[0] = pos;
        posList[1] = new Position(pos.getRow() - 1, pos.getColumn()); // north
        posList[2] = new Position(pos.getRow() - 1, pos.getColumn() + 1); // northeast
        posList[3] = new Position(pos.getRow(), pos.getColumn() + 1); // east
        posList[4] = new Position(pos.getRow() + 1, pos.getColumn() + 1); // southeast
        posList[5] = new Position(pos.getRow() + 1, pos.getColumn()); // south
        posList[6] = new Position(pos.getRow() + 1, pos.getColumn() - 1); // southwest
        posList[7] = new Position(pos.getRow(), pos.getColumn() - 1); // west
        posList[8] = new Position(pos.getRow() - 1, pos.getColumn() - 1); // northwest

        for (int i = 0; i < 9; i++) {
            if (validUnitPosition(posList[i]) && (getUnitAt(posList[i]) == null))
                return posList[i];
        }
        return null;
    }

    /**
     * Checks if the given position is within the world border, and if a city or unit can be placed on it
     * @param pos the parameter to be checked
     * @return true if valid
     */
    private Boolean validUnitPosition(Position pos) {
        // Check for null-position
        if(pos == null)
            return false;

        // check for out-of-bounds
        if (pos.getColumn() < 0 || GameConstants.WORLDSIZE < pos.getColumn())
            return false;
        if (pos.getRow() < 0 || GameConstants.WORLDSIZE < pos.getRow())
            return false;

        // check for mountains and ocean
        if (getTileAt(pos).getTypeString().equals(GameConstants.MOUNTAINS) || getTileAt(pos).getTypeString().equals(GameConstants.OCEANS))
            return false;

        return true;
    }

    /**
     * Resolves everything that happens at the end of every player's turn
     */
    private void endOfRound(){
        changeWorldAge();

        // Iterating over each tile on the map
        for(int i = 0; i < GameConstants.WORLDSIZE; i++){
            for(int j = 0; j< GameConstants.WORLDSIZE; j++){
                // If the tile contains a city..
                if (getCityAt(new Position(i,j)) != null){
                    CityImpl city = ((CityImpl) getCityAt(new Position(i,j)));
                    city.addProductionValue(6); // add production

                    // check if it can produce a unit
                    if (city.getProductionValue() >= city.getProductionCost()) {
                        if (setUnitAt(getNearestAvailableTile(new Position(i, j)), new UnitImpl(city.getProduction(), city.getOwner())))
                            city.addProductionValue(-city.getProductionCost());
                    }
                }
                // If the tile contains a unit..
                if (getUnitAt(new Position(i, j)) != null){
                    UnitImpl unit = ((UnitImpl) getUnitAt(new Position(i, j)));
                    unit.refreshMoveCount(); // refresh its movement
                }
            }
        }
    }

    public void changeWorldAge() {
        switch (version) {
            case GameConstants.ALPHACIV:
            case GameConstants.GAMMACIV:
            case GameConstants.DELTACIV: {
                worldAge += 100;
                break;
            }
            case GameConstants.BETACIV:
                if(worldAge < -100){worldAge += 100;} // While the world is younger than 100BC, the world increments by 100 years
                else if(worldAge == -100){worldAge = -1;} // Around Christ the world goes -50, -1, 1 50
                else if(worldAge == -1){worldAge = 1;} // Around Christ
                else if(worldAge == 1){worldAge = 50;} // Around Christ
                else if(worldAge >= 50 && worldAge < 1750){worldAge += 50;} //Between 50AD and 1750AD increment by 50
                else if(worldAge >= 1750 && worldAge < 1900){worldAge += 25;} //Between 1750 and 1900 increment by 25
                else if(worldAge >= 1900 && worldAge < 1970){worldAge += 5;} //Between 1900 and 1970 increment by 5
                else if(worldAge >= 1970){worldAge += 1;} //After 1970 increment by 1
        }
    }

    public void changeWorkForceFocusInCityAt(Position p, String balance) {
    }

    public void changeProductionInCityAt(Position p, String unitType) {
    }

    public void performUnitActionAt(Position p) {
    }

    public void setTypeAt(Position pos, String type) {
        map[pos.getRow()][pos.getColumn()].setType(type);
    }

    public boolean setUnitAt(Position pos, UnitImpl unit) {
        if (!validUnitPosition(pos))
            return false;
        // check for other units
        if (getUnitAt(pos) != null)
            return false;

        map[pos.getRow()][pos.getColumn()].setUnit(unit);
        return true;
    }

    public void setCityAt(Position pos, CityImpl city) {
        map[pos.getRow()][pos.getColumn()].setCity(city);
    }

    private String tileInterpreter(char c) {
        switch (c) {
            case 'p': // plains
                return GameConstants.PLAINS;
            case 'o': // ocean
                return GameConstants.OCEANS;
            case 'm': // mountains
                return GameConstants.MOUNTAINS;
            case 'f': // forest
                return GameConstants.FOREST;
            case 'h': // hills
                return GameConstants.HILLS;
            default: // default is just plains
                return GameConstants.PLAINS;
        }
    }

    private Player playerInterpreter(char c) {
        switch (c) {
            case '1':
                return Player.RED;
            case '2':
                return Player.BLUE;
            default: // default is just red
                return Player.RED;
        }
    }

    private String unitInterpreter(char c) {
        switch (c) {
            case 'a': // archer
                return GameConstants.ARCHER;
            case 'l': // legion
                return GameConstants.LEGION;
            case 's': // settler
                return GameConstants.SETTLER;
            default: // default is an archer
                return GameConstants.ARCHER;
        }
    }

    /**
     * This method implements the basic map given at page 459.
     * @return the finished map as an array of tiles
     */
    private TileImpl[][] generateMap(String[][] customMap){
        String[][] standardMap = new String[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
        standardMap[1][0] = "o";
        standardMap[0][1] = "h";
        standardMap[2][2] = "m";
        standardMap[2][0] = "pa1";
        standardMap[3][2] = "pl2";
        standardMap[4][3] = "ps1";
        standardMap[1][1] = "pc1";
        standardMap[4][1] = "pc2";

        switch (version) {
            case GameConstants.ALPHACIV:
            case GameConstants.BETACIV:
            case GameConstants.GAMMACIV:
                return generateSpecializedMap(standardMap);
            case GameConstants.DELTACIV:
                if(map == null || (map.length != GameConstants.WORLDSIZE) || (map[0].length != GameConstants.WORLDSIZE))
                    return generateSpecializedMap(standardMap);
                return generateSpecializedMap(customMap);
            default:
                return generateSpecializedMap(standardMap);
        }
    }

    private TileImpl[][] generateSpecializedMap(String[][] arrayMap){
        TileImpl[][] map = new TileImpl[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
        for(int i = 0; i < GameConstants.WORLDSIZE; i++){
            for(int j = 0; j< GameConstants.WORLDSIZE; j++){
                String string = arrayMap[i][j];
                String type = GameConstants.PLAINS;
                CityImpl city = null;
                UnitImpl unit = null;
                if (string != null) {
                    switch (string.length()) {
                        case 1: // only a tile type is given
                            type = tileInterpreter(string.charAt(0));
                            break;
                        case 3: // a tile type and a city or unit is given
                            type = tileInterpreter(string.charAt(0));
                            if (string.charAt(1) == 'c') // if the second string is a city, create one with owner based on the third character
                                city = new CityImpl(1, 0, playerInterpreter(string.charAt(2)), GameConstants.ARCHER, null);
                            else // otherwise it is a unit
                                unit = new UnitImpl(unitInterpreter(string.charAt(1)), playerInterpreter(string.charAt(2)));
                            break;
                        case 5:
                            type = tileInterpreter(string.charAt(0));
                            city = new CityImpl(1, 0, playerInterpreter(string.charAt(2)), GameConstants.ARCHER, null);
                            unit = new UnitImpl(unitInterpreter(string.charAt(3)), playerInterpreter(string.charAt(4)));
                            break;
                        default:
                            break;
                    }
                }
                map[i][j] = new TileImpl(new Position(i,j), type, city, unit);
            }
      }
      return map;
    }
}
