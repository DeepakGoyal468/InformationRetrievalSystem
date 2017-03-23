import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Deepak Goyal
 */
public class Search{
    HashMap<String, Integer> countWord = new HashMap<>();                      //map for storing the word count object.
    HashMap<String, Set<Integer>> invertedIndex  = new HashMap<>();         //map for storing the invertedindex of files.
public static void main(String args[]) throws FileNotFoundException, IOException, ClassNotFoundException{
    
       String word = "उत्तम";
    
    Search result = new Search();
    result.countFrequency(word);
    int length = result.postingList(word);
    System.out.println(length);
}

    /**
     *
     * @param word
     * @throws java.io.FileNotFoundException
     * @throws java.lang.ClassNotFoundException
     */
    public void countFrequency(String word) throws FileNotFoundException, IOException, ClassNotFoundException
{
        FileInputStream fis = new FileInputStream("wordCount");
        try (ObjectInputStream ois = new ObjectInputStream(fis)) {                  //Reading the object.
        countWord =  (HashMap<String, Integer>) ois.readObject();

        if(countWord.containsKey(word))
        {                                                                      //Checking if the word exist or not in the map.
            System.out.print("Word Count:");
            System.out.println(countWord.get(word));
        }    
               
//       try{
//            PrintWriter writer;
//            writer = new PrintWriter("output4.txt", "UTF-8");
//            writer.println("Word     count");
//            for (Map.Entry<String, Integer> entry : countWord.entrySet()) {
//                writer.println(entry.getKey()+" :    "+entry.getValue());       //to get the output file of hashmap
//                //System.out.println(entry.getKey()+" : "+entry.getValue());
//            }
//            writer.close();
//        } catch (IOException e) {
//           
//        }
    }
}

public int postingList(String word) throws FileNotFoundException, IOException, ClassNotFoundException{
        FileInputStream fis1 = new FileInputStream("invertedIndex");
        int length1=0;
        try (ObjectInputStream ois1 = new ObjectInputStream(fis1)) {
        invertedIndex =  (HashMap<String, Set<Integer>>) ois1.readObject();
        
        int length = invertedIndex.size();
        System.out.println("Size of invertedindex is " +length);
        if(invertedIndex.containsKey(word))
        
        {
            //TreeSet<Integer> PostingList = new TreeSet<>(invertedIndex.get(word));  //For sorting the posting list.
            System.out.println("Posting List:");
            System.out.println(invertedIndex.get(word));
//            invertedIndex.get(word).stream().forEach((i) -> {
//                System.out.println(i);
//            });
            TreeSet<Integer> PostingList = new TreeSet<>(invertedIndex.get(word));
            length1 = PostingList.size();
            
            
            System.out.println("The length of posting list is:" + length1);

            
        }
        
        
        
//        try{
//            PrintWriter writer;
//            writer = new PrintWriter("output5.txt", "UTF-8");
//            writer.println("Word     count");
//            for (Map.Entry<String, TreeSet<Integer>> entry : invertedIndex.entrySet()) {
//                writer.println(entry.getKey()+" :    "+entry.getValue());       //to get the output file of hashmap
//                //System.out.println(entry.getKey()+" : "+entry.getValue());
//            }
//            writer.close();
//        } catch (IOException e) {
//           
//        }
    }
        return length1;

}
}
