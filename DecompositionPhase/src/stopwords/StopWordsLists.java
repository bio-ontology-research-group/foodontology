package stopwords;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * Created by marg27 on 31/01/16.
 */
public class StopWordsLists {
    private static StopWordsLists instance = null;
    private HashSet<String> stopWords;
    private File stopWordsFile;
    private StopWordsLists(){
        stopWords = new HashSet<String>();
        this.loadStopWordsFile();
    }
    public static StopWordsLists getInstance(){
        if(instance == null){
            instance = new StopWordsLists();
        }
        return(instance);
    }

    private void loadStopWordsFile() {
        String currentPath = System.getProperty("user.dir").replace("src", "");
        String separator = System.getProperty("file.separator");
        stopWordsFile = new File(currentPath + separator + "resources" + separator + "ontologies" + separator + "stopwords.txt");
        if (stopWordsFile.exists()) {
            try{
                FileInputStream fstream = new FileInputStream(stopWordsFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String strLine=null;
                String stopWord=null;
                while ((strLine = br.readLine()) != null)   {
                    stopWord = strLine.replaceAll("\\s+$", "");
                    stopWord = stopWord.toLowerCase();
                    stopWords.add(stopWord);
                }
                br.close();
            }catch(Exception e){

            }
        }
    }

    public boolean checkWord(String word){
        if(word!=null){
            return(stopWords.contains(word.toLowerCase()));
        }
        return(false);
    }
}
