package measurement;

import stemmer.EnglishStemmer;
import util.AbstractAnnotator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by miguel on 2/3/16.
 */
public class CookingMeasurementsAnnotator extends AbstractAnnotator {
    public CookingMeasurementsAnnotator(){
        super();
        String currentPath = System.getProperty("user.dir").replace("src", "");
        String separator = System.getProperty("file.separator");
        String fileName = "cooking_measurements.txt";
        this.loadTermsFile(new File(currentPath+separator+"resources"+separator+"techniques"+separator+fileName));
    }
}
