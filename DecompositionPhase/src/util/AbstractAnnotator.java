package util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by miguel on 2/3/16.
 */
public abstract class AbstractAnnotator {
    protected HashMap<String,String> annotationsList;
    protected File termsFile;
    public AbstractAnnotator(){
        annotationsList = new HashMap<String,String>();
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
                begin =0;
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
                            begin = stemmedContent.indexOf(tokens.get(0),begin);
                            annotations.add(new RecipeAnnotation(annotation,annotationsList.get(annotation),begin,begin+annotation.length()));
                            begin=begin+annotation.length();
                        }
                    }
                }
            }
        }
        return(annotations);
    }
}
