package stemmer;

import util.LinguisticTool;

import java.util.StringTokenizer;

public class StemmerAdaptor extends LinguisticTool {
    protected EnglishStemmer stemmer;

    public StemmerAdaptor(){
        stemmer = new EnglishStemmer();
    }

    public String processString(String word){
        if((word!=null)&&(!word.isEmpty())) {
            StringTokenizer tokenizer = new StringTokenizer(word);
            String token;
            String wordStemmed ="";
            while (tokenizer.hasMoreElements()) {
                token = tokenizer.nextToken();
                try {
                    stemmer.setCurrent(token);
                    stemmer.stem();
                    wordStemmed += stemmer.getCurrent();
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("%%%%TokenException:" +token);
                }
                if (tokenizer.hasMoreTokens()) {
                    wordStemmed += " ";
                }
            }

            if(wordStemmed.length()>4) {//To try filter stemmed words very shorts
                return (wordStemmed);
            }
        }
        return(word);
    }
}
