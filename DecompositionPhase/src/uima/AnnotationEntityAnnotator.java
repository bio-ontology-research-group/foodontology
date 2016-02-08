package uima;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import uima.measurement.MeasurementEntity;
import uima.ontology.AnnotationEntity;
import uima.technique.TechniqueEntity;
import util.AnnotatorTools;
import util.WordEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

public class AnnotationEntityAnnotator extends JCasAnnotator_ImplBase {
    private HashMap chebiLabels, envoLabels, ncbiLabels, plantLabels, uberonLabels;//to avoid repeated labels. Ontologies
    private HashMap techniquesLabels, measurementsLabels;


    public AnnotationEntityAnnotator() {
        //load the ontologies
        String separator = System.getProperty("file.separator");
        String currentPath = System.getProperty("user.dir").replace("src", "");
        String ontologiesPath = currentPath + separator + "resources" + separator + "ontologies" + separator;
        chebiLabels = new HashMap<String, WordEntry>();
        loadOntology(new File(ontologiesPath+"chebi.owl"),chebiLabels);
        envoLabels = new HashMap<String, WordEntry>();
        //loadOntology(new File(ontologiesPath+"envo.owl"),envoLabels);
        ncbiLabels = new HashMap<String, WordEntry>();
        //loadOntology(new File(ontologiesPath+"NCBITAXON_2.owl"),ncbiLabels);
        plantLabels = new HashMap<String, WordEntry>();
        //loadOntology(new File(ontologiesPath+"plant-ontology-21.owl"),plantLabels);
        uberonLabels = new HashMap<String, WordEntry>();
        //loadOntology(new File(ontologiesPath+"uberon_ext.owl"),uberonLabels);
        //load files
        String fileName = "cooking_measurements.txt";
        String measurementsPath =  currentPath + separator + "resources" + separator + "measurements" + separator + fileName;
        measurementsLabels = new HashMap<String, WordEntry>();
        this.loadTermsFile(new File(measurementsPath),measurementsLabels);
        fileName = "cooking_techniques.txt";
        String techniquesPath = currentPath+separator+"resources"+separator+"techniques"+separator+fileName;
        techniquesLabels = new HashMap<String, WordEntry>();
        this.loadTermsFile(new File(techniquesPath),techniquesLabels);

    }

    private void loadTermsFile(File termsFile,HashMap<String, WordEntry> hashMap){
        if((termsFile!=null)&&(termsFile.exists())) {
            try {
                FileInputStream fstream = new FileInputStream(termsFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String strLine = null;
                String stem = null;
                while ((strLine = br.readLine()) != null) {
                    if (strLine != null) {
                        strLine = strLine.toLowerCase();
                        strLine = AnnotatorTools.removeWhiteSpaces(strLine);
                        stem = AnnotatorTools.textStemmer(strLine);
                        hashMap.put(stem, new WordEntry(strLine,null,stem));
                    }
                }
                br.close();
            } catch (Exception e) {

            }
        }
    }

    private void loadOntology(File owlFile, HashMap<String, WordEntry> hashMap) {
        try {
            if (owlFile.exists()) {
                System.out.println("Loading ontology..." + owlFile.getName());
                OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
                OWLDataFactory dataFactory = manager.getOWLDataFactory();
                OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
                config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
                FileDocumentSource fSource = new FileDocumentSource(owlFile);
                OWLOntology ontology = manager.loadOntologyFromOntologyDocument(fSource, config);
                Set<OWLClass> classes = ontology.getClassesInSignature();
                for (OWLClass owlClass : classes) {
                    String iri = owlClass.getIRI().toString();
                    for (OWLAnnotation annotation : EntitySearcher.getAnnotations(owlClass, ontology)) {
                        if(annotation.getProperty().isLabel()||
                                annotation.getProperty().getIRI().getRemainder().get().toLowerCase().contains("synonym") ){
                            if(annotation.getValue() instanceof OWLLiteral) {
                                String label = ((OWLLiteral) annotation.getValue()).getLiteral();
                                label = label.toLowerCase();
                                label = AnnotatorTools.removeWhiteSpaces(label);
                                String labelStemmed = AnnotatorTools.textStemmer(label);
                                hashMap.put(labelStemmed, new WordEntry(label, iri,labelStemmed));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The ontology:" + owlFile.getName() + " was not loaded.");
        }
    }

    private void annotateContent(JCas aJCas, String textStemmed, HashMap<String, WordEntry> map,String entityType) {
        String token;
        int begin = 0;
        ArrayList<String> tokens = new ArrayList<String>();
        for (String key : map.keySet()) {
            tokens.clear();
            StringTokenizer tokenizer = new StringTokenizer(key);
            StringTokenizer tokenizedContent = new StringTokenizer(textStemmed);
            begin = 0;
            while (tokenizer.hasMoreTokens()) {
                tokens.add(tokenizer.nextToken());
            }
            while (tokenizedContent.hasMoreTokens()) {
                if (tokenizedContent.nextToken().compareTo(tokens.get(0)) == 0) {
                    boolean flag = true;
                    for (int i = 1; i < tokens.size(); i++) {
                        if ((!tokenizedContent.hasMoreTokens())||(tokens.get(i).compareTo(tokenizedContent.nextToken()) != 0)) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        begin = textStemmed.indexOf(tokens.get(0), begin);
                        WordEntry entry = map.get(key);
                        switch(entityType) {
                            case "AnnotationEntity_Type":
                                AnnotationEntity annotation = new AnnotationEntity(aJCas);
                                annotation.setBegin(begin);
                                annotation.setEnd(begin + key.length());
                                annotation.setWord(entry.getWord());
                                annotation.setStem(entry.getStem());
                                annotation.setIri(entry.getIri());
                                annotation.addToIndexes();
                                break;
                            case "MeasurementEntity_Type":
                                MeasurementEntity measurement = new MeasurementEntity(aJCas);
                                measurement.setBegin(begin);
                                measurement.setEnd(begin + key.length());
                                measurement.setWord(entry.getWord());
                                measurement.setStem(entry.getStem());
                                measurement.addToIndexes();
                                break;
                            case "TechniqueEntity_Type":
                                TechniqueEntity technique = new TechniqueEntity(aJCas);
                                technique.setBegin(begin);
                                technique.setEnd(begin + key.length());
                                technique.setWord(entry.getWord());
                                technique.setStem(entry.getStem());
                                technique.addToIndexes();
                                break;
                        }
                        begin = begin + key.length();
                    }
                }
            }
        }
    }


    /**
     * @see JCasAnnotator_ImplBase#process(JCas)
     */
    @Override
    public void process(JCas aJCas) {
        // get document text
        String docText = aJCas.getDocumentText();

        String docTextStemmed = AnnotatorTools.textStemmer(docText);
        docTextStemmed = docTextStemmed.toLowerCase();
        annotateContent(aJCas,docTextStemmed,chebiLabels,"AnnotationEntity_Type");
        annotateContent(aJCas,docTextStemmed,envoLabels,"AnnotationEntity_Type");
        annotateContent(aJCas,docTextStemmed,ncbiLabels,"AnnotationEntity_Type");
        annotateContent(aJCas,docTextStemmed,plantLabels,"AnnotationEntity_Type");
        annotateContent(aJCas,docTextStemmed,uberonLabels,"AnnotationEntity_Type");

        annotateContent(aJCas, docTextStemmed, measurementsLabels, "MeasurementEntity_Type");
        annotateContent(aJCas,docTextStemmed,techniquesLabels,"TechniqueEntity_Type");

    }
}