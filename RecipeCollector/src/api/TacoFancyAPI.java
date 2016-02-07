package api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Created by marg27 on 10/01/16.
 */
public class TacoFancyAPI extends Collector{
    public String url="http://taco-randomizer.herokuapp.com/random/?full-taco=true";
    public TacoFancyAPI(String outPath){
        super(outPath);
    }
    public void collectRecipes(){
        //http://taco-randomizer.herokuapp.com/random/
        Random random = new Random();
        while(true){
            try {
                URL tacoFancy = new URL(url);
                System.out.println("TacoFancy-->"+tacoFancy);
                URLConnection yc = tacoFancy.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        yc.getInputStream()));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(result.toString());
                if((json!=null)&&(json.get("name")!=null)){
                    try {
                        String name = (String) json.get("name");
                        String recipe = (String) json.get("recipe");
                        File fileRecipe = new File(this.outPath + URLEncoder.encode(name, "UTF-8") + ".txt");
                        System.out.println("TACOFANCY-->Serializing: "+fileRecipe.getName());
                        PrintWriter printWriter = new PrintWriter(fileRecipe);
                        printWriter.println(name);
                        printWriter.println(recipe);
                        printWriter.close();
                    }catch(Exception e){
                        e.getMessage();
                    }
                }
                Thread.sleep(80000+random.nextInt(500));
            }catch(Exception e){

            }
        }

    }
}
