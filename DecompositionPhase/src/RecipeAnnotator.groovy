import groovyx.gpars.GParsPool
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.math.NumberUtils
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.cas.FSIterator
import org.apache.uima.jcas.JCas
import org.uimafit.factory.AnalysisEngineFactory
import stopwords.StopWordsLists
import uima.flavor.FlavorEntity
import uima.measurement.MeasurementEntity
import uima.ontology.AnnotationEntity
import uima.phytochemical.PhytochemicalEntity
import uima.technique.TechniqueEntity
import uima.thesaurus.ThesaurusEntity

import java.util.concurrent.ConcurrentHashMap

int recipeCounter = 0;
String separator = System.getProperty("file.separator");
String currentPath = System.getProperty("user.dir").replace("src","");
ConcurrentHashMap termFrequency = new ConcurrentHashMap();
ConcurrentHashMap conceptFrequency = new ConcurrentHashMap();

GParsPool.withPool {
    def annotationFile = new PrintWriter(new BufferedWriter(new FileWriter(currentPath+separator+"outputs"+separator+"annotation_paralell.txt")));
    AnalysisEngine engine = AnalysisEngineFactory
            .createAnalysisEngineFromPath(currentPath + separator + "resources" + separator + "annotators" + separator + "RecipeAnnotator.xml");

    File recipiesFolder = new File(currentPath+separator+"resources"+separator+"recipes");
    recipiesFolder.listFiles().eachParallel { recipeFile ->
        if (recipeFile != null) {
            recipeCounter++;
            //Ontologies
            String title = URLDecoder.decode(recipeFile.getName(), "utf-8");
            if (title.lastIndexOf('.') > -1) {
                title = title.substring(0, title.lastIndexOf('.'))
            }

            String content = IOUtils.toString(new FileReader(recipeFile));

            JCas jCas = AnalysisEngineFactory.process(engine, content);
            synchronized (jCas) {
                String annotation = "\n************************************\n";
                annotation += "************************************\n";
                annotation += "Title: " + title + "\n";
                annotation += "Content: \n" + content + "\n";
                annotation += "Annotations: \n";

                AnnotationEntity annotationEntity;
                FSIterator annotations = jCas.getAnnotationIndex(AnnotationEntity.type).iterator();
                for (int i = 0; annotations.hasNext(); i++) {
                    annotationEntity = (AnnotationEntity) annotations.next();
                    annotation += annotationEntity.word + "\t" + annotationEntity.stem + "\t" + annotationEntity.iri + "\t" + annotationEntity.begin + "\t" + annotationEntity.end + "\n";
                    if (conceptFrequency.containsKey(annotationEntity.iri)) {
                        int counter = (Integer)conceptFrequency.get(annotationEntity.iri);
                        counter++;
                        conceptFrequency.put(annotationEntity.iri, counter);
                    } else {
                        conceptFrequency.put(annotationEntity.iri, 1);
                    }
                }

                //Phytochemicals
                annotation += "\n\nPhytochemicals: \n\n";
                PhytochemicalEntity phytochemicalEntity;
                annotations = jCas.getAnnotationIndex(PhytochemicalEntity.type).iterator();
                for (int i = 0; annotations.hasNext(); i++) {
                    phytochemicalEntity = (PhytochemicalEntity) annotations.next();
                    annotation += phytochemicalEntity.getWord() + "\t" + phytochemicalEntity.getStem() + "\t" + phytochemicalEntity.getIri() + "\t" + phytochemicalEntity.getBegin() + "\t" + phytochemicalEntity.getEnd() + "\n";
                }

                //Thesaurus
                annotation += "\n\nThesaurus: \n\n";
                ThesaurusEntity thesaurusEntity;
                annotations = jCas.getAnnotationIndex(ThesaurusEntity.type).iterator();
                for (int i = 0; annotations.hasNext(); i++) {
                    thesaurusEntity = (ThesaurusEntity) annotations.next();
                    annotation += thesaurusEntity.getWord() + "\t" + thesaurusEntity.getStem() + "\t" + thesaurusEntity.getBegin() + "\t" + thesaurusEntity.getEnd() + "\n";
                }

                //Cooking flavors
                annotation += "\n\nFlavors: \n\n";
                FlavorEntity flavorEntity;
                annotations = jCas.getAnnotationIndex(FlavorEntity.type).iterator();
                for (int i = 0; annotations.hasNext(); i++) {
                    flavorEntity = (FlavorEntity) annotations.next();
                    annotation += flavorEntity.getWord() + "\t" + flavorEntity.getFlavor() + "\t" + flavorEntity.getStem() + "\t" + flavorEntity.getBegin() + "\t" + flavorEntity.getEnd() + "\n";
                }

                //Cooking methods
                annotation += "\n\nTechniques: \n\n";
                TechniqueEntity techniqueEntity;
                annotations = jCas.getAnnotationIndex(TechniqueEntity.type).iterator();
                for (int i = 0; annotations.hasNext(); i++) {
                    techniqueEntity = (TechniqueEntity) annotations.next();
                    annotation += techniqueEntity.getWord() + "\t" + techniqueEntity.getStem() + "\t" + techniqueEntity.getBegin() + "\t" + techniqueEntity.getEnd() + "\n";

                }

                //Cooking measurements
                annotation += "\n\nMeasurements: \n\n";

                MeasurementEntity measurementEntity;
                annotations = jCas.getAnnotationIndex(MeasurementEntity.type).iterator();
                for (int i = 0; annotations.hasNext(); i++) {
                    measurementEntity = (MeasurementEntity) annotations.next();
                    annotation += measurementEntity.getWord() + "\t" + measurementEntity.getStem() + "\t" + measurementEntity.getBegin() + "\t" + measurementEntity.getEnd() + "\n";

                }
                synchronized (annotationFile){
                    annotationFile.println(annotation);
                }
            }
            //Frequency analysis
            StringTokenizer tokenizer = new StringTokenizer(content);
            String token;
            while (tokenizer.hasMoreElements()) {
                token = tokenizer.nextElement();
                token = token.toLowerCase();
                if (!NumberUtils.isDigits(token)) {
                    if (!StopWordsLists.getInstance().checkWord(token)) {
                        if (termFrequency.containsKey(token)) {
                            int counter =(Integer) termFrequency.get(token);
                            counter++;
                            termFrequency.put(token,counter);
                        } else {
                            termFrequency.put(token, 1);
                        }
                    }
                }
            }
        }
    }
    annotationFile.close();
}

def termStatisticsFile = new PrintWriter(new BufferedWriter(new FileWriter(currentPath+separator+"outputs"+separator+"terms_statistics.txt")));
termStatisticsFile.println("Number of recipes:"+recipeCounter);
termFrequency = termFrequency.sort{it.value}
termFrequency.each {key,value ->
    termStatisticsFile.println(key+"\t"+value);
}

termStatisticsFile.close();


def conceptStatisticsFile = new PrintWriter(new BufferedWriter(new FileWriter(currentPath+separator+"outputs"+separator+"concepts_statistics.txt")));
conceptStatisticsFile.println("Number of recipes:"+recipeCounter);
conceptFrequency = conceptFrequency.sort{it.value}
conceptFrequency.each {key,value ->
    conceptStatisticsFile.println(key+"\t"+value);
}
conceptStatisticsFile.close();
