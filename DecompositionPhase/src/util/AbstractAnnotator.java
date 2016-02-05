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
    public AbstractAnnotator(){
        annotationsList = new HashMap<String,String>();
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
                        strLine = AnnotatorTools.removeWhiteSpaces(strLine);
                        stem = AnnotatorTools.textStemmer(strLine);
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
            String stemmedContent = AnnotatorTools.textStemmer(content);
            stemmedContent = stemmedContent.toLowerCase();
            int begin=0;
            ArrayList<String> tokens = new ArrayList<String>();
            for (String annotation : annotationsList.keySet()){
                tokens.clear();
                StringTokenizer tokenizer = new StringTokenizer(annotation);
                StringTokenizer tokenizedContent = new StringTokenizer(stemmedContent);
                while(tokenizer.hasMoreTokens()){
                    tokens.add(tokenizer.nextToken());
                }
                while(tokenizedContent.hasMoreTokens()){
                    if(tokenizedContent.nextToken().compareTo(tokens.get(0))==0){
                        boolean flag = true;
                        for(int i=1;i<tokens.size();i++){
                            if((!tokenizedContent.hasMoreTokens())||(tokens.get(i).compareTo(tokenizedContent.nextToken())!=0)){
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            int pos = stemmedContent.indexOf(tokens.get(0),begin);
                            annotations.add(new RecipeAnnotation(annotation,annotationsList.get(annotation),pos,pos+annotation.length()));
                            begin=begin+annotation.length();
                        }
                    }
                }
            }
        }
        return(annotations);
    }
}
