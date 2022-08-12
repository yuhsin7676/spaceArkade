package spacearkade.game;

public enum EnumStatus {
    
    NOPLAY ("noPlay"),
    WAIT ("wait"),
    PLAY ("play"),
    LOSE ("lose"),
    WIN ("win");
    
    private String title;

    EnumStatus(String title) {
        this.title = title;
    }
    
}
