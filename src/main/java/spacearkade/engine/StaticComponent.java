package spacearkade.engine;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class StaticComponent extends Component{
    
    public StaticComponent() {
        super();
        this.isStaticComponent = true;
        this.infinityMass = true;
        this.className = "StaticComponent";
        this.typeName = "StaticComponent";
    }
    
}
