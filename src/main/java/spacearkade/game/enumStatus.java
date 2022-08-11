package spacearkade.game;

public enum enumStatus {
    
    NOPLAY ("noPlay"),
    WAIT ("wait"),
    PLAY ("play"),
    LOSE ("lose"),
    WIN ("win");
    
    private String title;

    enumStatus(String title) {
        this.title = title;
    }
    
}
