package uima.flavor;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class FlavorEntity extends Annotation {
    /**
     * @generated
     * @ordered
     */
    public final static int typeIndexID = JCasRegistry.register(FlavorEntity.class);

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
    protected FlavorEntity() {
    }

    /**
     * Internal - constructor used by generator
     *
     * @generated
     */
    public FlavorEntity(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public FlavorEntity(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public FlavorEntity(JCas jcas, int begin, int end) {
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
        if (FlavorEntity_Type.featOkTst && ((FlavorEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "FlavorEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((FlavorEntity_Type) jcasType).casFeatCode_stem);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setStem(String v) {
        if (FlavorEntity_Type.featOkTst && ((FlavorEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "FlavorEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((FlavorEntity_Type) jcasType).casFeatCode_stem, v);
    }

    // *--------------*
    // * Features: stem
    /**
     * getter for word - gets
     *
     * @generated
     */
    public String getWord() {
        if (FlavorEntity_Type.featOkTst && ((FlavorEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "FlavorEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((FlavorEntity_Type) jcasType).casFeatCode_word);
    }

    /**
     * setter for word - sets
     *
     * @generated
     */
    public void setWord(String v) {
        if (FlavorEntity_Type.featOkTst && ((FlavorEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "FlavorEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((FlavorEntity_Type) jcasType).casFeatCode_word, v);
    }

    /**
     * getter for flavor - gets
     *
     * @generated
     */
    public String getFlavor() {
        if (FlavorEntity_Type.featOkTst && ((FlavorEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("flavor",
                "FlavorEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((FlavorEntity_Type) jcasType).casFeatCode_word);
    }

    /**
     * setter for flavor - sets
     *
     * @generated
     */
    public void setFlavor(String v) {
        if (FlavorEntity_Type.featOkTst && ((FlavorEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("flavor",
                "FlavorEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((FlavorEntity_Type) jcasType).casFeatCode_word, v);
    }
}