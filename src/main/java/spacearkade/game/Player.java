package spacearkade.game;

import spacearkade.engine.Component;

public class Player {
    
    public ArkadeWorld worldPointer;
    public int worldNumber;
    public int playerNumber;
    public enumStatus status = enumStatus.NOPLAY;
    public Component object;
    
}
