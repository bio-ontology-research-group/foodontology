import groovy.json.JsonSlurper
import org.apache.commons.io.FilenameUtils
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

def MXPReader = { file ->
    if(file!=null){
        int indexRecipe = 0;
        file.eachWithIndex { line, index ->
            line = line.replaceAll("\\s+", "");
            if ((line != null) && ((index >= indexRecipe) && (line.startsWith("*")))) {
                //Start the recipe
                indexRecipe = index + 1;
                String name = null;
                String recipe = "";
                try {
                    int titleIndex = 0;
                    file.eachWithIndex { line2, index2 ->
                        if (index2 >= indexRecipe) {
                            titleIndex++;
                            if (line2.contains("- - - - - - - - - - - - - - - - - - ")) {//end of the recipe
                                indexRecipe = index2 + 1;
                                throw new Exception("Recipe read");
                            }
                            if (!line2.isEmpty()) {
                                if ((name == null) && (titleIndex==2)) {
                                    name = line2.replaceAll("\\s+","");
                                }
                                recipe += line2 + "\n";
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

def XMLReader = { file ->
    if(file!=null){
        String content = file.getText('UTF-8')
        def mediaWiki = new XmlParser().parseText(content);
        mediaWiki.page.eachWithIndex { page,index ->
            assert page instanceof groovy.util.Node;
            NodeList title = (NodeList) page.get("title");
            if(title!=null) {
                String name = null;
                String recipe = "";
                if (title.text().contains("Category")) {
                    name = title.text();
                    if(name.contains(":")){
                        name = name.split(":")[1];
                    }
                    NodeList revision = (NodeList)page.get("revision");
                    String[] splitRecipe = revision.text().split("\\n");
                    splitRecipe.each { line ->
                        if(!line.contains("<")&&(!line.contains("&lt;")) && (!line.contains("["))) {
                            recipe += line + "\n";
                        }
                    }
                }
                if(name!=null){
                    File fileRecipe = new File("../recipes/" + URLEncoder.encode(name+String.valueOf(index), "UTF-8") + ".txt");
                    PrintWriter printWriter = new PrintWriter(fileRecipe);
                    printWriter.println(recipe);
                    printWriter.close();
                }
            }
        }
    }
}

def JSONReader = { file ->
    if(file!=null){
        file.eachLine { line ->
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(line.toString());
            String name = json.get("name");
            String recipe = json.get("ingredients");
            if(name.length()>20){
                name = name.substring(0,20);
            }
            File fileRecipe = new File("../recipes/" + URLEncoder.encode(name, "UTF-8") + ".txt");
            PrintWriter printWriter = new PrintWriter(fileRecipe);
            printWriter.println(name);
            printWriter.println(recipe);
            printWriter.close();
        }
    }
}

File fileDirectory = new File("../recipes_raw/different formats");
fileDirectory.listFiles().each { recipeFile ->
    String ext = FilenameUtils.getExtension(recipeFile.getName());
    if(ext.toUpperCase().compareTo("MXP")==0){
        MXPReader(recipeFile);
    }else if(ext.toUpperCase().compareTo("XML")==0){
        XMLReader(recipeFile);
    }else if(ext.toUpperCase().compareTo("JSON")==0){
        JSONReader(recipeFile);
    }
}
