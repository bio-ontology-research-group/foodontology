package uima.ontology;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class PlantEntity extends Annotation {
    /**
     * @generated
     * @ordered
     */
    public final static int typeIndexID = JCasRegistry.register(PlantEntity.class);

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
    protected PlantEntity() {
    }

    /**
     * Internal - constructor used by generator
     *
     * @generated
     */
    public PlantEntity(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public PlantEntity(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public PlantEntity(JCas jcas, int begin, int end) {
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
        if (PlantEntity_Type.featOkTst && ((PlantEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "PlantEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((PlantEntity_Type) jcasType).casFeatCode_word);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setWord(String v) {
        if (PlantEntity_Type.featOkTst && ((PlantEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "PlantEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((PlantEntity_Type) jcasType).casFeatCode_word, v);
    }

    // *--------------*
    // * Features: stem

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getStem() {
        if (PlantEntity_Type.featOkTst && ((PlantEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "PlantEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((PlantEntity_Type) jcasType).casFeatCode_stem);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setStem(String v) {
        if (PlantEntity_Type.featOkTst && ((PlantEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "PlantEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((PlantEntity_Type) jcasType).casFeatCode_stem, v);
    }

    // *--------------*
    // * Features: iri

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getIri() {
        if (PlantEntity_Type.featOkTst && ((PlantEntity_Type) jcasType).casFeat_iri == null) jcasType.jcas.throwFeatMissing("iri",
                "PlantEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((PlantEntity_Type) jcasType).casFeatCode_iri);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setIri(String v) {
        if (PlantEntity_Type.featOkTst && ((PlantEntity_Type) jcasType).casFeat_iri == null) jcasType.jcas.throwFeatMissing("iri",
                "PlantEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((PlantEntity_Type) jcasType).casFeatCode_iri, v);
    }
}