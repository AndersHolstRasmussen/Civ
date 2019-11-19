package hotciv.broker.invokers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import frds.broker.ReplyObject;
import hotciv.broker.OperationNames;
import hotciv.framework.Game;
import hotciv.framework.GameObserver;
import hotciv.framework.NullObserver;
import hotciv.framework.Position;

public class GameInvoker implements frds.broker.Invoker{
    private Game servant;

    public GameInvoker(Game servant) {
        this.servant = servant;
    }

    public ReplyObject handleRequest(String objectId, String operationName, String payload) {
        ReplyObject reply = new ReplyObject(0, "");
        Gson gson = new Gson();

        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(payload).getAsJsonArray();
        Position pos;
        try {
            switch (operationName) {
                case OperationNames.addObserver:
                    GameObserver observer = gson.fromJson(array.get(0), NullObserver.class);
                    servant.addObserver(observer);
                    reply = new ReplyObject(0, "");
                    break;
                case OperationNames.changeProductionInCityAt:
                    pos = gson.fromJson(array.get(0), Position.class);
                    String unitType = gson.fromJson(array.get(1), String.class);
                    servant.changeProductionInCityAt(pos, unitType);
                    reply = new ReplyObject(0, "");
                    break;
                case OperationNames.changeWorkForceFocusInCityAt:
                    pos = gson.fromJson(array.get(0), Position.class);
                    String balance = gson.fromJson(array.get(1), String.class);
                    servant.changeWorkForceFocusInCityAt(pos, balance);
                    reply = new ReplyObject(0, "");
                    break;
                case OperationNames.endOfTurn:
                    servant.endOfTurn();
                    reply = new ReplyObject(0, "");
                    break;
                case OperationNames.getAge:
                    reply = new ReplyObject(0, gson.toJson(servant.getAge()));
                    break;
                case OperationNames.getCityAt:
                    pos = gson.fromJson(array.get(0), Position.class);
                    reply = new ReplyObject(0, gson.toJson(servant.getCityAt(pos)));
                    break;
                case OperationNames.getPlayerInTurn:
                    reply = new ReplyObject(0, gson.toJson(servant.getPlayerInTurn()));
                    break;
                case OperationNames.getTileAt:
                    pos = gson.fromJson(array.get(0), Position.class);
                    reply = new ReplyObject(0, gson.toJson(servant.getTileAt(pos)));
                    break;
                case OperationNames.getUnitAt:
                    pos = gson.fromJson(array.get(0), Position.class);
                    reply = new ReplyObject(0, gson.toJson(servant.getUnitAt(pos)));
                    break;
                case OperationNames.getWinner:
                    reply = new ReplyObject(0, gson.toJson(servant.getWinner()));
                    break;
                case OperationNames.moveUnit:
                    Position from = gson.fromJson(array.get(0), Position.class);
                    Position to = gson.fromJson(array.get(1), Position.class);
                    reply = new ReplyObject(0, gson.toJson(servant.moveUnit(from, to)));
                    break;
                case OperationNames.performUnitActionAt:
                    pos = gson.fromJson(array.get(0), Position.class);
                    servant.performUnitActionAt(pos);
                    reply = new ReplyObject(0, "");
                    break;
                case OperationNames.setTileFocus:
                    pos = gson.fromJson(array.get(0), Position.class);
                    servant.setTileFocus(pos);
                    reply = new ReplyObject(0, "");
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return reply;
    }
}