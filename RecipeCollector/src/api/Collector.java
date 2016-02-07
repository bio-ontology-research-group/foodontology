package api;

/**
 * Created by marg27 on 11/01/16.
 */
public abstract class Collector {
    protected String outPath=null;
    public Collector(String outPath){
        this.outPath = outPath;
    }
    public abstract void collectRecipes();
}
