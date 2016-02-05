package uima;

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

public class AnnotationEntity_Type extends Annotation_Type {
    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {
        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (AnnotationEntity_Type.this.useExistingInstance) {
                // Return eq fs instance if already created
                FeatureStructure fs = AnnotationEntity_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new AnnotationEntity(addr, AnnotationEntity_Type.this);
                    AnnotationEntity_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            }
            else return new AnnotationEntity(addr, AnnotationEntity_Type.this);
        }
    };

    /** @generated */
    public final static int typeIndexID = AnnotationEntity.typeIndexID;

    /**
     * @generated
     * @modifiable
     */
    public final static boolean featOkTst = JCasRegistry.getFeatOkTst("uima.AnnotationEntity");

    /** @generated */
    final Feature casFeat_stem;


    /** @generated */
    final int casFeatCode_stem;


    /** @generated */
    public String getStem(int addr) {
        if (featOkTst && casFeat_stem == null) jcas.throwFeatMissing("stem", "uima.AnnotationEntity");
        return ll_cas.ll_getStringValue(addr, casFeatCode_stem);
    }

    /** @generated */
    public void setStem(int addr, String v) {
        if (featOkTst && casFeat_stem == null) jcas.throwFeatMissing("stem", "uima.AnnotationEntity");
        ll_cas.ll_setStringValue(addr, casFeatCode_stem, v);
    }

    /** @generated */
    final Feature casFeat_iri;

    /** @generated */
    final int casFeatCode_iri;

    /** @generated */
    public String getIri(int addr) {
        if (featOkTst && casFeat_iri == null) jcas.throwFeatMissing("iri", "uima.AnnotationEntity");
        return ll_cas.ll_getStringValue(addr, casFeatCode_iri);
    }

    /** @generated */
    public void setIri(int addr, String v) {
        if (featOkTst && casFeat_iri == null) jcas.throwFeatMissing("iri", "uima.AnnotationEntity");
        ll_cas.ll_setStringValue(addr, casFeatCode_iri, v);
    }

    /**
     * initialize variables to correspond with Cas Type and Features
     *
     * @generated
     */
    public AnnotationEntity_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

        casFeat_stem = jcas.getRequiredFeatureDE(casType, "stem", "uima.cas.String", featOkTst);
        casFeatCode_stem = (null == casFeat_stem) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_stem).getCode();

        casFeat_iri = jcas.getRequiredFeatureDE(casType, "iri", "uima.cas.String", featOkTst);
        casFeatCode_iri = (null == casFeat_iri) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_iri).getCode();

    }
}