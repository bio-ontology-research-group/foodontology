import org.testng.xml.XMLParser

File xmlRecipes = new File("../recipes_raw/mx2 format");
xmlRecipes.listFiles().each { recipeFile ->
    String content = recipeFile.getText('ISO-8859-1')
    def fdx = new XmlParser().parseText(content);
    fdx.RcpE.each{recipe ->
        assert recipe instanceof groovy.util.Node
        String name = recipe.attribute("name");
        String ingredients = recipe.text();
        File fileRecipe = new File("../recipes/" + URLEncoder.encode(name, "UTF-8") + ".txt");
        PrintWriter printWriter = new PrintWriter(fileRecipe);
        printWriter.println(name+"\n");
        printWriter.println(ingredients);
        printWriter.close();
    }
}