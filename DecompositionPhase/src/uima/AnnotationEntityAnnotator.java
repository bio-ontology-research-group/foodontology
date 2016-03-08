package uima;


import lemmatizer.LemmatizerAdaptor;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.uimafit.descriptor.ConfigurationParameter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import stemmer.StemmerAdaptor;
import uima.flavor.FlavorEntity;
import uima.list.AgrovocEntity;
import uima.measurement.MeasurementEntity;
import uima.ontology.*;
import uima.phytochemical.PhytochemicalEntity;
import uima.technique.TechniqueEntity;
import uima.thesaurus.ThesaurusEntity;
import util.LinguisticTool;
import util.WordEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnotationEntityAnnotator extends JCasAnnotator_ImplBase {
    private HashMap chebiLabels, envoLabels, ncbiLabels, plantLabels, uberonLabels;//to avoid repeated labels. Ontologies
    private HashMap techniquesLabels, measurementsLabels;
    private HashMap flavorsLabels, foodKBLabels, ontoFoodLabels, openFoodLabels;
    private HashMap languaLabels, phytoChemicals,agrovocLabels;
    private HashMap<String,HashSet<String>> chebi2phyto;
    private LinguisticTool linguisticTool;

    public AnnotationEntityAnnotator() {

    }

    public void initialize(UimaContext context) throws ResourceInitializationException {
        try {
            String tool = (String) context.getConfigParameterValue("LinguisticTool");
            if ((tool == null) || (tool.isEmpty())) {
                throw new Exception();
            }
            linguisticTool = (LinguisticTool) Class.forName(tool).newInstance();


            String separator = System.getProperty("file.separator");
            String currentPath = System.getProperty("user.dir").replace("src", "");
            String ontologiesPath = currentPath + separator + "resources" + separator + "ontologies" + separator;
            chebiLabels = new HashMap<String, WordEntry>();
            loadLabelsOntology(new File(ontologiesPath+"chebi.owl"),chebiLabels);
            envoLabels = new HashMap<String, WordEntry>();
            loadLabelsOntology(new File(ontologiesPath+"envo.owl"),envoLabels);
            ncbiLabels = new HashMap<String, WordEntry>();
            //loadLabelsOntology(new File(ontologiesPath+"NCBITAXON_2.owl"),ncbiLabels);
            plantLabels = new HashMap<String, WordEntry>();
            loadLabelsOntology(new File(ontologiesPath+"plant-ontology-21.owl"),plantLabels);
            uberonLabels = new HashMap<String, WordEntry>();
            loadLabelsOntology(new File(ontologiesPath+"uberon_ext.owl"),uberonLabels);
            foodKBLabels = new HashMap<String, WordEntry>();
            loadAnnotationsOntology(new File(ontologiesPath + "FoodKB.owl"), foodKBLabels, true);
            ontoFoodLabels = new HashMap<String, WordEntry>();
            loadClassesOntology(new File(ontologiesPath + "OntoFood.owl"), ontoFoodLabels);
            openFoodLabels = new HashMap<String, WordEntry>();
            loadAnnotationsOntology(new File(ontologiesPath + "OpenFoodSafetyModelRepository.owl"), openFoodLabels,false);
            agrovocLabels = new HashMap<String, WordEntry>();

            //load files
            String fileName = "cooking_measurements.txt";
            String measurementsPath =  currentPath + separator + "resources" + separator + "measurements" + separator + fileName;
            measurementsLabels = new HashMap<String, WordEntry>();
            loadTermsFile(new File(measurementsPath),measurementsLabels);
            fileName = "cooking_techniques.txt";
            String techniquesPath = currentPath+separator+"resources"+separator+"techniques"+separator+fileName;
            techniquesLabels = new HashMap<String, WordEntry>();
            loadTermsFile(new File(techniquesPath),techniquesLabels);

            fileName = "agrovoc_2016-01-21_core.rdf";
            String agrovocPath = currentPath+separator+"resources"+separator+"agrovoc"+separator+fileName;
            agrovocLabels = new HashMap<String, WordEntry>();
            loadTermsFile(new File(agrovocPath), agrovocLabels);

            //load flavours
            fileName = "flavournet_org.txt";
            String flavorPath = currentPath+separator+"resources"+separator+"flavours"+separator+fileName;
            flavorsLabels = new HashMap<String, WordEntry>();
            loadFlavorsFile(new File(flavorPath), flavorsLabels);

            //load thesaurus
            fileName= "LanguaL2014.XML";
            String languaPath = currentPath+separator+"resources"+separator+"thesaurus"+separator+fileName;
            languaLabels = new HashMap<String, WordEntry>();
            loadLanguaThesaurus(new File(languaPath),languaLabels);

            //load phytochemicals
            fileName = "phytochebi.tsv";
            String phytoChemicalsPath = currentPath+separator+"resources"+separator+"phytochemicals"+separator+fileName;
            phytoChemicals = new HashMap<String, WordEntry>();
            chebi2phyto = new HashMap<String,HashSet<String>>();
            loadPhytoChemicals(new File(phytoChemicalsPath), phytoChemicals);



        }catch(Exception e){
            System.out.println("A linguistic tool has to be defined in the ddescriptor:RecipeAnnotator.xml");
            System.exit(-1);
        }
    }

    private void loadPhytoChemicals(File phytoFile, HashMap<String, WordEntry> hashMap){
        if((phytoFile!=null)&&(phytoFile.exists())){
            try{
                /*Example:
                  PS083604	cyanidin-3-glucoside chloride; MS2; QqQ; positive; CE 40 V	PUBCHEM CID 441667	.	28426
                  phytochemical Id 	chemical title 												  plant name chebi ids.
                 */
                System.out.println("Loading phytochemicals file..." + phytoFile.getName());
                FileInputStream fstream = new FileInputStream(phytoFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String strLine = null;
                String stem = null;
                while ((strLine = br.readLine()) != null) {
                    if (strLine != null) {
                        String[] attributes = strLine.split("\\t");
                        if (attributes.length == 5) {
                            String phytochemical = attributes[0];
                            String plantName = attributes[3];
                            String idChebi = attributes[4];
                            String[] idsChebi = idChebi.split(" ");
                            if(idChebi.compareTo(".")!=0){
                                HashSet<String> set = null;
                                for(String id : idsChebi) {
                                    id = id.toUpperCase();
                                    id = linguisticTool.removeWhiteSpaces(id);
                                    id = "CHEBI_"+id;
                                    if (chebi2phyto.containsKey(id)) {
                                        set = chebi2phyto.get(id);
                                    } else {
                                        set = new HashSet<String>();
                                    }
                                    set.add(phytochemical);
                                    chebi2phyto.put(id, set);
                                }
                            }
                            if(plantName.compareTo(".")!=0){
                                plantName = plantName.toLowerCase();
                                plantName = linguisticTool.removeWhiteSpaces(plantName);
                                String plantNameStemmed = linguisticTool.processString(plantName);
                                WordEntry entry = new WordEntry(plantName, plantNameStemmed);
                                for(String id : idsChebi) {
                                    id = id.toUpperCase();
                                    id = linguisticTool.removeWhiteSpaces(id);
                                    id = "CHEBI_" + id;
                                    entry.addAnnotation(id);
                                }
                                hashMap.put(plantName,entry);
                            }
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("The file:" + phytoFile.getName() + " was not loaded.");
            }
        }
    }

    private void loadLanguaThesaurus(File languaFile, HashMap<String, WordEntry> hashMap){
        if((languaFile!=null)&&(languaFile.exists())){
            try{
                System.out.println("Loading thesaurus file..." + languaFile.getName());
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(languaFile);
                NodeList descriptorList = document.getElementsByTagName("DESCRIPTOR");
                for(int i = 0; i<descriptorList.getLength(); i++){
                    Node descriptor = descriptorList.item(i);
                    NodeList children = descriptor.getChildNodes();
                    HashSet<String> termSet = new HashSet<String>();
                    HashSet<String> synSet = new HashSet<String>();
                    for(int j=0;j<children.getLength();j++) {
                        Node child = children.item(j);
                        if(child.getNodeName().compareTo("TERM")==0) {
                            String term = child.getFirstChild().getNodeValue();
                            //We filter the term.
                            term = term.replaceAll("^[0-9]*\\s-", "");
                            term = term.replaceAll("\\([^)]*\\)", "");
                            if (!term.contains("<")&&(!term.contains(">")&&(!term.contains("%")))){
                                String[] terms = term.split(",");
                                if (terms.length > 1) {
                                    for (String aux : terms) {
                                        aux = aux.replace(" OR ", "");
                                        termSet.add(aux);
                                    }
                                } else {
                                    termSet.add(term);
                                }
                            }
                        }
                        if(child.getNodeName().compareTo("SYNONYMS")==0) {
                            NodeList synonymsList = child.getChildNodes();
                            for(int k=0; k< synonymsList.getLength();k++){
                                Node synonymNode = synonymsList.item(k);
                                if(synonymNode.getFirstChild()!=null) {
                                    String synonym = synonymNode.getFirstChild().getNodeValue();
                                    synonym = synonym.replaceAll("\\([^)]*\\)","");
                                    String[] synonyms = synonym.split(",");
                                    if(synonyms.length>1){
                                        for(String aux : synonyms){
                                            aux = aux.replace(" OR ","");
                                            termSet.add(aux);
                                        }
                                    }else{
                                        synSet.add(synonym);
                                    }
                                }
                            }
                        }
                    }
                    for(String term : termSet){
                        term = term.toLowerCase();
                        term = linguisticTool.removeWhiteSpaces(term);
                        String stem = linguisticTool.processString(term);
                        hashMap.put(stem, new WordEntry(term,stem));
                    }
                    for(String synonym : synSet){
                        synonym = synonym.toLowerCase();
                        synonym = linguisticTool.removeWhiteSpaces(synonym);
                        String stem = linguisticTool.processString(synonym);
                        hashMap.put(stem, new WordEntry(synonym,stem));
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("The ontology:" + languaFile.getName() + " was not loaded.");
            }
        }
    }
    private void loadFlavorsFile(File flavorsFile, HashMap<String, WordEntry> hashMap){
        if((flavorsFile!=null)&&(flavorsFile.exists())){
            try{
                System.out.println("Loading flavors file..." + flavorsFile.getName());
                FileInputStream fstream = new FileInputStream(flavorsFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String strLine = null;
                String stem = null;
                while ((strLine = br.readLine()) != null) {
                    if (strLine != null) {
                        String[] attributes = strLine.split("\\t");
                        if(attributes.length==6) {
                            String flavor = attributes[4];
                            WordEntry wordEntry = null;
                            String[] ingredients = attributes[5].split(",");
                            for(String ingredient : ingredients) {
                                ingredient = ingredient.toLowerCase();
                                ingredient = linguisticTool.removeWhiteSpaces(ingredient);
                                stem = linguisticTool.processString(ingredient);
                                if(hashMap.containsKey(stem)){
                                    wordEntry = hashMap.get(stem);
                                    wordEntry.addAnnotation(flavor);
                                    hashMap.put(stem,wordEntry);
                                }else {
                                    wordEntry = new WordEntry(ingredient, stem);
                                    wordEntry.addAnnotation(flavor);
                                    hashMap.put(stem, wordEntry);
                                }
                            }
                        }
                    }
                }
                br.close();
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("The ontology:" + flavorsFile.getName() + " was not loaded.");
            }

        }
    }

    private void loadTermsFile(File termsFile,HashMap<String, WordEntry> hashMap){
        if((termsFile!=null)&&(termsFile.exists())) {
            try {
                System.out.println("Loading file..." + termsFile.getName());
                FileInputStream fstream = new FileInputStream(termsFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String strLine = null;
                String stem = null;
                while ((strLine = br.readLine()) != null) {
                    if (strLine != null) {
                        strLine = strLine.toLowerCase();
                        strLine = linguisticTool.removeWhiteSpaces(strLine);
                        stem = linguisticTool.processString(strLine);
                        hashMap.put(stem, new WordEntry(strLine,stem));
                    }
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("The file:" + termsFile.getName() + " was not loaded.");
            }
        }
    }

    private void loadAnnotationsOntology(File owlFile, HashMap<String, WordEntry> hashMap,boolean loadFromIRI){
        try {
            if (owlFile.exists()) {
                System.out.println("Loading ontology..." + owlFile.getName());
                OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
                OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
                config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
                FileDocumentSource fSource = new FileDocumentSource(owlFile);
                OWLOntology ontology = manager.loadOntologyFromOntologyDocument(fSource, config);
                for (OWLAxiom axiom : ontology.getAxioms()) {
                    if (axiom.getAxiomType() == AxiomType.ANNOTATION_ASSERTION) {

                        OWLAnnotationAssertionAxiom aaxiom = (OWLAnnotationAssertionAxiom) axiom;
                        if(aaxiom.getValue().asLiteral().isPresent()&&(aaxiom.getSubject() instanceof IRI)) {
                            String literal = "";
                            IRI iri = (IRI) aaxiom.getSubject();
                            OWLAnnotation annotation = aaxiom.getAnnotation();
                            if (loadFromIRI) {
                                literal = iri.getShortForm();
                                literal = literal.replaceAll("_"," ");
                            } else {
                                literal = annotation.getValue().asLiteral().get().getLiteral();
                            }
                            literal = literal.toLowerCase();
                            literal = linguisticTool.removeWhiteSpaces(literal);
                            String literalStemmed = linguisticTool.processString(literal);
                            hashMap.put(literalStemmed, new WordEntry(literal, iri.getShortForm(), literalStemmed));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The ontology:" + owlFile.getName() + " was not loaded.");
        }
    }

    private void loadClassesOntology(File owlFile, HashMap<String, WordEntry> hashMap){
        try {
            if (owlFile.exists()) {
                System.out.println("Loading ontology..." + owlFile.getName());
                OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
                OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
                config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
                FileDocumentSource fSource = new FileDocumentSource(owlFile);
                OWLOntology ontology = manager.loadOntologyFromOntologyDocument(fSource, config);
                Set<OWLClass> owlClasses = ontology.getClassesInSignature();
                for(OWLClass owlClass : owlClasses){
                    String iri = owlClass.getIRI().getShortForm();
                    String literal = owlClass.getIRI().getShortForm();
                    literal = literal.replaceAll("_"," ");
                    literal = literal.toLowerCase();
                    literal = linguisticTool.removeWhiteSpaces(literal);
                    String literalStemmed = linguisticTool.processString(literal);
                    hashMap.put(literalStemmed, new WordEntry(literal, iri, literalStemmed));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The ontology:" + owlFile.getName() + " was not loaded.");
        }
    }

    private void loadLabelsOntology(File owlFile, HashMap<String, WordEntry> hashMap) {
        try {
            if (owlFile.exists()) {
                System.out.println("Loading ontology..." + owlFile.getName());
                OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
                OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
                config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
                FileDocumentSource fSource = new FileDocumentSource(owlFile);
                OWLOntology ontology = manager.loadOntologyFromOntologyDocument(fSource, config);
                Set<OWLClass> classes = ontology.getClassesInSignature();
                for (OWLClass owlClass : classes) {
                    String iri = owlClass.getIRI().getShortForm();
                    for (OWLAnnotation annotation : EntitySearcher.getAnnotations(owlClass, ontology)) {
                        if(annotation.getProperty().isLabel()||
                                annotation.getProperty().getIRI().getRemainder().get().toLowerCase().contains("synonym") ){
                            if(annotation.getValue() instanceof OWLLiteral) {
                                String label = ((OWLLiteral) annotation.getValue()).getLiteral();
                                label = linguisticTool.removeWhiteSpaces(label);
                                if(label.length()>4) {
                                    label = label.toLowerCase();
                                    String labelStemmed = linguisticTool.processString(label);
                                    hashMap.put(labelStemmed, new WordEntry(label, iri, labelStemmed));
                                }
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

    private void annotateContent(JCas aJCas, String textStemmed, HashMap<String, WordEntry> map,int entityType) {
        String pattern="";
        boolean flagMultiword;
        for (String key : map.keySet()) {
            StringTokenizer keyTokenizer = new StringTokenizer(key);
            flagMultiword = false;
            pattern="(\\b";
            while (keyTokenizer.hasMoreTokens()) {
                pattern +=linguisticTool.escapePattern(keyTokenizer.nextToken())+"[\\w]*";
                if(keyTokenizer.hasMoreTokens()){
                    flagMultiword = true;
                    pattern +="\\s";
                }
            }
            pattern+=")";
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(textStemmed);
            while(matcher.find()) {
                String extractedWord = matcher.group();
                WordEntry entry = map.get(key);
                if((!flagMultiword)&&(Math.abs(entry.getWord().length()-extractedWord.length())>3)){
                    continue;
                }
                switch (entityType) {
                    case AnnotationTypes.CHEBI_ENTITY_TYPE:
                        ChebiEntity chebiEntity = new ChebiEntity(aJCas);
                        chebiEntity.setBegin(matcher.start());
                        chebiEntity.setEnd(matcher.end());
                        chebiEntity.setWord(entry.getWord());
                        chebiEntity.setStem(entry.getStem());
                        chebiEntity.setIri(entry.getIri());
                        chebiEntity.addToIndexes();
                        //we check the phytochemicals.
                        if(chebi2phyto.containsKey(entry.getIri())){
                            HashSet<String> set = chebi2phyto.get(entry.getIri());
                            for(String phyto : set){
                                PhytochemicalEntity phytochemicalEntity = new PhytochemicalEntity(aJCas);
                                phytochemicalEntity.setBegin(matcher.start());
                                phytochemicalEntity.setEnd(matcher.end());
                                phytochemicalEntity.setWord(entry.getWord());
                                phytochemicalEntity.setIri(phyto);
                                phytochemicalEntity.addToIndexes();
                            }
                        }
                        break;
                    case AnnotationTypes.ENVO_ENTITY_TYPE:
                        EnvoEntity envoEntity = new EnvoEntity(aJCas);
                        envoEntity.setBegin(matcher.start());
                        envoEntity.setEnd(matcher.end());
                        envoEntity.setWord(entry.getWord());
                        envoEntity.setStem(entry.getStem());
                        envoEntity.setIri(entry.getIri());
                        envoEntity.addToIndexes();
                        break;
                    case AnnotationTypes.FOODKB_ENTITY_TYPE:
                        FoodKBEntity foodKBEntity = new FoodKBEntity(aJCas);
                        foodKBEntity.setBegin(matcher.start());
                        foodKBEntity.setEnd(matcher.end());
                        foodKBEntity.setWord(entry.getWord());
                        foodKBEntity.setStem(entry.getStem());
                        foodKBEntity.setIri(entry.getIri());
                        foodKBEntity.addToIndexes();
                        break;
                    case AnnotationTypes.NCBI_ENTITY_TYPE:
                        NCBIEntity ncbiEntity = new NCBIEntity(aJCas);
                        ncbiEntity.setBegin(matcher.start());
                        ncbiEntity.setEnd(matcher.end());
                        ncbiEntity.setWord(entry.getWord());
                        ncbiEntity.setStem(entry.getStem());
                        ncbiEntity.setIri(entry.getIri());
                        ncbiEntity.addToIndexes();
                        break;
                    case AnnotationTypes.ONTOFOOD_ENTITY_TYPE:
                        OntoFoodEntity ontoFoodEntity = new OntoFoodEntity(aJCas);
                        ontoFoodEntity.setBegin(matcher.start());
                        ontoFoodEntity.setEnd(matcher.end());
                        ontoFoodEntity.setWord(entry.getWord());
                        ontoFoodEntity.setStem(entry.getStem());
                        ontoFoodEntity.setIri(entry.getIri());
                        ontoFoodEntity.addToIndexes();
                        break;
                    case AnnotationTypes.PLANT_ENTITY_TYPE:
                        PlantEntity plantEntity = new PlantEntity(aJCas);
                        plantEntity.setBegin(matcher.start());
                        plantEntity.setEnd(matcher.end());
                        plantEntity.setWord(entry.getWord());
                        plantEntity.setStem(entry.getStem());
                        plantEntity.setIri(entry.getIri());
                        plantEntity.addToIndexes();
                        break;
                    case AnnotationTypes.UBERON_ENTITY_TYPE:
                        UberonEntity uberonEntity = new UberonEntity(aJCas);
                        uberonEntity.setBegin(matcher.start());
                        uberonEntity.setEnd(matcher.end());
                        uberonEntity.setWord(entry.getWord());
                        uberonEntity.setStem(entry.getStem());
                        uberonEntity.setIri(entry.getIri());
                        uberonEntity.addToIndexes();
                        break;
                    case AnnotationTypes.PHYTOCHEMICAL_ENTITY_TYPE:
                        for(String idChebi : entry.getAnnotations()) {
                            PhytochemicalEntity phytochemicalEntity = new PhytochemicalEntity(aJCas);
                            phytochemicalEntity.setBegin(matcher.start());
                            phytochemicalEntity.setEnd(matcher.start());
                            phytochemicalEntity.setWord(entry.getWord());
                            phytochemicalEntity.setStem(entry.getStem());
                            phytochemicalEntity.setIri(idChebi);
                            phytochemicalEntity.addToIndexes();
                        }
                        break;
                    case AnnotationTypes.MEASUREMENT_ENTITY_TYPE:
                        MeasurementEntity measurement = new MeasurementEntity(aJCas);
                        measurement.setBegin(matcher.start());
                        measurement.setEnd(matcher.end());
                        measurement.setWord(entry.getWord());
                        measurement.setStem(entry.getStem());
                        measurement.addToIndexes();
                        break;
                    case AnnotationTypes.TECHNIQUE_ENTITY_TYPE:
                        TechniqueEntity technique = new TechniqueEntity(aJCas);
                        technique.setBegin(matcher.start());
                        technique.setEnd(matcher.end());
                        technique.setWord(entry.getWord());
                        technique.setStem(entry.getStem());
                        technique.addToIndexes();
                        break;
                    case AnnotationTypes.FLAVOUR_ENTITY_TYPE:
                        for(String flavor : entry.getAnnotations()) {
                            FlavorEntity flavorEntity = new FlavorEntity(aJCas);
                            flavorEntity.setBegin(matcher.start());
                            flavorEntity.setEnd(matcher.start());
                            flavorEntity.setWord(entry.getWord());
                            flavorEntity.setStem(entry.getStem());
                            flavorEntity.setFlavor(flavor);
                            flavorEntity.addToIndexes();
                        }
                        break;
                    case AnnotationTypes.THESAURUS_ENTITY_TYPE:
                        ThesaurusEntity thesaurus = new ThesaurusEntity(aJCas);
                        thesaurus.setBegin(matcher.start());
                        thesaurus.setEnd(matcher.end());
                        thesaurus.setWord(entry.getWord());
                        thesaurus.setStem(entry.getStem());
                        thesaurus.addToIndexes();
                        break;

                    case AnnotationTypes.AGROVOC_ENTITY_TYPE:
                        AgrovocEntity agrovocEntity = new AgrovocEntity(aJCas);
                        agrovocEntity.setBegin(matcher.start());
                        agrovocEntity.setEnd(matcher.end());
                        agrovocEntity.setWord(entry.getWord());
                        agrovocEntity.setStem(entry.getStem());
                        agrovocEntity.addToIndexes();
                        break;
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
        //Ontologies
        String docTextStemmed = linguisticTool.processString(docText);
        docTextStemmed = docTextStemmed.toLowerCase();
        annotateContent(aJCas,docTextStemmed,chebiLabels,AnnotationTypes.CHEBI_ENTITY_TYPE);
        annotateContent(aJCas,docTextStemmed,envoLabels,AnnotationTypes.ENVO_ENTITY_TYPE);
        annotateContent(aJCas,docTextStemmed,foodKBLabels,AnnotationTypes.FOODKB_ENTITY_TYPE);
        annotateContent(aJCas,docTextStemmed,ncbiLabels,AnnotationTypes.NCBI_ENTITY_TYPE);
        annotateContent(aJCas,docTextStemmed,ontoFoodLabels,AnnotationTypes.ONTOFOOD_ENTITY_TYPE);
        annotateContent(aJCas,docTextStemmed,openFoodLabels,AnnotationTypes.OPENDFOOD_ENTITY_TYPE);
        annotateContent(aJCas,docTextStemmed,plantLabels,AnnotationTypes.PLANT_ENTITY_TYPE);
        annotateContent(aJCas,docTextStemmed,uberonLabels,AnnotationTypes.UBERON_ENTITY_TYPE);
        annotateContent(aJCas,docTextStemmed,agrovocLabels,AnnotationTypes.AGROVOC_ENTITY_TYPE);

        //Phytochemicals
        annotateContent(aJCas,docTextStemmed,phytoChemicals,AnnotationTypes.PHYTOCHEMICAL_ENTITY_TYPE);
        //Measurements
        annotateContent(aJCas, docTextStemmed, measurementsLabels, AnnotationTypes.MEASUREMENT_ENTITY_TYPE);
        //Techniques
        annotateContent(aJCas,docTextStemmed,techniquesLabels,AnnotationTypes.TECHNIQUE_ENTITY_TYPE);
        //Flavours
        annotateContent(aJCas,docTextStemmed,flavorsLabels,AnnotationTypes.FLAVOUR_ENTITY_TYPE);
        //Thesaurus
        annotateContent(aJCas,docTextStemmed,languaLabels,AnnotationTypes.THESAURUS_ENTITY_TYPE);
    }
}