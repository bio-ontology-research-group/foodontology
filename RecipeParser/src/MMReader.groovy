File path = new File("../recipes_raw/mm format/");

def splitRecipes={File file ->
    if(file!=null){
        int indexRecipe =0;
        file.eachWithIndex { line,index ->
            if((line!=null)&&((index>=indexRecipe)&&(line.startsWith("-")))){
                //Start the recipe
                indexRecipe = index+1;
                String name = null;
                String recipe ="";
                try {
                    file.eachWithIndex { line2, index2 ->
                        if (index2 >= indexRecipe) {
                            if (line2.startsWith("-")) {//end of the recipe
                                indexRecipe = index2+1;
                                throw new Exception("Recipe read");
                            }
                            if (!line2.isEmpty()) {
                                if ((name == null) && (line2.contains("Title:"))) {
                                    name = line2.split(": ")[1];
                                    name = name.replaceAll("\\s+", "");
                                }
                                recipe += line2+"\n";
                            }
                        }
                    }
                }catch(Exception e){}
                if(name!=null){
                    File fileRecipe = new File("../recipes/" + URLEncoder.encode(name, "UTF-8") + ".txt");
                    PrintWriter printWriter = new PrintWriter(fileRecipe);
                    printWriter.println(recipe);
                    printWriter.close();
                }
            }
        }
    }
}
path.listFiles().each { File recipe ->
    if (recipe.isDirectory()) {
        recipe.listFiles().each { File recipe2 ->
            splitRecipes(recipe2);
        }
    }else{
        splitRecipes(recipe);
    }
}