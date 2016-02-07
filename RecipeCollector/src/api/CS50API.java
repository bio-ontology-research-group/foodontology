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
import java.util.Iterator;
import java.util.Random;

/**
 * Created by marg27 on 14/01/16.
 */
public class CS50API extends Collector {
    private String key="3a54f3a7697bb8af9cb45fb29e014c90";
    private String uri="http://api.cs50.net/food/3/recipes?key="+key;
    public CS50API(String outPath){
        super(outPath);
    }
    public void collectRecipes(){
        //collect 60 per hour
        //http://api.cs50.net/food/3/recipes?key=3a54f3a7697bb8af9cb45fb29e014c90&id=117003&output=json
        try{
            Random random = new Random();
            for(int id=31000;;id++){
                String urlService = uri+"&id="+String.format("%06d", id)+"&output=json";
                System.out.println("CS50-->"+urlService);
                URL cs50URL = new URL(urlService);
                URLConnection yc = cs50URL.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        yc.getInputStream()));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                JSONParser parser = new JSONParser();
                JSONArray json = (JSONArray) parser.parse(result.toString());
                if(json!=null){
                    try {
                        Iterator<JSONObject> it = json.iterator();
                        while (it.hasNext()) {
                            JSONObject recipe = it.next();
                            String name = (String) recipe.get("name");
                            String ingredients = (String) recipe.get("ingredients");
                            File fileRecipe = new File(this.outPath + URLEncoder.encode(name, "UTF-8") + ".txt");
                            System.out.println("CS50-->Serializing: "+fileRecipe.getName());
                            PrintWriter printWriter = new PrintWriter(fileRecipe);
                            printWriter.println(name);
                            printWriter.print(ingredients);
                            printWriter.close();
                        }
                    }catch(Exception e){
                        e.getMessage();
                    }
                }

                Thread.sleep(80000+random.nextInt(500));
            }
        }catch(Exception e){

        }
    }
}
