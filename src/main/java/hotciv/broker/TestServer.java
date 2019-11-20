package hotciv.broker;

import frds.broker.ClientRequestHandler;
import frds.broker.Requestor;
import frds.broker.marshall.json.StandardJSONRequestor;
import hotciv.broker.*;
import hotciv.framework.Player;
import hotciv.framework.Position;


public class TestServer {
    private static String ip = "localhost";
    private static Client client;

    public static void main(String[] args) {
        int counter = 0;
        client = new Client(ip, 2800);

        // Check getAge call
        int value = client.gameProxy.getAge();
        if(value == 19) counter++;
        System.out.println("Age is: " + value);

        // Check playerInTurn call
        Player player = client.gameProxy.getPlayerInTurn();
        if(player.equals(Player.GREEN)) counter++;
        System.out.println("Player in turn is: " + player);

        // Check moveUnit call and get unit at
        Position pos = new Position(2, 2);
        client.gameProxy.moveUnit(null, pos);
        boolean moved = client.gameProxy.getUnitAt(pos) != null;
        if(moved) counter++;
        System.out.println("A unit has been moved: " + moved);

        // Check getAge call
        int gameAge= client.gameProxy.getAge();
        if(gameAge == 19) counter++;
        System.out.println("Age is: " + gameAge);

        System.out.println(counter + "/4 test successful");
    }
}