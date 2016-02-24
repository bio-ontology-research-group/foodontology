package uima.ontology;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class NCBIEntity extends Annotation {
    /**
     * @generated
     * @ordered
     */
    public final static int typeIndexID = JCasRegistry.register(NCBIEntity.class);

    /**
     * @generated
     * @ordered
     */
    public final static int type = typeIndexID;

    /** @generated */
    public int getTypeIndexID() {
        return typeIndexID;
    }

    /**
     * Never called. Disable default constructor
     *
     * @generated
     */
    protected NCBIEntity() {
    }

    /**
     * Internal - constructor used by generator
     *
     * @generated
     */
    public NCBIEntity(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public NCBIEntity(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public NCBIEntity(JCas jcas, int begin, int end) {
        super(jcas);
        setBegin(begin);
        setEnd(end);
        readObject();
    }

    /**
     * <!-- begin-user-doc --> Write your own initialization here <!--
     * end-user-doc -->
     *
     * @generated modifiable
     */
    private void readObject() {
    }

    // *--------------*
    // * Features: word

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getWord() {
        if (NCBIEntity_Type.featOkTst && ((NCBIEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "NCBIEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((NCBIEntity_Type) jcasType).casFeatCode_word);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setWord(String v) {
        if (NCBIEntity_Type.featOkTst && ((NCBIEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "NCBIEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((NCBIEntity_Type) jcasType).casFeatCode_word, v);
    }

    // *--------------*
    // * Features: stem

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getStem() {
        if (NCBIEntity_Type.featOkTst && ((NCBIEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "NCBIEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((NCBIEntity_Type) jcasType).casFeatCode_stem);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setStem(String v) {
        if (NCBIEntity_Type.featOkTst && ((NCBIEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "NCBIEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((NCBIEntity_Type) jcasType).casFeatCode_stem, v);
    }

    // *--------------*
    // * Features: iri

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getIri() {
        if (NCBIEntity_Type.featOkTst && ((NCBIEntity_Type) jcasType).casFeat_iri == null) jcasType.jcas.throwFeatMissing("iri",
                "NCBIEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((NCBIEntity_Type) jcasType).casFeatCode_iri);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setIri(String v) {
        if (NCBIEntity_Type.featOkTst && ((NCBIEntity_Type) jcasType).casFeat_iri == null) jcasType.jcas.throwFeatMissing("iri",
                "NCBIEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((NCBIEntity_Type) jcasType).casFeatCode_iri, v);
    }
}