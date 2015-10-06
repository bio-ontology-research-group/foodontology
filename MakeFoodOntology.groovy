@Grab(group='net.sourceforge.owlapi', module='owlapi-distribution', version='4.0.1')

import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.*
import org.semanticweb.owlapi.profiles.*
import org.semanticweb.owlapi.util.*
import org.semanticweb.owlapi.io.*
import org.semanticweb.elk.owlapi.*
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary


OWLOntologyManager manager = OWLManager.createOWLOntologyManager()

OWLDataFactory fac = manager.getOWLDataFactory()
OWLDataFactory factory = fac

OWLOntology ont = manager.createOntology(IRI.create("http://aber-owl.net/food-ontology.owl"))
def onturi = "http://phenomebrowser.net/fish-phenotype.owl#"

OWLReasonerFactory reasonerFactory = null

ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor()
OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor)

OWLAnnotationProperty label = fac.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI())

def r = { String s ->
  if (s == "part-of") {
    factory.getOWLObjectProperty(IRI.create("http://purl.obolibrary.org/obo/BFO_0000050"))
  } else if (s == "has-part") {
    factory.getOWLObjectProperty(IRI.create("http://purl.obolibrary.org/obo/BFO_0000051"))
  } else {
    factory.getOWLObjectProperty(IRI.create("http://aber-owl.net/#"+s))
  }
}

def c = { String s ->
  factory.getOWLClass(IRI.create(onturi+s))
}

def addAnno = {resource, prop, cont ->
  OWLAnnotation anno = factory.getOWLAnnotation(
    factory.getOWLAnnotationProperty(prop.getIRI()),
    factory.getOWLLiteral(cont))
  def axiom = factory.getOWLAnnotationAssertionAxiom(resource.getIRI(),
                                                     anno)
  manager.addAxiom(ont,axiom)
}

println "Parsing file..."

def name2cl = [:]
def classes = new LinkedHashSet()

def subcat2cat = [:].withDefault { new TreeSet() }
new File("foodb/foods.csv").eachLine { fline ->
  def line = fline.split(',(?=([^\"]*\"[^\"]*\")*[^\"]*$)', -1)
  if (line.size() == 18) {
    def id = line[0]
    def name = line[2].substring(1,line[2].size()-1)
    def syn = line[1].substring(1,line[1].size()-1)
    def cat = line[11].substring(1,line[11].size()-1)
    def subcat = line[12].substring(1,line[12].size()-1)
    if (cat && subcat) {
      subcat2cat[subcat].add(cat)
    }
  }
}

def counter = 0
def categories = new TreeSet()
subcat2cat.each { subcat, cats ->
  cats.each { categories.add(it) }
}
categories.each { cat ->
  def cl = c("FOODCATEGORY:$counter")
  classes.add(cl)
  addAnno(cl,OWLRDFVocabulary.RDFS_LABEL,cat)
  manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(cl, c("Food")))
  counter += 1
  name2cl[cat] = cl
}
subcat2cat.each { subcat, cats ->
  def cl = c("FOODCATEGORY:$counter")
  classes.add(cl)
  addAnno(cl,OWLRDFVocabulary.RDFS_LABEL,subcat)
  counter += 1
  name2cl[subcat] = cl
}
subcat2cat.each { subcat, cats ->
  def scl = name2cl[subcat]
  cats.each { cat ->
    def ccl = name2cl[cat]
    manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(scl, ccl))
  }
}

new File("foodb/foods.csv").eachLine { fline ->
  def line = fline.split(',(?=([^\"]*\"[^\"]*\")*[^\"]*$)', -1)
  if (line.size() == 18) {
    def id = line[0]
    def name = line[2].substring(1,line[2].size()-1)
    def syn = line[1].substring(1,line[1].size()-1)
    def cat = line[11].substring(1,line[11].size()-1)
    def subcat = line[12].substring(1,line[12].size()-1)
    def cl = c("FOOD:$id")
    classes.add(cl)
    if (name && name!="UL") {
      addAnno(cl,OWLRDFVocabulary.RDFS_LABEL,name)
    }
    if (syn && syn!="UL") {
      addAnno(cl,OWLRDFVocabulary.RDFS_LABEL,syn)
    }
    def ccl = name2cl[subcat]
    if (ccl) {
      manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(cl, ccl))
    }
  }
}

