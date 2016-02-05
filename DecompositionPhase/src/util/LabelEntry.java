package util;

/**
 * Created by marg27 on 01/02/16.
 */
public class LabelEntry {
    protected String iri;
    protected String label;
    public LabelEntry(String label, String iri){
        this.iri = iri;
        this.label = label;
    }

    public void setIri(String iri) {
        this.iri = iri;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIri() {
        return iri;
    }

    public String getLabel() {
        return label;
    }
}
