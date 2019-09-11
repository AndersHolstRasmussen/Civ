package hotciv.framework;

/**
 * Collection of constants used in HotCiv Game. Note that strings are
 * used instead of enumeration types to keep the set of valid
 * constants open to extensions by future HotCiv variants.  Enums can
 * only be changed by compile time modification.
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
public class GameConstants {
    // The size of the world is set permanently to a 16x16 grid
    public static final int WORLDSIZE = 16;
    // Valid unit types
    public static final String ARCHER = "archer";
    public static final String LEGION = "legion";
    public static final String SETTLER = "settler";
    // Valid terrain types
    public static final String PLAINS = "plains";
    public static final String OCEANS = "ocean";
    public static final String FOREST = "forest";
    public static final String HILLS = "hills";
    public static final String MOUNTAINS = "mountain";
    // Valid production balance types
    public static final String productionFocus = "hammer";
    public static final String foodFocus = "apple";
    // Civ versions
    public static final String ALPHACIV = "alphaciv";
    public static final String BETACIV = "betaciv";
    public static final String GAMMACIV = "gammaciv";
    public static final String DELTACIV = "deltaciv";

    public static abstract class UNITS {
        public String getString(){return null;};
        public int getCost(){return 0;};
        public int getDefStrength() { return 0; }
        public int getMovement() { return 0; }
        public int getAttStrength() { return 0; }

        public static UNITS toClass(String string) {
            switch (string) {
                case GameConstants.ARCHER: return new ARCHER();
                case GameConstants.LEGION: return new LEGION();
                case GameConstants.SETTLER: return new SETTLER();
            }
            return null;
        }
        public static class ARCHER extends UNITS {
            public static final String string = GameConstants.ARCHER;
            public static final int attStrength = 2;
            public static final int defStrength = 3;
            public static final int movement = 1;
            public static final int cost = 10;

            public String getString() { return string; }
            public int getCost() { return cost; }
            public int getDefStrength() { return defStrength; }
            public int getMovement() { return movement; }
            public int getAttStrength() { return attStrength; }
        }

        public static class LEGION extends UNITS {
            public static final String string = GameConstants.LEGION;
            public static final int attStrength = 4;
            public static final int defStrength = 2;
            public static final int movement = 1;
            public static final int cost = 15;

            public String getString() { return string; }
            public int getCost() { return cost; }
            public int getDefStrength() { return defStrength; }
            public int getMovement() { return movement; }
            public int getAttStrength() { return attStrength; }
        }

        public static class SETTLER extends UNITS {
            public static final String string = GameConstants.SETTLER;
            public static final int attStrength = 0;
            public static final int defStrength = 3;
            public static final int movement = 1;
            public static final int cost = 30;

            public String getString() { return string; }
            public int getCost() { return cost; }
            public int getDefStrength() { return defStrength; }
            public int getMovement() { return movement; }
            public int getAttStrength() { return attStrength; }
        }
    }
}
