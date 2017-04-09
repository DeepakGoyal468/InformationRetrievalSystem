package retrieval;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Deepak Goyal
 */
public class termWeight {
    
    
    HashMap<String, HashMap<Integer, Double>> normalizedWeight = new HashMap<>();
   
    Retrieval rt = new Retrieval();
    public void termweight() throws IOException, FileNotFoundException, ClassNotFoundException{
    String word;
    
    for (Map.Entry<String, HashMap<Integer, Integer>> entry :rt.termFrequency.entrySet()) {
        word = entry.getKey();
        
        HashMap<Integer, Double> frequency1 = new HashMap<>();
        double idf = rt.inverseTermFrequency(word);
        
        
        for(Map.Entry<Integer, Integer> entry1 : rt.termFrequency.get(word).entrySet()){
            double weight = entry1.getValue()*idf;
            frequency1.put(entry1.getKey(),weight);
            //termWeight.put(word, frequency1);
           
        }

        double sum = 0;
        double normWeight = 0;

        for(Map.Entry<Integer, Double> entry2 : frequency1.entrySet()) {
              sum += entry2.getValue();        
        }
        for(Map.Entry<Integer, Double> entry2 : frequency1.entrySet()) {
              normWeight = entry2.getValue()/sum;
              entry2.setValue(normWeight);
              normalizedWeight.put(word,frequency1);                           
        }    
    }   
}
    
    public static void main(String args[]) throws FileNotFoundException, IOException, ClassNotFoundException{
        
        termWeight tw = new termWeight();
        tw.termweight();
        
        FileOutputStream fileOutputStream = new FileOutputStream("normalizedweight");
    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
        objectOutputStream.writeObject(tw.normalizedWeight);
        objectOutputStream.close();
    }
//        try{
//                PrintWriter writer;
//                writer = new PrintWriter("normalizedweight.txt", "UTF-8");
//                writer.println("Word     count");
//                tw.normalizedWeight.entrySet().stream().forEach((entry) -> {
//                    writer.println(entry.getKey()+" :    "+entry.getValue());       //to get the output file of hashmap
//                    //System.out.println(entry.getKey()+" : "+entry.getValue());
//            });
//                writer.close();
//            } catch (IOException e) {
//               
//            }
        
        
    }
    
    
    
}
