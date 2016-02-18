package util;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by marg27 on 01/02/16.
 */
public class WordEntry {
    protected String iri;
    protected String word;
    protected String stem;
    protected HashSet<String> annotations;
    public WordEntry(String word, String iri, String stem){
        this.iri = iri;
        this.word = word;
        this.stem = stem;
        this.annotations = null;
    }
    public WordEntry(String word, String stem){
        this.iri = null;
        this.word = word;
        this.stem = stem;
        this.annotations = null;
    }

    public void setStem(String stem) { this.stem = stem; }

    public void setIri(String iri) {
        this.iri = iri;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void addAnnotation(String annotation) {
        if ((annotation != null)&&(!annotation.isEmpty())){
            if (annotations == null) {
                annotations = new HashSet<String>();
            }
            annotations.add(annotation.toUpperCase());
        }
    }
    public String getIri() {
        return iri;
    }

    public String getWord() { return word; }

    public Set<String> getAnnotations(){ return annotations; }

    public String getStem() { return stem; }
}
