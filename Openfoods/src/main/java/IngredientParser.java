import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.google.common.collect.*;

public class IngredientParser {
    Properties props;

    public IngredientParser(Properties props) {
        this.props = props;
    }

    public void parseOpenFoods() throws Exception {
        String filePath = this.props.getProperty("openFoodsFile");
        Set<String> countries = Sets.newHashSet(new String[] {
            "United States",
            "United Kingdom",
            "Australia",
            "Canada",
            "New Zealand"
        });
        PrintWriter out = new PrintWriter(new BufferedWriter(
            new FileWriter("data/foods.txt"), 1073741824));
        try(BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            int c = 0;
            while((line = br.readLine()) != null) {
                String[] items = line.split("\t", -1);
                if (items.length != 159) continue;
                String foodName = items[7].trim();
                String country = items[33].trim();
                String ingredients = items[34].trim();
                String additives = items[45];
                if (countries.contains(country) && (!ingredients.equals("") || !additives.equals(""))) {
                    out.print(c + "\t" + foodName + "\t");
                    if (!ingredients.equals("")){
                        // String[] ings = ingredients.split(",|\\(|\\[");
                        // for (String ing: ings) {
                        //     ing = ing.toLowerCase().replaceAll("[^a-z0-9 ]", "").trim();
                        //     if(!ing.equals("") && !this.isNumber(ing)) {
                        //         out.println(c + "\t" + foodName + "\t" + ing);
                        //     }
                        // }
                        out.print(ingredients + "\t");
                    } else {
                        out.print(".\t");
                    }
                    if (!additives.equals("")) {
                        // String[] adds = additives.split(",|\\(|\\[|-");
                        // for (String add: adds) {
                        //     add = add.toLowerCase().replaceAll("[^a-z0-9 ]", "").trim();
                        //     if (!add.equals("") && !this.isNumber(add)) {
                        //         out.println(c + "\t" + foodName + "\t" + add);
                        //     }
                        // }
                        out.println(additives);
                    } else {
                        out.println(".");
                    }
                    c++;
                }
            }
        }
        out.close();
    }

    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }


}
