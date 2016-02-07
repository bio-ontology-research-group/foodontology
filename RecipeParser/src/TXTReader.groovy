def hyphenSignFile={file ->
    if(file!=null) {
        int indexRecipe = 0;
        file.eachWithIndex { line, index ->
            if ((line != null) && ((index >= indexRecipe) && (line.startsWith("-")))) {
                //Start the recipe
                indexRecipe = index + 1;
                String name = null;
                String recipe = "";
                try {
                    file.eachWithIndex { line2, index2 ->
                        if (index2 >= indexRecipe) {
                            if (line2.startsWith("-")) {//end of the recipe
                                indexRecipe = index2 + 1;
                                throw new Exception("Recipe read");
                            }
                            if (!line2.isEmpty() && (!line2.startsWith("-"))) {
                                if ((name == null) && (line2.contains("Title:"))) {
                                    name = line2.split(": ")[1];
                                    name = name.replaceAll("\\s+\$", "");
                                }
                                recipe += line2+"\n";
                            }
                        }
                    }
                } catch (Exception e) {
                }
                if (name != null) {
                    File fileRecipe = new File("../recipes/" + URLEncoder.encode(name, "UTF-8") + ".txt");
                    PrintWriter printWriter = new PrintWriter(fileRecipe);
                    printWriter.println(recipe);
                    printWriter.close();
                }
            }
        }
    }
}

def atSignFile={file ->
    if(file!=null) {
        int indexRecipe = 0;
        file.eachWithIndex { line, index ->
            if ((line != null) && ((index >= indexRecipe) && (line.startsWith("@")))) {
                //Start the recipe
                indexRecipe = index + 1;
                String name = null;
                String recipe = "";
                try {
                    int titleIndex = 0
                    file.eachWithIndex { line2, index2 ->
                        if (index2 >= indexRecipe) {
                            titleIndex++;
                            if (line2.startsWith("Yield")) {//end of the recipe
                                indexRecipe = index2 + 1;
                                throw new Exception("Recipe read");
                            }
                            if (!line2.isEmpty()) {
                                if ((name == null) && (titleIndex == 2)) {
                                    name = line2.replaceAll("\\s+", "");
                                }
                                recipe += line2+"\n";
                            }
                        }
                    }
                } catch (Exception e) {
                }
                if (name != null) {
                    File fileRecipe = new File("../recipes/" + URLEncoder.encode(name, "UTF-8") + ".txt");
                    PrintWriter printWriter = new PrintWriter(fileRecipe);
                    printWriter.println(recipe);
                    printWriter.close();
                }
            }
        }
    }
}

File txtRecipes = new File("../recipes_raw/txt format");
txtRecipes.listFiles().each { recipeFile ->
    String firstLine;
    try {
        recipeFile.eachLine { line  ->
            firstLine = line;
            throw new Exception("First line catched");
        }
    }catch(Exception e){}
    if(firstLine.startsWith("-")){
        hyphenSignFile(recipeFile)
    }else if(firstLine.startsWith("@")){
        atSignFile(recipeFile)
    }
}
