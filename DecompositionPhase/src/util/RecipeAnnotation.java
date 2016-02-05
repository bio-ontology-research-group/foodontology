package util;

/**
 * Created by miguel on 2/3/16.
 */
public class RecipeAnnotation {
    protected String stem;
    protected String word;
    protected int begin;
    protected int end;
    public RecipeAnnotation(String stem, String word, int begin, int end){
        this.stem =stem;
        this.word = word;
        this.begin = begin;
        this.end = end;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getStem() {
        return stem;
    }

    public String getWord() {
        return word;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }
}
