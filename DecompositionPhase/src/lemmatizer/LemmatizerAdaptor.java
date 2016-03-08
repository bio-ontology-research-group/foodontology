package lemmatizer;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import util.LinguisticTool;

import java.util.Properties;


/**
 * Created by marg27 on 04/03/16.
 */
public class LemmatizerAdaptor extends LinguisticTool {
    Properties props;
    StanfordCoreNLP pipeline;

    public LemmatizerAdaptor() {
        props = new Properties();
        props.put("annotators", "tokenize,ssplit, pos,  lemma");
        pipeline = new StanfordCoreNLP(props, false);
    }

    public String processString(String text) {
        String lemma = "";
        Annotation document = pipeline.process(text);
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                lemma += token.get(CoreAnnotations.LemmaAnnotation.class) + " ";
            }
        }
        return lemma;
    }

}

