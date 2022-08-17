package spacearkade.game;

import spacearkade.engine.Component;

public class Player {
    
    public ArkadeWorld worldPointer;
    public int playerNumber;
    public EnumStatus status = EnumStatus.NOPLAY;
    public Component object;
    
}
