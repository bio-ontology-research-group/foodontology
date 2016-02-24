package uima.ontology;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class OpenFoodEntity extends Annotation {
    /**
     * @generated
     * @ordered
     */
    public final static int typeIndexID = JCasRegistry.register(OpenFoodEntity.class);

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
    protected OpenFoodEntity() {
    }

    /**
     * Internal - constructor used by generator
     *
     * @generated
     */
    public OpenFoodEntity(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public OpenFoodEntity(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public OpenFoodEntity(JCas jcas, int begin, int end) {
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
        if (OpenFoodEntity_Type.featOkTst && ((OpenFoodEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "OpenFoodEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((OpenFoodEntity_Type) jcasType).casFeatCode_word);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setWord(String v) {
        if (OpenFoodEntity_Type.featOkTst && ((OpenFoodEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "OpenFoodEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((OpenFoodEntity_Type) jcasType).casFeatCode_word, v);
    }

    // *--------------*
    // * Features: stem

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getStem() {
        if (OpenFoodEntity_Type.featOkTst && ((OpenFoodEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "OpenFoodEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((OpenFoodEntity_Type) jcasType).casFeatCode_stem);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setStem(String v) {
        if (OpenFoodEntity_Type.featOkTst && ((OpenFoodEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "OpenFoodEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((OpenFoodEntity_Type) jcasType).casFeatCode_stem, v);
    }

    // *--------------*
    // * Features: iri

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getIri() {
        if (OpenFoodEntity_Type.featOkTst && ((OpenFoodEntity_Type) jcasType).casFeat_iri == null) jcasType.jcas.throwFeatMissing("iri",
                "OpenFoodEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((OpenFoodEntity_Type) jcasType).casFeatCode_iri);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setIri(String v) {
        if (OpenFoodEntity_Type.featOkTst && ((OpenFoodEntity_Type) jcasType).casFeat_iri == null) jcasType.jcas.throwFeatMissing("iri",
                "OpenFoodEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((OpenFoodEntity_Type) jcasType).casFeatCode_iri, v);
    }
}