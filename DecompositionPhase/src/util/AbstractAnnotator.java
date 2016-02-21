package util;

import stemmer.EnglishStemmer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by miguel on 2/3/16.
 */
public abstract class AbstractAnnotator {
    protected HashMap<String,String> annotationsList;
    protected File termsFile;
    protected AnnotatorTools annotatorTools;
    public AbstractAnnotator(){
        annotationsList = new HashMap<String,String>();
        annotatorTools = new AnnotatorTools();
    }

    protected void loadTermsFile(File termsFile){
        if((termsFile!=null)&&(termsFile.exists())) {
            this.termsFile = termsFile;
            try {
                FileInputStream fstream = new FileInputStream(termsFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String strLine = null;
                String word = null;
                String stem = null;
                while ((strLine = br.readLine()) != null) {
                    if (strLine != null) {
                        strLine = strLine.toLowerCase();
                        strLine = annotatorTools.removeWhiteSpaces(strLine);
                        stem = annotatorTools.textStemmer(strLine);
                        annotationsList.put(stem, word);
                    }
                }
                br.close();
            } catch (Exception e) {

            }
        }
    }

    public List<RecipeAnnotation> process(String content){
        ArrayList annotations = new ArrayList<RecipeAnnotation>();
        if((content!=null)&&(!content.isEmpty())) {
            //First we need to steem the content
            String stemmedContent = annotatorTools.textStemmer(content);
            String regex = "";
            for (String annotation : annotationsList.keySet()){
                String annotationEscaped = annotatorTools.escapePattern(annotation);
                regex = "\\b" + annotationEscaped + "\\b";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(stemmedContent);
                while (matcher.find()) {
                    annotations.add(new RecipeAnnotation(annotationsList.get(annotation),matcher.group(),matcher.start(),matcher.end()));
                }
            }
        }
        return(annotations);
    }
}
