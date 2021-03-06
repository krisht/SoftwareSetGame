package backend;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;

class Game {

    private HashMap<Integer, User> playerList = new HashMap<>();
    private DBComm gameDB = new DBComm();
    private int gid;
    private String gameName;
    private GameBoard gameBoard = new GameBoard();

    /**
     * Constructor for Game class given a user defined gameName
     *
     * @param gameName Game name for particular game instance
     */
    Game(String gameName) {
        if (GameListing.getGames().keySet().size() != 0) {
            this.gid = Collections.max(GameListing.getGames().keySet()) + 1;
        } else this.gid = 0;
        this.gameName = gameName;
        gameBoard.initialize();
    }

    /**
     * Gets gid of this Game
     *
     * @return Integer representing game's id
     */
    int getGid() {
        return this.gid;
    }

    GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Gets game name of this Game
     *
     * @return String representing game ID
     */
    String getGameName() {
        return this.gameName;
    }

    /**
     * Gets list of players as a HashMap
     *
     * @return HashMap of Integer to user objects
     */
    HashMap<Integer, User> getPlayerList() {
        return playerList;
    }

    /**
     * Allows a user with uid to submit their selected set
     *
     * @param uid Integer representing uid
     * @param c1  Integer representing id of card 1
     * @param c2  Integer representing id of card 2
     * @param c3  Integer representing id of card 3
     * @return JSONObject containing a data regarding board, submission etc.
     */
    JSONObject userSubmits(int uid, int c1, int c2, int c3) {
        JSONObject obj = gameBoard.processSubmission(c1, c2, c3);
        if (obj.getBoolean("setCorrect")) {
            User user = findPlayer(uid);
            if (user != null)
                user.incScore();
            obj.put("uid", uid);
            obj.put("returnValue", 1);
        } else {
            obj.put("uid", uid);
            obj.put("returnValue", 0);
        }

        return obj;
    }

    /**
     * Allows user to requestCards. Doesn't allow user to
     * request more than 3 cards at a time. Doesn't allow user
     * to request cards if more than 21 cards.
     *
     * @return JSONObject with new board, replaced positions and other items
     */
    JSONObject requestCards() {
        return gameBoard.requestCards();
    }

    boolean noMoreSetsConfirm(){
        for(User user : this.getPlayerList().values())
            if(!user.getNoMoreSets()) {
                System.out.println(user.getUsername() + " says there are still sets!.");
                return false;
            }
        return true;
    }

    /**
     * Adds user with uid to game to playerList HashMap
     *
     * @param uid  Integer representing User
     * @param user User object representing User
     * @return JSONObject verifying that user was added
     */
    JSONObject addToGame(int uid, User user) {
        try {
            playerList.put(uid, user);
            user.resetScore();
            JSONObject obj = new JSONObject();
            obj.put("addUser", true);
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * Method that finds player by uid in the HashMap
     *
     * @param uid Integer reresenting User with uid
     * @return User object representing uid
     */
    private User findPlayer(int uid) {
        if (playerList.containsKey(uid))
            return playerList.get(uid);
        return null;
    }

//    /**
//     * Overrides finalize to incorporate and make
//     * sure that links to database are closed
//     *
//     * @throws Throwable Throws in case of error on closing
//     */
//    @Override
//    protected void finalize() throws Throwable {
//        gameDB.DBClose();
//        super.finalize();
//    }
}
