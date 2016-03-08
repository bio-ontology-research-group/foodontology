package uima.list;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class AgrovocEntity extends Annotation {
    /**
     * @generated
     * @ordered
     */
    public final static int typeIndexID = JCasRegistry.register(AgrovocEntity.class);

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
    protected AgrovocEntity() {
    }

    /**
     * Internal - constructor used by generator
     *
     * @generated
     */
    public AgrovocEntity(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public AgrovocEntity(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public AgrovocEntity(JCas jcas, int begin, int end) {
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
        if (AgrovocEntity_Type.featOkTst && ((AgrovocEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "AgrovocEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((AgrovocEntity_Type) jcasType).casFeatCode_word);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setWord(String v) {
        if (AgrovocEntity_Type.featOkTst && ((AgrovocEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "AgrovocEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((AgrovocEntity_Type) jcasType).casFeatCode_word, v);
    }

    // *--------------*
    // * Features: stem

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getStem() {
        if (AgrovocEntity_Type.featOkTst && ((AgrovocEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "AgrovocEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((AgrovocEntity_Type) jcasType).casFeatCode_stem);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setStem(String v) {
        if (AgrovocEntity_Type.featOkTst && ((AgrovocEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "AgrovocEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((AgrovocEntity_Type) jcasType).casFeatCode_stem, v);
    }

    // *--------------*
    // * Features: iri

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getIri() {
        if (AgrovocEntity_Type.featOkTst && ((AgrovocEntity_Type) jcasType).casFeat_iri == null) jcasType.jcas.throwFeatMissing("iri",
                "AgrovocEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((AgrovocEntity_Type) jcasType).casFeatCode_iri);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setIri(String v) {
        if (AgrovocEntity_Type.featOkTst && ((AgrovocEntity_Type) jcasType).casFeat_iri == null) jcasType.jcas.throwFeatMissing("iri",
                "AgrovocEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((AgrovocEntity_Type) jcasType).casFeatCode_iri, v);
    }
}