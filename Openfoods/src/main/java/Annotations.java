import java.io.*;
import java.nio.file.*;
import java.util.*;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceFault_Exception;
import uk.ac.ebi.chebi.webapps.chebiWS.model.*;

public class Annotations {

    Properties props;

    public Annotations(Properties props) {
        this.props = props;
    }

    public void getChebiAnnotations() throws Exception {
        ChebiWebServiceClient client = new ChebiWebServiceClient();
        String filePath = "data/ingredients.txt";
        Map<String, List<LiteEntity> > mem = new HashMap<String, List<LiteEntity> >();
        PrintWriter out = new PrintWriter(new BufferedWriter(
            new FileWriter("data/ingredients-chebi.txt"), 1073741824));
        try(BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            int c = 0;
            while((line = br.readLine()) != null) {
                String[] items = line.split("\t", -1);
                String query = items[2].trim();
                List<LiteEntity> resultList = null;
                if (!mem.containsKey(query)) {
                    LiteEntityList entities = client.getLiteEntity(
                        query,
                        SearchCategory.ALL,
                        5,
                        StarsCategory.ALL);
                    resultList = entities.getListElement();
                    mem.put(query, resultList);
                } else {
                    resultList = mem.get(query);
                }
                for(LiteEntity entity: resultList) {
                    out.println(
                        line + "\t" + entity.getChebiId() + "|" +
                        entity.getChebiAsciiName() + "|" + entity.getSearchScore());
                }
            }
        }
        out.close();
    }

    public void getDrugbankAnnotations() throws Exception {
        String filePath = "data/drug_links.csv";
        Map<String, List<String> > map = new HashMap<String, List<String> >();
        try(BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            int c = 0;
            while((line = br.readLine()) != null) {
                String[] items = line.split(",", -1);
                String drugBankId = items[0].trim();
                String chebiId = items[8].trim();
                if (!chebiId.equals("")) {
                    if(!map.containsKey(chebiId)) {
                        map.put(chebiId, new ArrayList<String>());
                    }
                    map.get(chebiId).add(drugBankId);
                }
            }
        }
        PrintWriter out = new PrintWriter(new BufferedWriter(
            new FileWriter("data/ingredients-chebi-drugbank.txt"), 1073741824));
        filePath = "data/ingredients-chebi.txt";
        try(BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            int c = 0;
            while((line = br.readLine()) != null) {
                String[] items = line.split("\t", -1);
                String[] chebis = items[3].split("\\|");
                String chebiId = chebis[0].split("\\:")[1];
                System.out.println(chebiId);
                out.print(line + "\t");
                if (map.containsKey(chebiId) && map.get(chebiId).size() > 0) {
                    List<String> dbIds = map.get(chebiId);
                    out.print(dbIds.get(0));
                    for (int i = 1; i < dbIds.size(); i++) {
                        out.print("|" + dbIds.get(i));
                    }
                } else {
                    out.print(".");
                }
                out.println();
            }
        }
        out.close();
    }

}