def ccounter = 0
new File("foodb/compounds.csv").eachLine { fline ->
  def line = fline.split(',(?=([^\"]*\"[^\"]*\")*[^\"]*$)', -1)
  if (line.size() == 100) {
    def id = line[0]
    def type = line[2].substring(1,line[2].size()-1)
    def lab = line[4].substring(1,line[4].size()-1)
    def chebi = line[22]?.replaceAll("\"","")
    if (name2cl[type]==null) {
      def cl = c("CHEMTYPE:$ccounter")
      classes.add(cl)
      ccounter +=1
      addAnno(cl,OWLRDFVocabulary.RDFS_LABEL,type)
      manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(cl, c("Chemical")))
      name2cl[type] = cl
    }
    def cl = c("CHEMICAL:$id")
    classes.add(cl)
    addAnno(cl,OWLRDFVocabulary.RDFS_LABEL,lab)
    if (type && name2cl[type]) {
      manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(cl, name2cl[type]))
    }
    if (chebi && chebi != "NULL") {
      manager.addAxiom(ont, factory.getOWLEquivalentClassesAxiom(cl, fac.getOWLClass(IRI.create("http://purl.obolibrary.org/obo/CHEBI_"+chebi))))
    }
  }
}

def fcounter = 0
new File("foodb/flavors.csv").eachLine { fline ->
  def line = fline.split(',(?=([^\"]*\"[^\"]*\")*[^\"]*$)', -1)
  def id = line[0]
  if (id!="id") {
    def name = line[1].substring(1,line[1].size()-1)
    def cat = line[2].substring(1,line[2].size()-1)
    def group = line[3].substring(1,line[3].size()-1)
    if (name2cl[cat] == null && cat!="UL") {
      def cl = c("FLAVOR:$fcounter")
      classes.add(cl)
      fcounter += 1
      addAnno(cl,OWLRDFVocabulary.RDFS_LABEL, cat)
      manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(cl, c("Odor")))
    }
    def cl = c("FLAVOR:$id")
    classes.add(cl)
    addAnno(cl,OWLRDFVocabulary.RDFS_LABEL, name)
    if (name2cl[cat]) {
      manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(cl, name2cl[cat]))
    } else {
      manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(cl, c("Odor")))
      println "Could not find $cat"
    }
  }
}

new File("foodb/compounds_foods.csv").eachLine { fline ->
  def line = fline.split(',(?=([^\"]*\"[^\"]*\")*[^\"]*$)', -1)
  if (line[0]!="id") {
    def cid = line[1]
    def fid = line[2]
    def cl1 = c("CHEMICAL:$cid")
    def cl2 = c("FLAVOR:$fid")
    if (cl1 in classes && cl2 in classes) {
      manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(cl1, fac.getOWLObjectSomeValuesFrom(r("has-flavor"),cl2)))
    }
  }
}
new File("foodb/compounds_foods.csv").eachLine { fline ->
  def line = fline.split(',(?=([^\"]*\"[^\"]*\")*[^\"]*$)', -1)
  if (line[0]!="id") {
    def cid = line[1] // compound
    def fid = line[2] // food
    def cl1 = c("CHEMICAL:$cid")
    def cl2 = c("FOOD:$fid")
    if (cl1 in classes && cl2 in classes) {
      manager.addAxiom(ont, factory.getOWLSubClassOfAxiom(cl2, fac.getOWLObjectSomeValuesFrom(r("has-part"),cl1)))
    }
  }
}

manager.saveOntology(ont, IRI.create("file:/tmp/food.owl"))
