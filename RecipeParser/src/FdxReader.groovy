File xmlRecipes = new File("../recipes_raw/fdx format");
xmlRecipes.listFiles().each { recipeFile ->
    String content = recipeFile.getText('UTF-8')
    def fdx = new XmlParser().parseText(content);
    fdx.Recipes.Recipe.each{recipe ->
        String name = recipe.attributes().Name;
        File fileRecipe = new File("../recipes/" + URLEncoder.encode(name, "UTF-8") + ".txt");
        PrintWriter printWriter = new PrintWriter(fileRecipe);
        printWriter.println(name+"\n");
        printWriter.println("Ingredients:\n");
        recipe.RecipeIngredients.RecipeIngredient.each{ingredient ->
            printWriter.println(ingredient.attributes().Ingredient);
            printWriter.println(ingredient.attributes().IngredientName);
        }
        printWriter.println("\nProcedure:\n");
        recipe.RecipeProcedures.RecipeProcedure.each{procedure ->
            assert procedure instanceof groovy.util.Node
            printWriter.println(procedure.text());
        }
        printWriter.close();
    }
}