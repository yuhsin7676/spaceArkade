package spacearkade.engine;

public class StaticComponent extends Component{
    
    public StaticComponent() {
        super();
        this.isStaticComponent = true;
        this.infinityMass = true;
        this.className = "StaticComponent";
        this.typeName = "StaticComponent";
    }
    
}
