import org.testng.xml.XMLParser

File xmlRecipes = new File("../recipes_raw/exl format");
xmlRecipes.listFiles().each { recipeFile ->
    String content = recipeFile.getText('UTF-8')
    def data = new XmlParser().parseText(content);

    data.recipe.each{recipe ->
        String name = recipe.attributes().description;
        if(name!=null) {
            File fileRecipe = new File("../recipes/" + URLEncoder.encode(name, "UTF-8") + ".txt");
            PrintWriter printWriter = new PrintWriter(fileRecipe);
            printWriter.println(name+"\n");
            printWriter.println("Ingredients:\n");
            recipe.RecipeItem.each { item ->
                printWriter.println(item.attributes().ItemName);
            }
            printWriter.println(recipe.memo);
            printWriter.close();
        }
    }
}