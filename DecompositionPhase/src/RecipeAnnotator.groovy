import groovyx.gpars.GParsPool
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.math.NumberUtils
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.cas.FSIterator
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.Annotation
import org.uimafit.factory.AnalysisEngineFactory
import stopwords.StopWordsLists
import sun.reflect.annotation.AnnotationType
import uima.AnnotationTypes
import uima.flavor.FlavorEntity
import uima.measurement.MeasurementEntity
import uima.ontology.ChebiEntity
import uima.ontology.EnvoEntity
import uima.ontology.FoodKBEntity
import uima.ontology.NCBIEntity
import uima.ontology.OntoFoodEntity
import uima.ontology.OpenFoodEntity
import uima.ontology.PlantEntity
import uima.ontology.UberonEntity
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
    def parserFile = new PrintWriter(new BufferedWriter(new FileWriter(currentPath+separator+"outputs"+separator+"parser_paralell.txt")));
    AnalysisEngine engine = AnalysisEngineFactory
            .createAnalysisEngineFromPath(currentPath + separator + "resources" + separator + "annotators" + separator + "RecipeAnnotator.xml");

    File recipiesFolder = new File(currentPath+separator+"resources"+separator+"recipes");
    recipiesFolder.listFiles().eachWithIndexParallel { File recipeFile, int idRecipe  ->
    //recipiesFolder.listFiles().eachWithIndex { File recipeFile, int idRecipe  ->
        if (recipeFile != null) {
            recipeCounter++;
            //Ontologies
            String title = URLDecoder.decode(recipeFile.getName(), "utf-8");
            title = title.replace("\n", "").replace("\r", "").replace("\"","");
            if (title.lastIndexOf('.') > -1) {
                title = title.substring(0, title.lastIndexOf('.'))
            }

            String content = IOUtils.toString(new FileReader(recipeFile));

            JCas jCas = AnalysisEngineFactory.process(engine, content);
            synchronized (jCas) {
                HashMap<String, ArrayList<String>> parser = new HashMap<String,ArrayList<String>>()
                String annotation = "\n************************************\n";
                annotation += "************************************\n";
                annotation += "Title: " + title + "\n";
                annotation += "Content: \n" + content + "\n";
                annotation += "Annotations: \n";
                //FoodId FoodTitle FoodComponent CHEBI ENVO FOODKB NCBI ONTOFOOD OPENDFOOD PLANT UBERON PHYTOCHEMICAL MEASUREMENT
                //TECHNIQUE FLAVOUR THESAURUS

                //ontologies
                annotation += "\n\nOntologies: \n\n";

                annotations = jCas.getAnnotationIndex().iterator();
                Annotation gAnnotation;
                String iri, word;
                int pos;
                for (int i = 0; annotations.hasNext(); i++) {
                    gAnnotation = (Annotation)annotations.next();
                    iri=null;
                    word=null;
                    pos = -1;
                    switch (gAnnotation.getTypeIndexID()){
                        case ChebiEntity.type:
                            chebiEntity = (ChebiEntity) gAnnotation;
                            iri = chebiEntity.iri;
                            word = chebiEntity.word;
                            pos = AnnotationTypes.CHEBI_ENTITY_TYPE;
                            annotation += chebiEntity.word + "\t" + chebiEntity.stem + "\t" + chebiEntity.iri + "\t" + chebiEntity.begin + "\t" + chebiEntity.end + "\n";
                            break;
                        case EnvoEntity.type:
                            envoEntity = (EnvoEntity) gAnnotation;
                            iri = envoEntity.iri;
                            word = envoEntity.word;
                            pos = AnnotationTypes.ENVO_ENTITY_TYPE;
                            annotation += envoEntity.word + "\t" + envoEntity.stem + "\t" + envoEntity.iri + "\t" + envoEntity.begin + "\t" + envoEntity.end + "\n";
                            break;
                        case FoodKBEntity.type:
                            foodKBEntity = (FoodKBEntity) gAnnotation;
                            iri = foodKBEntity.iri;
                            word = foodKBEntity.word;
                            pos = AnnotationTypes.FOODKB_ENTITY_TYPE;
                            annotation += foodKBEntity.word + "\t" + foodKBEntity.stem + "\t" + foodKBEntity.iri + "\t" + foodKBEntity.begin + "\t" + foodKBEntity.end + "\n";
                            break;
                        case NCBIEntity.type:
                            ncbiEntity = (NCBIEntity) gAnnotation;
                            iri = ncbiEntity.iri;
                            word = ncbiEntity.word;
                            pos = AnnotationTypes.NCBI_ENTITY_TYPE;
                            annotation += ncbiEntity.word + "\t" + ncbiEntity.stem + "\t" + ncbiEntity.iri + "\t" + ncbiEntity.begin + "\t" + ncbiEntity.end + "\n";
                            break;
                        case OntoFoodEntity.type:
                            ontoFoodEntity = (OntoFoodEntity) gAnnotation;
                            iri = ontoFoodEntity.iri;
                            word = ontoFoodEntity.word;
                            pos = AnnotationTypes.ONTOFOOD_ENTITY_TYPE;
                            annotation += ontoFoodEntity.word + "\t" + ontoFoodEntity.stem + "\t" + ontoFoodEntity.iri + "\t" + ontoFoodEntity.begin + "\t" + ontoFoodEntity.end + "\n";
                            break;
                        case OpenFoodEntity.type:
                            openFoodEntity = (OpenFoodEntity) gAnnotation
                            iri = openFoodEntity.iri;
                            word = openFoodEntity.word;
                            pos = AnnotationTypes.OPENDFOOD_ENTITY_TYPE;
                            annotation += openFoodEntity.word + "\t" + openFoodEntity.stem + "\t" + openFoodEntity.iri + "\t" + openFoodEntity.begin + "\t" + openFoodEntity.end + "\n";
                            break;
                        case PlantEntity.type:
                            plantEntity = (PlantEntity) gAnnotation;
                            iri = plantEntity.iri;
                            word = plantEntity.word;
                            pos = AnnotationTypes.PLANT_ENTITY_TYPE;
                            annotation += plantEntity.word + "\t" + plantEntity.stem + "\t" + plantEntity.iri + "\t" + plantEntity.begin + "\t" + plantEntity.end + "\n";
                            break;
                        case UberonEntity.type:
                            uberonEntity = (UberonEntity) gAnnotation;
                            iri = uberonEntity.iri;
                            word = uberonEntity.word;
                            pos = AnnotationTypes.UBERON_ENTITY_TYPE;
                            annotation += uberonEntity.word + "\t" + uberonEntity.stem + "\t" + uberonEntity.iri + "\t" + uberonEntity.begin + "\t" + uberonEntity.end + "\n";
                            break;
                        case PhytochemicalEntity.type:
                            phytochemicalEntity = (PhytochemicalEntity) gAnnotation;
                            iri = phytochemicalEntity.iri;
                            word = phytochemicalEntity.word;
                            pos = AnnotationTypes.PHYTOCHEMICAL_ENTITY_TYPE;
                            annotation += phytochemicalEntity.getWord() + "\t" + phytochemicalEntity.getStem() + "\t" + phytochemicalEntity.getIri() + "\t" + phytochemicalEntity.getBegin() + "\t" + phytochemicalEntity.getEnd() + "\n";
                            break;
                        case ThesaurusEntity.type:
                            thesaurusEntity = (ThesaurusEntity) gAnnotation;
                            word = thesaurusEntity.word;
                            iri = thesaurusEntity.word;
                            pos = AnnotationTypes.THESAURUS_ENTITY_TYPE;
                            annotation += thesaurusEntity.getWord() + "\t" + thesaurusEntity.getStem() + "\t" + thesaurusEntity.getBegin() + "\t" + thesaurusEntity.getEnd() + "\n";
                            break;
                        case FlavorEntity.type:
                            flavorEntity = (FlavorEntity) gAnnotation;
                            word = flavorEntity.word;
                            iri = flavorEntity.flavor;
                            pos = AnnotationTypes.FLAVOUR_ENTITY_TYPE;
                            annotation += flavorEntity.getWord() + "\t" + flavorEntity.getFlavor() + "\t" + flavorEntity.getStem() + "\t" + flavorEntity.getBegin() + "\t" + flavorEntity.getEnd() + "\n";
                            break;
                        case TechniqueEntity.type:
                            techniqueEntity = (TechniqueEntity) gAnnotation;
                            word = techniqueEntity.word;
                            iri = techniqueEntity.word;
                            pos = AnnotationTypes.TECHNIQUE_ENTITY_TYPE;
                            annotation += techniqueEntity.getWord() + "\t" + techniqueEntity.getStem() + "\t" + techniqueEntity.getBegin() + "\t" + techniqueEntity.getEnd() + "\n";
                            break;
                        case MeasurementEntity.type:
                            measurementEntity = (MeasurementEntity) gAnnotation;
                            word = measurementEntity.word;
                            iri = measurementEntity.word;
                            pos = AnnotationTypes.MEASUREMENT_ENTITY_TYPE;
                            annotation += measurementEntity.getWord() + "\t" + measurementEntity.getStem() + "\t" + measurementEntity.getBegin() + "\t" + measurementEntity.getEnd() + "\n";
                            break;
                    }

                    if(pos>=0) {
                        String[] arrayList;
                        if (parser.containsKey(word)) {
                            arrayList = parser.get(word);
                            if (arrayList[pos]!=null) {
                                String aux = arrayList[pos];
                                aux = aux + "|" + iri;
                                arrayList[pos] = aux;
                            } else {
                                arrayList[pos] = iri;
                            }
                        } else {
                            arrayList = new String[AnnotationTypes.MAX_ANNOTATION_TYPES];
                            arrayList[pos] = iri;
                            parser.put(word, arrayList);
                        }
                        //Frequency analysis
                        String key;
                        if (iri.compareTo(word) != 0) {
                            key = iri + "&&" + word;
                        } else {
                            key = word;
                        }
                        if (conceptFrequency.containsKey(key)) {
                            int counter = (Integer) conceptFrequency.get(key);
                            counter++;
                            conceptFrequency.put(key, counter);
                        } else {
                            conceptFrequency.put(key, 1);
                        }
                    }
                }

                synchronized (annotationFile) {
                    annotationFile.println(annotation);
                }
                synchronized (parserFile) {
                    String parserAnnotation;
                    for(String key : parser.keySet()){
                        String[] list = parser.get(key);
                        parserAnnotation = idRecipe+"\t"+title+"\t"+key;
                        for(int i=0;i<AnnotationTypes.MAX_ANNOTATION_TYPES;i++){
                            if(list[i]!=null) {
                                parserAnnotation +="\t"+list[i];
                            }else{
                                parserAnnotation +="\t.";
                            }
                        }
                        parserFile.println(parserAnnotation);
                    }
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
def orderedTermFrequency = termFrequency.sort{it.value}
orderedTermFrequency.each {key,value ->
    termStatisticsFile.println(key+"\t"+value);
}

termStatisticsFile.close();


def conceptStatisticsFile = new PrintWriter(new BufferedWriter(new FileWriter(currentPath+separator+"outputs"+separator+"concepts_statistics.txt")));
conceptStatisticsFile.println("Number of recipes:"+recipeCounter);
def orderedConceptFrequency = conceptFrequency.sort{it.value}
orderedConceptFrequency.each {key,value ->
    conceptStatisticsFile.println(key+"\t"+value);
}
conceptStatisticsFile.close();
