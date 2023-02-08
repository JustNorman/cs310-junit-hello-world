package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

public class Converter {
    
    /*  
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "{}"; // default return value; replace later!
        
        try {
        
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            //String[] headings = full.get(0);
            Iterator<String[]> iterator = full.iterator();
            //System.out.print(Arrays.toString(headings));
            
            // INSERT YOUR CODE HERE
            JSONObject json = new JSONObject();
            JSONArray Prod = new JSONArray();
            JSONArray Col = new JSONArray();
            JSONArray Data = new JSONArray();
            JSONArray holder;
            String[] record = iterator.next();
            
            for(int i = 0; i < record.length; i++)
            {
                Prod.add(record[i]);
            }
            while(iterator.hasNext())
            {
                holder = new JSONArray();
                record = iterator.next();
                Col.add(record[0]);
                for(int i = 1; i < record.length; i++)
                {
                    int stringHolder = Integer.parseInt(record[i]);
                    holder.add(stringHolder);
                }
                Data.add(holder);
            }
            //json.put("ProdNums", Prod);
            json.put("ColHeadings", Col);
            json.put("Data", Data);
            result = JSONValue.toJSONString(json);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
        
        try {
            
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");
            
            // INSERT YOUR CODE HERE
            //JsonArray records = new JsonArray();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(jsonString);
            JSONArray Prod = (JSONArray)json.get("ProdNums");
            JSONArray Col = (JSONArray)json.get("ColHeadings");
            JSONArray Data = (JSONArray)json.get("Data");
            JSONArray holder;
            String[] record = new String[Prod.size()];
            
            for(int i = 0; i < Prod.size(); i++)
            {
                record[i] = (String) Prod.get(i);
            }
            csvWriter.writeNext(record);
            for(int i = 0; i < Data.size(); i++)
            {
                holder = (JSONArray) Data.get(i);
                record = new String[holder.size() + 1];
                record[0] = (String) Col.get(i);
                for(int j = 0; j < holder.size(); j++)
                {
                    record[j + 1] = Long.toString((long)holder.get(j));
                }
                csvWriter.writeNext(record);
            }
            result = writer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
}
