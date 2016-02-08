package uima.measurement;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.tcas.Annotation_Type;

public class MeasurementEntity_Type extends Annotation_Type {
    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {
        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (MeasurementEntity_Type.this.useExistingInstance) {
                // Return eq fs instance if already created
                FeatureStructure fs = MeasurementEntity_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new MeasurementEntity(addr, MeasurementEntity_Type.this);
                    MeasurementEntity_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            }
            else return new MeasurementEntity(addr, MeasurementEntity_Type.this);
        }
    };

    /** @generated */
    public final static int typeIndexID = MeasurementEntity.typeIndexID;

    /**
     * @generated
     * @modifiable
     */
    public final static boolean featOkTst = JCasRegistry.getFeatOkTst("uima.ontology.MeasurementEntity");

    /** @generated */
    final Feature casFeat_stem;


    /** @generated */
    final int casFeatCode_stem;


    /** @generated */
    public String getStem(int addr) {
        if (featOkTst && casFeat_stem == null) jcas.throwFeatMissing("stem", "uima.ontology.MeasurementEntity");
        return ll_cas.ll_getStringValue(addr, casFeatCode_stem);
    }

    /** @generated */
    public void setStem(int addr, String v) {
        if (featOkTst && casFeat_stem == null) jcas.throwFeatMissing("stem", "uima.ontology.MeasurementEntity");
        ll_cas.ll_setStringValue(addr, casFeatCode_stem, v);
    }

    /** @generated */
    final Feature casFeat_word;

    /** @generated */
    final int casFeatCode_word;

    /** @generated */
    public String getWord(int addr) {
        if (featOkTst && casFeat_word == null) jcas.throwFeatMissing("word", "uima.ontology.MeasurementEntity");
        return ll_cas.ll_getStringValue(addr, casFeatCode_word);
    }

    /** @generated */
    public void setWord(int addr, String v) {
        if (featOkTst && casFeat_word == null) jcas.throwFeatMissing("word", "uima.ontology.MeasurementEntity");
        ll_cas.ll_setStringValue(addr, casFeatCode_word, v);
    }

    /**
     * initialize variables to correspond with Cas Type and Features
     *
     * @generated
     */
    public MeasurementEntity_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

        casFeat_stem = jcas.getRequiredFeatureDE(casType, "stem", "uima.cas.String", featOkTst);
        casFeatCode_stem = (null == casFeat_stem) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_stem).getCode();

        casFeat_word = jcas.getRequiredFeatureDE(casType, "word", "uima.cas.String", featOkTst);
        casFeatCode_word = (null == casFeat_word) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_word).getCode();

    }
}