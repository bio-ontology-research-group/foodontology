package util;


import stemmer.EnglishStemmer;

import java.util.regex.Pattern;

public abstract class LinguisticTool {
    protected EnglishStemmer stemmer;

    public LinguisticTool(){

    }
    public String escapePattern(String word){
        if((word!=null)&&(!word.isEmpty())) {
            Pattern specialRegexChars = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");
            String wordEscaped = specialRegexChars.matcher(word).replaceAll("\\\\$0");
            return(wordEscaped);
        }
        return (word);
    }

    public String removeWhiteSpaces(String word){
        if((word!=null)&&(!word.isEmpty())){
            word = word.replaceAll("^\\s+", ""); //We remove the lefth whitespaces
            word = word.replaceAll("\\s+$", ""); //we remove the right whitespaces
        }
        return(word);
    }

    public abstract String processString(String word);
}
