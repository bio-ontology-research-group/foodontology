package uima;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import stemmer.EnglishStemmer;
import util.AnnotatorTools;
import util.LabelEntry;
import util.RecipeAnnotation;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnotationEntityAnnotator extends JCasAnnotator_ImplBase {
    private HashMap chebiLabels, envoLabels, ncbiLabels, plantLabels, uberonLabels;//to avoid repeated labels.

    public AnnotationEntityAnnotator() {
        //load the ontologies
        String separator = System.getProperty("file.separator");
        String currentPath = System.getProperty("user.dir").replace("src", "");
        String ontologiesPath = currentPath + separator + "resources" + separator + "ontologies" + separator;
        chebiLabels = new HashMap<String, LabelEntry>();
        loadOntology(new File(ontologiesPath+"chebi.owl"),chebiLabels);
        envoLabels = new HashMap<String, LabelEntry>();
        loadOntology(new File(ontologiesPath+"envo.owl"),envoLabels);
        ncbiLabels = new HashMap<String, LabelEntry>();
        loadOntology(new File(ontologiesPath+"NCBITAXON_2.owl"),ncbiLabels);
        plantLabels = new HashMap<String, LabelEntry>();
        loadOntology(new File(ontologiesPath+"plant-ontology-21.owl"),plantLabels);
        uberonLabels = new HashMap<String, LabelEntry>();
        loadOntology(new File(ontologiesPath+"uberon_ext.owl"),uberonLabels);

    }

    private void loadOntology(File owlFile, HashMap<String, LabelEntry> hashMap) {
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
                    for (OWLAnnotation a : EntitySearcher.getAnnotations(owlClass, ontology, dataFactory.getRDFSLabel())) {
                        OWLAnnotationValue value = a.getValue();
                        if (value instanceof OWLLiteral) {
                            String label = ((OWLLiteral) value).getLiteral();
                            label = label.toLowerCase();
                            label = AnnotatorTools.removeWhiteSpaces(label);
                            String labelStemmed = AnnotatorTools.textStemmer(label);
                            hashMap.put(labelStemmed, new LabelEntry(label, iri));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("The ontology:" + owlFile.getName() + " was not loaded.");
        }
    }

    private void annotateContent(JCas aJCas, String textStemmed, HashMap<String, LabelEntry> map) {
        String token;
        int begin = 0;
        ArrayList<String> tokens = new ArrayList<String>();
        for (String key : map.keySet()) {
            tokens.clear();
            StringTokenizer tokenizer = new StringTokenizer(key);
            StringTokenizer tokenizedContent = new StringTokenizer(textStemmed);
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
                        int pos = textStemmed.indexOf(tokens.get(0), begin);
                        LabelEntry entry = map.get(key);
                        AnnotationEntity annotation = new AnnotationEntity(aJCas);
                        annotation.setBegin(begin);
                        annotation.setEnd(begin + key.length());
                        annotation.setStem(entry.getLabel());
                        annotation.setIri(entry.getIri());
                        annotation.addToIndexes();
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
        annotateContent(aJCas,docTextStemmed,chebiLabels);
        annotateContent(aJCas,docTextStemmed,envoLabels);
        annotateContent(aJCas,docTextStemmed,ncbiLabels);
        annotateContent(aJCas,docTextStemmed,plantLabels);
        annotateContent(aJCas,docTextStemmed,uberonLabels);
    }
}