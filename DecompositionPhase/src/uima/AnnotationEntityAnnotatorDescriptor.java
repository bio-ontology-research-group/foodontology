package uima;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;

public class AnnotationEntityAnnotatorDescriptor {

    public static AnalysisEngineDescription createDescriptor()
            throws ResourceInitializationException {
        TypeSystemDescription typeSystemDescription = TypeSystemDescriptionFactory
                .createTypeSystemDescription();
        return AnalysisEngineFactory.createPrimitiveDescription(AnnotationEntityAnnotator.class,
                typeSystemDescription);
    }
}