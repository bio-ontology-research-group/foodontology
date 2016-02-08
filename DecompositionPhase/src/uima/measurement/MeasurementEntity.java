package uima.measurement;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class MeasurementEntity extends Annotation {
    /**
     * @generated
     * @ordered
     */
    public final static int typeIndexID = JCasRegistry.register(MeasurementEntity.class);

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
    protected MeasurementEntity() {
    }

    /**
     * Internal - constructor used by generator
     *
     * @generated
     */
    public MeasurementEntity(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public MeasurementEntity(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public MeasurementEntity(JCas jcas, int begin, int end) {
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
        if (MeasurementEntity_Type.featOkTst && ((MeasurementEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "MeasurementEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((MeasurementEntity_Type) jcasType).casFeatCode_stem);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setStem(String v) {
        if (MeasurementEntity_Type.featOkTst && ((MeasurementEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "MeasurementEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((MeasurementEntity_Type) jcasType).casFeatCode_stem, v);
    }

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getWord() {
        if (MeasurementEntity_Type.featOkTst && ((MeasurementEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "MeasurementEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((MeasurementEntity_Type) jcasType).casFeatCode_word);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setWord(String v) {
        if (MeasurementEntity_Type.featOkTst && ((MeasurementEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "MeasurementEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((MeasurementEntity_Type) jcasType).casFeatCode_word, v);
    }
}