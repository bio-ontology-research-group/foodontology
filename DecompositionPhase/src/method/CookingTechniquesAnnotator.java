package method;


import stemmer.EnglishStemmer;
import stemmer.SnowballProgram;
import util.AbstractAnnotator;
import util.RecipeAnnotation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by marg27 on 31/01/16.
 */
public class CookingTechniquesAnnotator extends AbstractAnnotator {
    public CookingTechniquesAnnotator(){
        super();
        String currentPath = System.getProperty("user.dir").replace("src", "");
        String separator = System.getProperty("file.separator");
        String fileName = "cooking_measurements.txt";
        this.loadTermsFile(new File(currentPath+separator+"resources"+separator+"techniques"+separator+fileName));
    }
}
