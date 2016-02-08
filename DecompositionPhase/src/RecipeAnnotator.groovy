import org.apache.commons.io.IOUtils
import org.apache.commons.lang.math.NumberUtils
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.cas.FSIterator
import org.apache.uima.jcas.JCas
import org.uimafit.factory.AnalysisEngineFactory
import stopwords.StopWordsLists
import uima.measurement.MeasurementEntity
import uima.ontology.AnnotationEntity
import uima.technique.TechniqueEntity

int recipeCounter = 0;
String separator = System.getProperty("file.separator");
String currentPath = System.getProperty("user.dir").replace("src","");
def termFrequency = [:];
def conceptFrequency = [:];

def annotationFile = new PrintWriter(new BufferedWriter(new FileWriter(currentPath+separator+"outputs"+separator+"annotation.txt")));

AnalysisEngine engine = AnalysisEngineFactory
        .createAnalysisEngineFromPath(currentPath+separator+"resources"+separator+"annotators"+separator+"RecipeAnnotator.xml");
File recipiesFolder = new File(currentPath+separator+"resources"+separator+"recipes");
recipiesFolder.listFiles().each{ recipeFile ->
    if(recipeFile!=null){
        recipeCounter++;
        //Ontologies
        String title = URLDecoder.decode(recipeFile.getName(),"utf-8");
        if(title.lastIndexOf('.')>-1) {
            title = title.substring(0, title.lastIndexOf('.'))
        }
        String content = IOUtils.toString(new FileReader(recipeFile));

        annotationFile.println("Title: "+title);
        annotationFile.println("Content: \n"+content);
        annotationFile.println("Annotations: \n");

        JCas jCas = AnalysisEngineFactory.process(engine, content);

        AnnotationEntity annotationEntity;
        FSIterator annotations = jCas.getAnnotationIndex(AnnotationEntity.type).iterator();
        for (int i = 0; annotations.hasNext(); i++) {
            annotationEntity = (AnnotationEntity) annotations.next();
            annotationFile.println(annotationEntity.word+"\t"+annotationEntity.stem+"\t"+annotationEntity.iri+"\t"+annotationEntity.begin+"\t"+annotationEntity.end);
            if(conceptFrequency[annotationEntity.iri] != null){
                int counter = conceptFrequency[annotationEntity.iri];
                counter++;
                conceptFrequency[annotationEntity.iri] = counter;
            }else{
                def expando = new Expando();
                expando.counter = 1;
                expando.termlist = [];
                conceptFrequency[annotationEntity.iri] = 1;
            }
        }
        //Cooking methods
        annotationFile.println("\n\nTechniques: \n");

        TechniqueEntity techniqueEntity;
        annotations = jCas.getAnnotationIndex(TechniqueEntity.type).iterator();
        for (int i = 0; annotations.hasNext(); i++) {
            techniqueEntity = (TechniqueEntity) annotations.next();
            annotationFile.println(techniqueEntity.getWord()+"\t"+techniqueEntity.getStem()+"\t"+techniqueEntity.getBegin()+"\t"+techniqueEntity.getEnd());

        }

        annotationFile.println("\n\nMeasurements: \n");

        MeasurementEntity measurementEntity;
        annotations = jCas.getAnnotationIndex(MeasurementEntity.type).iterator();
        for (int i = 0; annotations.hasNext(); i++) {
            measurementEntity = (MeasurementEntity) annotations.next();
            annotationFile.println(measurementEntity.getWord()+"\t"+measurementEntity.getStem()+"\t"+measurementEntity.getBegin()+"\t"+measurementEntity.getEnd());

        }
        annotationFile.println("\n************************************");
        annotationFile.println("************************************\n");

        //Frequency analysis
        StringTokenizer tokenizer = new StringTokenizer(content);
        String token;
        while (tokenizer.hasMoreElements()) {
            token = tokenizer.nextElement();
            token = token.toLowerCase();
            if(!NumberUtils.isDigits(token)) {
                if (!StopWordsLists.getInstance().checkWord(token)) {
                    if (termFrequency[token] != null) {
                        int counter = termFrequency[token];
                        counter++;
                        termFrequency[token] = counter;
                    } else {
                        termFrequency[token] = 1;
                    }
                }
            }
        }
    }
}

annotationFile.close();

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
