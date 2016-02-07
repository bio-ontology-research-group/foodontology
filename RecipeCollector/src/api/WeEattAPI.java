package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by marg27 on 10/01/16.
 */
public class WeEattAPI extends Collector{
    private String apiKey="";
    //http://www.weeatt.com/
    public WeEattAPI(String outPath){
        super(outPath);
    }
    public void collectRecipes(){

    }
}
