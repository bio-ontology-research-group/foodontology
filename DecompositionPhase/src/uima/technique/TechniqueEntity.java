package uima.technique;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class TechniqueEntity extends Annotation {
    /**
     * @generated
     * @ordered
     */
    public final static int typeIndexID = JCasRegistry.register(TechniqueEntity.class);

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
    protected TechniqueEntity() {
    }

    /**
     * Internal - constructor used by generator
     *
     * @generated
     */
    public TechniqueEntity(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public TechniqueEntity(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public TechniqueEntity(JCas jcas, int begin, int end) {
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
    // * Features: stem

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getStem() {
        if (TechniqueEntity_Type.featOkTst && ((TechniqueEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "TechniqueEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((TechniqueEntity_Type) jcasType).casFeatCode_stem);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setStem(String v) {
        if (TechniqueEntity_Type.featOkTst && ((TechniqueEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "TechniqueEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((TechniqueEntity_Type) jcasType).casFeatCode_stem, v);
    }

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getWord() {
        if (TechniqueEntity_Type.featOkTst && ((TechniqueEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "TechniqueEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((TechniqueEntity_Type) jcasType).casFeatCode_word);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setWord(String v) {
        if (TechniqueEntity_Type.featOkTst && ((TechniqueEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "TechniqueEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((TechniqueEntity_Type) jcasType).casFeatCode_word, v);
    }
}