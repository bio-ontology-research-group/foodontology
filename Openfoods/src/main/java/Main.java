import java.io.*;
import java.util.*;

public class Main {

    public static final String PROJECT_PROPERTIES = "project.properties";
    Properties props;
    IngredientParser ingredientParser;
    Annotations annotations;

    public Main() throws Exception {
        this.props = this.getProperties();
        this.ingredientParser = new IngredientParser(this.props);
        this.annotations = new Annotations(this.props);
    }

    public Properties getProperties() throws Exception {
        if (this.props != null) {
            return this.props;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        InputStream is = loader.getResourceAsStream(PROJECT_PROPERTIES);
        props.load(is);
        return props;
    }

    public void run(String[] args) throws Exception {
        this.ingredientParser.parseOpenFoods();
        // this.annotations.getChebiAnnotations();
        // this.annotations.getDrugbankAnnotations();
    }


    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }
}
