import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory


try
{
    ArrayList<File> listFiles = new ArrayList<File>()
    listFiles.add(new File("../recipes_raw/xls format/AUSNUT 2007 - Food Database.xls"));
    listFiles.add(new File("../recipes_raw/xls format/copy_of_qccc_recipe_list_-_easy-1.xlsx"));
    listFiles.add(new File("../recipes_raw/xls format/new_recipes.xlsx"));
    listFiles.eachWithIndex { File file, int index ->
        if(index==0) {
            Workbook workbook = WorkbookFactory.create(file);
            //Create Workbook instance holding reference to .xlsx file
            //Get first/desired sheet from the workbook
            Sheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            for(int i=1;i<sheet.size();i++){
                Row row = sheet.getRow(i);
                if((row!=null)&&(!row.getCell(0).getStringCellValue().isEmpty())) {
                    Cell cName = row.getCell(3);
                    Cell cRecipe = row.getCell(4);
                    if((!cName.getStringCellValue().isEmpty())&&(!cRecipe.getStringCellValue().isEmpty())) {
                        String name = cName.getStringCellValue();
                        String recipe = cRecipe.getStringCellValue();
                        File fileRecipe = new File("../recipes/" + URLEncoder.encode(name, "UTF-8") + ".txt");
                        PrintWriter printWriter = new PrintWriter(fileRecipe);
                        printWriter.println(name);
                        printWriter.println(recipe);
                        printWriter.close();
                    }
                }
            }
        }else{
            Workbook workbook = WorkbookFactory.create(file);
            for(int i=1;i<workbook.getNumberOfSheets();i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if(sheet.getRow(1).getCell(0)!=null) {
                    String name = sheet.getRow(1).getCell(0).getStringCellValue();
                    String recipe = "";
                    if ((name != null) && (!name.isEmpty())) {
                        for (iRow = 0; iRow < 100; iRow++) {
                            Row row = sheet.getRow(iRow);
                            if (row != null) {
                                String line = ""
                                for (jRow = 0; jRow < 100; jRow++) {
                                    Cell cell = row.getCell(jRow);
                                    if (cell != null) {
                                        switch (cell.getCellType()) {
                                            case Cell.CELL_TYPE_NUMERIC:
                                                line += String.valueOf(cell.getNumericCellValue()) + "\t";
                                                break;
                                            case Cell.CELL_TYPE_STRING:
                                                line += cell.getStringCellValue() + "\t";
                                                break;
                                        }
                                    }
                                }
                                if (!line.isEmpty()) {
                                    recipe += line + "\n";
                                }
                            }
                        }
                        File fileRecipe = new File("../recipes/" + URLEncoder.encode(name, "UTF-8") + ".txt");
                        PrintWriter printWriter = new PrintWriter(fileRecipe);
                        printWriter.println(recipe);
                        printWriter.close();
                    }
                }
            }
        }
    }
}
catch (Exception e) {
    e.printStackTrace();
}
