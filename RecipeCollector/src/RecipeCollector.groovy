import api.CS50API
import api.Collector
import api.EdamamAPI
import api.Food2ForkApi
import api.RecipeBridgeAPI
import api.RecipePuppyAPI
import api.TacoFancyAPI
import api.WeEattAPI
import groovyx.gpars.GParsPool

ArrayList<String> ingredientsList = new ArrayList();
File fileIngredients = new File(args[0]);
fileIngredients.eachLine { ingredient ->
    if((ingredient!=null)&&(ingredient.length()>2)){
        ingredientsList.add(ingredient);
    }
}

String outPath = args[1];
ArrayList<Collector> collectorList = new ArrayList<Collector>();
collectorList.add(new CS50API(outPath));
collectorList.add(new EdamamAPI(outPath,ingredientsList));
collectorList.add(new Food2ForkApi(outPath));
//*collectorList.add(new RecipeBridgeAPI(outPath));
collectorList.add(new RecipePuppyAPI(outPath,ingredientsList));
collectorList.add(new TacoFancyAPI(outPath));
//*collectorList.add(new WeEattAPI(outPath));

GParsPool.withPool {
    collectorList.eachParallel {collector ->
        collector.collectRecipes();
    }
}
