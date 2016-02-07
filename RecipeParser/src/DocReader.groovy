import org.apache.commons.io.FilenameUtils
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.extractor.WordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph

def serializeRecipe = { String name, String recipe ->
    File fileRecipe = new File("../recipes/"+URLEncoder.encode(name, "UTF-8") + ".txt");
    PrintWriter printWriter = new PrintWriter(fileRecipe);
    printWriter.println(name);
    printWriter.println(recipe);
    printWriter.close();
}

def readDocFile = {File file ->

    try {
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());

        HWPFDocument doc = new HWPFDocument(fis);

        WordExtractor we = new WordExtractor(doc);

        String[] paragraphs = we.getParagraphText();

        System.out.println("Total no of paragraph "+paragraphs.length);
        StringBuilder result = new StringBuilder();
        for (String para : paragraphs) {
            result.append(para.toString());
        }
        fis.close();
        serializeRecipe(file.getName(),result.toString());
    } catch (Exception e) {
        e.printStackTrace();
    }

}
def readDocxFile = {File  file ->
    try {
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());

        XWPFDocument document = new XWPFDocument(fis);

        List<XWPFParagraph> paragraphs = document.getParagraphs();

        System.out.println("Total no of paragraph "+paragraphs.size());

        StringBuilder result = new StringBuilder();
        for (String para : paragraphs) {
            result.append(para.toString());
        }
        fis.close();
        serializeRecipe(file.getName(),result.toString());

    } catch (Exception e) {
        e.printStackTrace();
    }
}

File path = new File("../recipes_raw/doc format/");

path.listFiles().each {File recipe ->
    if(recipe.isDirectory()){
        recipe.listFiles().each { File recipe2 ->
            String ext = FilenameUtils.getExtension(recipe2.getName());
            if(ext.toUpperCase().compareTo("DOCX")==0){
                readDocxFile(recipe2);
            }else if(ext.toUpperCase().compareTo("DOC")==0){
                readDocFile(recipe2);
            }
        }
    }else{
        String ext = FilenameUtils.getExtension(recipe.getName());
        if(ext.toUpperCase().compareTo("DOCX")==0){
            readDocxFile(recipe);
        }else if(ext.toUpperCase().compareTo("DOC")==0){
            readDocFile(recipe);
        }
    }
}
