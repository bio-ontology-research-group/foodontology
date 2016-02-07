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
import java.util.List;
import java.util.Random;

/**
 * Created by marg27 on 10/01/16.
 */
public class EdamamAPI extends Collector{
    String appId="82250a59";
    String appKey="5c101fd7b87949270f4a7b306882e091";
    String url="https://api.edamam.com/search?";
    private List<String> ingredients=null;
    public EdamamAPI(String outPath,List ingredients){
        super(outPath);
        this.ingredients = ingredients;
    }
    public void collectRecipes(){
        //Each 60 calls per hour
        //https://api.edamam.com/search?q=chicken&app_id=82250a59&app_key=5c101fd7b87949270f4a7b306882e091
        if(this.ingredients!=null) {
            try {
                Random random = new Random();
                Iterator<String> it = ingredients.iterator();
                String ingredient;
                while (it.hasNext()) {
                    ingredient = it.next();
                    if(ingredient!=null) {
                        String urlService = url+"q="+ingredient+"&app_id="+appId+"&app_key="+appKey;
                        URL edamamURL = new URL(urlService);
                        System.out.println("EdamamAPI-->"+urlService);
                        URLConnection yc = edamamURL.openConnection();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                yc.getInputStream()));
                        String line="";
                        StringBuilder result = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        reader.close();
                        JSONParser parser = new JSONParser();
                        JSONObject json = (JSONObject) parser.parse(result.toString());
                        if((json!=null)&&(json.get("hits")!=null)){
                            try {
                                JSONArray hits = (JSONArray) json.get("hits");
                                Iterator<JSONObject> rt = hits.iterator();

                                while (rt.hasNext()) {
                                    JSONObject recipes = rt.next();
                                    JSONObject recipe = (JSONObject) recipes.get("recipe");
                                    String label = (String) recipe.get("label");
                                    JSONArray ingredients = (JSONArray) recipe.get("ingredients");
                                    Iterator<JSONObject> ingredientsIt = ingredients.iterator();
                                    JSONObject jsonIngredient;
                                    File fileRecipe = new File(this.outPath + URLEncoder.encode(label, "UTF-8") + ".txt");
                                    System.out.println("EDAMAMAPI-->Serializing: "+fileRecipe.getName());
                                    PrintWriter printWriter = new PrintWriter(fileRecipe);
                                    printWriter.println(label);
                                    while (ingredientsIt.hasNext()) {
                                        jsonIngredient = ingredientsIt.next();
                                        printWriter.println(jsonIngredient.get("text"));
                                    }
                                    printWriter.close();

                                }
                            }catch(Exception e){
                                e.getMessage();
                            }
                        }
                        Thread.sleep(80000+random.nextInt(500));
                    }
                }
            }catch(Exception e){

            }
        }

    }
}
