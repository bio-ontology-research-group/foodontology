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
 * Created by marg27 on 11/01/16.
 */
public class RecipePuppyAPI extends Collector {
    private String url="http://www.recipepuppy.com/api/";
    private List<String> ingredients;
    public RecipePuppyAPI(String outPath,List<String> ingredients){
        super(outPath);
        this.ingredients = ingredients;
    }

    public void collectRecipes(){
        //http://www.recipepuppy.com/api/?i=eggs
        if(this.ingredients!=null) {
            try {
                Random random = new Random();
                Iterator<String> it = ingredients.iterator();
                String ingredient;
                while (it.hasNext()) {
                    ingredient = it.next();
                    if(ingredient!=null) {
                        String urlService = url+"?i="+ingredient;
                        URL recipeURL = new URL(urlService);
                        System.out.println("RecipePuppy-->"+urlService);
                        URLConnection yc = recipeURL.openConnection();
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
                        if((json!=null)&&(json.get("results")!=null)){
                            try {
                                JSONArray recipes = (JSONArray) json.get("results");
                                Iterator<JSONObject> recipeIt = recipes.iterator();
                                JSONObject recipe;
                                while (recipeIt.hasNext()) {
                                    recipe = recipeIt.next();
                                    String title = (String) recipe.get("title");
                                    String ingredients = (String) recipe.get("ingredients");
                                    File fileRecipe = new File(this.outPath + URLEncoder.encode(title, "UTF-8") + ".txt");
                                    System.out.println("RECIPEPUPPY-->Serializing: "+fileRecipe.getName());
                                    PrintWriter printWriter = new PrintWriter(fileRecipe);
                                    printWriter.println(title);
                                    printWriter.println(ingredients);
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
