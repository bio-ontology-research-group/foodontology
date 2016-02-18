package uima.thesaurus;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;
import uima.ontology.AnnotationEntity_Type;

public class ThesaurusEntity extends Annotation {
    /**
     * @generated
     * @ordered
     */
    public final static int typeIndexID = JCasRegistry.register(ThesaurusEntity.class);

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
    protected ThesaurusEntity() {
    }

    /**
     * Internal - constructor used by generator
     *
     * @generated
     */
    public ThesaurusEntity(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public ThesaurusEntity(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public ThesaurusEntity(JCas jcas, int begin, int end) {
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
        if (ThesaurusEntity_Type.featOkTst && ((ThesaurusEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "ThesaurusEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((ThesaurusEntity_Type) jcasType).casFeatCode_stem);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setStem(String v) {
        if (ThesaurusEntity_Type.featOkTst && ((ThesaurusEntity_Type) jcasType).casFeat_stem == null) jcasType.jcas.throwFeatMissing("stem",
                "ThesaurusEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((ThesaurusEntity_Type) jcasType).casFeatCode_stem, v);
    }

    /**
     * getter for stem - gets
     *
     * @generated
     */
    public String getWord() {
        if (AnnotationEntity_Type.featOkTst && ((ThesaurusEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "ThesaurusEntity");
        return jcasType.ll_cas.ll_getStringValue(addr, ((ThesaurusEntity_Type) jcasType).casFeatCode_word);
    }

    /**
     * setter for stem - sets
     *
     * @generated
     */
    public void setWord(String v) {
        if (ThesaurusEntity_Type.featOkTst && ((ThesaurusEntity_Type) jcasType).casFeat_word == null) jcasType.jcas.throwFeatMissing("word",
                "ThesaurusEntity");
        jcasType.ll_cas.ll_setStringValue(addr, ((ThesaurusEntity_Type) jcasType).casFeatCode_word, v);
    }
}