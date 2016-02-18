package uima.thesaurus;

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

public class ThesaurusEntity_Type extends Annotation_Type {
    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {
        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (ThesaurusEntity_Type.this.useExistingInstance) {
                // Return eq fs instance if already created
                FeatureStructure fs = ThesaurusEntity_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new ThesaurusEntity(addr, ThesaurusEntity_Type.this);
                    uima.thesaurus.ThesaurusEntity_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            }
            else return new ThesaurusEntity(addr, uima.thesaurus.ThesaurusEntity_Type.this);
        }
    };

    /** @generated */
    public final static int typeIndexID = ThesaurusEntity.typeIndexID;

    /**
     * @generated
     * @modifiable
     */
    public final static boolean featOkTst = JCasRegistry.getFeatOkTst("uima.thesaurus.ThesaurusEntity");

    /** @generated */
    public final Feature casFeat_stem;


    /** @generated */
    public final int casFeatCode_stem;


    /** @generated */
    public String getStem(int addr) {
        if (featOkTst && casFeat_stem == null) jcas.throwFeatMissing("stem", "uima.thesaurus.ThesaurusEntity");
        return ll_cas.ll_getStringValue(addr, casFeatCode_stem);
    }

    /** @generated */
    public void setStem(int addr, String v) {
        if (featOkTst && casFeat_stem == null) jcas.throwFeatMissing("stem", "uima.thesaurus.ThesaurusEntity");
        ll_cas.ll_setStringValue(addr, casFeatCode_stem, v);
    }

    /** @generated */
    final Feature casFeat_word;

    /** @generated */
    final int casFeatCode_word;

    /** @generated */
    public String getIri(int addr) {
        if (featOkTst && casFeat_word == null) jcas.throwFeatMissing("word", "uima.thesaurus.ThesaurusEntity");
        return ll_cas.ll_getStringValue(addr, casFeatCode_word);
    }

    /** @generated */
    public void setIri(int addr, String v) {
        if (featOkTst && casFeat_word == null) jcas.throwFeatMissing("word", "uima.thesaurus.ThesaurusEntity");
        ll_cas.ll_setStringValue(addr, casFeatCode_word, v);
    }

    /**
     * initialize variables to correspond with Cas Type and Features
     *
     * @generated
     */
    public ThesaurusEntity_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

        casFeat_stem = jcas.getRequiredFeatureDE(casType, "stem", "uima.cas.String", featOkTst);
        casFeatCode_stem = (null == casFeat_stem) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_stem).getCode();

        casFeat_word = jcas.getRequiredFeatureDE(casType, "word", "uima.cas.String", featOkTst);
        casFeatCode_word = (null == casFeat_word) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_word).getCode();

    }
}