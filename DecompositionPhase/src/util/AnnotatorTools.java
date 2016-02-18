package util;


import stemmer.EnglishStemmer;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class AnnotatorTools {
    protected static EnglishStemmer stemmer = new EnglishStemmer();
    public static String escapePattern(String word){
        if((word!=null)&&(!word.isEmpty())) {
            Pattern specialRegexChars = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");
            String wordEscaped = specialRegexChars.matcher(word).replaceAll("\\\\$0");
            return(wordEscaped);
        }
        return (word);
    }

    public static String textStemmer(String word){
        if((word!=null)&&(!word.isEmpty())) {
            StringTokenizer tokenizer = new StringTokenizer(word);
            String token;
            String wordStemmed ="";
            while(tokenizer.hasMoreElements()) {
                token = tokenizer.nextToken();
                stemmer.setCurrent(token);
                stemmer.stem();
                wordStemmed += stemmer.getCurrent();
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

    public static String removeWhiteSpaces(String word){
        if((word!=null)&&(!word.isEmpty())){
            word = word.replaceAll("^\\s+", ""); //We remove the lefth whitespaces
            word = word.replaceAll("\\s+$", ""); //we remove the right whitespaces
        }
        return(word);
    }
}
