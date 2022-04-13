import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;

public class DataDriven {

    public CSVReader readCSV(String filepath) throws IOException, CsvValidationException {
        CSVReader reader=new CSVReader(new FileReader(filepath));
        return reader;
    }
}
