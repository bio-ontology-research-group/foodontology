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
public class Food2ForkApi extends Collector{
    private String apiKey="3b1df1d43c72cd3efec7af6cee4b2abc";
    private String searchUrl="http://food2fork.com/api/search?";
    private String getUrl="http://food2fork.com/api/get?";
    public Food2ForkApi(String outPath){
        super(outPath);
    }

    public void collectRecipes() {
        //500 per day
        //http://food2fork.com/api/search?key={API_KEY}&q=shredded%20chicken
        try {
            Random random = new Random();
            for(int recipeId=520;;recipeId++){
                String urlSearchService = getUrl+"key="+apiKey+"&rId="+recipeId;
                System.out.println("Food2Fork-->"+urlSearchService);
                URL food2forkURL = new URL(urlSearchService);
                URLConnection yc = food2forkURL.openConnection();
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
                if((json!=null)&&(json.get("recipe")!=null)) {
                    try {
                        JSONObject recipe = (JSONObject) json.get("recipe");
                        if(!recipe.isEmpty()) {
                            String title = (String) recipe.get("title");
                            JSONArray ingredients = (JSONArray) recipe.get("ingredients");
                            Iterator<String> it = ingredients.iterator();
                            File fileRecipe = new File(this.outPath + URLEncoder.encode(title, "UTF-8") + ".txt");
                            System.out.println("FOOD2FORK-->Serializing: "+fileRecipe.getName());
                            PrintWriter printWriter = new PrintWriter(fileRecipe);
                            printWriter.println(title);
                            String ingredient;
                            while (it.hasNext()) {
                                ingredient = it.next();
                                printWriter.println(ingredient);
                            }
                            printWriter.close();
                        }
                    }catch(Exception e){
                        e.getMessage();
                    }
                }
                Thread.sleep(200000+random.nextInt(500));
            }
        }catch(Exception e){

        }
    }
}
