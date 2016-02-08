package util;

/**
 * Created by marg27 on 01/02/16.
 */
public class WordEntry {
    protected String iri;
    protected String word;
    protected String stem;
    public WordEntry(String word, String iri, String stem){
        this.iri = iri;
        this.word = word;
        this.stem = stem;
    }

    public void setStem(String stem) { this.stem = stem; }

    public void setIri(String iri) {
        this.iri = iri;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getIri() {
        return iri;
    }

    public String getWord() {
        return word;
    }

    public String getStem() { return stem; }
}
