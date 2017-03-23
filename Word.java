import java.io.*;
import static java.lang.Math.log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Word2{
    HashMap <String,Integer>  fileIndexList= new HashMap<>();                   //map for storing the index of files
    int indexOfFile=1;
    double length =0;
    Map<String, Set<Integer>> invertedList = new HashMap <>();              //Map for storing the word and their posting list
    HashSet<String> stopwordfile = new HashSet<>();
    HashMap<String, HashMap<Integer, Integer>> termFrequency = new HashMap<>();
    HashMap<String, Double> inverseFrequency = new HashMap<>();
    HashMap<String, HashMap<Integer, Double>> termWeight = new HashMap<>();
    static double size =0;
    Search result = new Search();
public String validWord(String word){

    word=word.replaceAll("\\<[^>]*>","");                                       //it replaces all the tags and read the content between them.
  //word=word.replaceAll("(?d)<date>.*?</date>", "");                           //.*? Matches any character upto the next </DATE> string.// from net 
    return word;         
    
}
public String removePunctuationMarks(String word){

   if(word.contains("|") || word.contains("॥")){
       word = word.replace("।","");
       word = word.replace("॥","");
   }

   String replaceAll = word.replaceAll("\\p{Punct}+", "");                      //From net for removing only special characters.
   return replaceAll;

}
public void createIndexOfFile(String file){                                     //function for creating index of all files.
    fileIndexList.put(file,indexOfFile);                
    indexOfFile++;

}

public void makeIndex(String word, int docIndex){
    Set<Integer> set1 = new HashSet<>();
    if(invertedList.containsKey(word)){
       set1 = invertedList.get(word);                                           //set1 is to store the ids of docs in which word exist..
       set1.add(docIndex);                                                      //if that docid is already there then it will not add id. 
                                                                                //and if it is not there then it will add.
    }
    else{
       //set1 = new HashSet<>();                                                //set1 is for creating a new set.
       set1.add(docIndex);                                                      //if word is occuring first time in the hashmap invertedList.
       //invertedList.put(word, set1);
    }
       //TreeSet<Integer> set2 = new TreeSet<>(set1);
       invertedList.put(word,set1); 
}

public void termfrequency(String word, int docIndex) throws IOException, FileNotFoundException, ClassNotFoundException{
    
    HashMap<Integer, Integer> frequency = new HashMap<>();
    int count = 1;
    if(termFrequency.containsKey(word)){
        frequency = termFrequency.get(word);
        if(frequency.containsKey(docIndex)){
            count = frequency.get(docIndex);
            
            count += 1;
            
        }  
        
    }
    frequency.put(docIndex, count);
    termFrequency.put(word, frequency);
    
}
public void inverseTermFrequency() throws IOException, FileNotFoundException, ClassNotFoundException{
    

    termFrequency.entrySet().stream().map((entry) -> entry.getKey()).map((word) -> {
        length = termFrequency.get(word).size();
        //length = result.postingList(word);
        return word;
        }).forEach((word) -> {
            double idf = log(size/length);
            
            //if(!inverseFrequency.containsKey(word)){
            inverseFrequency.put(word, idf);
            //}
        });
    
}
public void termweight(){
    String word;
    
    
    for (Map.Entry<String, HashMap<Integer, Integer>> entry : termFrequency.entrySet()) {
        word = entry.getKey();
        HashMap<Integer, Integer> frequency  = new HashMap<>();
        HashMap<Integer, Double> frequency1 = new HashMap<>();
        frequency = termFrequency.get(word);
        
        for(Map.Entry<Integer, Integer> entry1 : frequency.entrySet()){
            double weight = entry1.getValue()*inverseFrequency.get(word);
            frequency1.put(entry1.getKey(),weight);
            termWeight.put(word, frequency1);
        }
        
        
    }
}
public void printMap() throws FileNotFoundException, IOException{               //function for printing hashmap of posting list.


    FileOutputStream fileOutputStream = new FileOutputStream("invertedIndex");
    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
        objectOutputStream.writeObject(invertedList);
        objectOutputStream.close();
    }
    
        try{
                PrintWriter writer;
                writer = new PrintWriter("output1.txt", "UTF-8");
                writer.println("Word     count");
                termFrequency.entrySet().stream().forEach((entry) -> {
                    writer.println(entry.getKey()+" :    "+entry.getValue());       //to get the output file of hashmap
                    //System.out.println(entry.getKey()+" : "+entry.getValue());
            });
                writer.close();
            } catch (IOException e) {
               
            }
        
       
        try{
                PrintWriter writer;
                writer = new PrintWriter("output3.txt", "UTF-8");
                writer.println("Word     count");
                termWeight.entrySet().stream().forEach((entry) -> {
                    writer.println(entry.getKey()+" :    "+entry.getValue());       //to get the output file of hashmap
                    //System.out.println(entry.getKey()+" : "+entry.getValue());
            });
                writer.close();
            } catch (IOException e) {
               
            }

}

public void Stopwordfile() throws FileNotFoundException, IOException
{
    File f = new File ("hindiStopWords.txt");         
    Scanner scan =new Scanner(f);                                               //Making a list of stopwords.
    while(scan.hasNext())
    {
        String word= scan.next();
        this.stopwordfile.add(word);
    }
}
                   
public boolean IsWordStopword(String word)
{                                                                               //to remove stopwords.
     return this.stopwordfile.contains(word);
}

   
public static void main(String args[]) throws FileNotFoundException, IOException, ClassNotFoundException{
   
    HashMap<String,Integer> map = new HashMap<>();                              //map for count the frequency of words.
    Word2 w = new Word2();
   // FileReader f1;
   //f1 = new FileReader("1.txt");
    
    HindiStemmer stemWord = new HindiStemmer();
    BufferedReader b1;
    File folder = new File("hindi/");                                           //make a folder to store all files.
    File[] listOfFiles = folder.listFiles();                                    //getting the list of all the files.
    size = listOfFiles.length;
    
    for (File f : listOfFiles)                                                  //loop for all the files
    {
        if (f.isFile()) 
        {
            w.createIndexOfFile(f.getName());                                   //make index of all files
            b1 =   new BufferedReader(new FileReader(f));
            //System.out.println(f);
            //int inside = 0;
            String line=b1.readLine();                                          //reading line by line
                while(line!=null)        
                { 
                    String a[]=line.split(" ");                                 //Spliting the line with spaces.
                    for(int i=0;i<a.length;i++)
                    {//if(a[i].length()>0)                                      // for counting total words in a document.
                            //count+=1;
                        if(!a[i].contains("date")){                             //to not read date tag and its content.
                            a[i] = w.validWord(a[i]);                           //for removing title and content english words and all other tags
                            a[i] = w.removePunctuationMarks(a[i]);              //for removing special characters.
                            if(!w.IsWordStopword(a[i]))
                            {
                                a[i] = stemWord.stemTheWord(a[i]);
                                if(!a[i].isEmpty())                             //For removing spaces
                                {
                                   w.makeIndex(a[i], w.fileIndexList.get(f.getName()));
                                   w.termfrequency(a[i],w.fileIndexList.get(f.getName()));
                                  
                                   if (map.containsKey(a[i])) 
                                   {
                                       map.put(a[i], map.get(a[i]) + 1);        //putting the string and it's frequency in hashmap if it already exist there.
                                   } 
                                   else 
                                   {
                                        map.put(a[i], 1);                       //if not then put that word with count 1.
                                   }
                                }
                            }
                        }
                    }
                    line=b1.readLine();
                }
                
                
                    //System.out.print("Total number of words "+count);
                b1.close();
        }
        //System.out.println(f);
    }

//    try{
//            PrintWriter writer;
//            writer = new PrintWriter("output.txt", "UTF-8");
//            writer.println("Word     count");
//            for (Map.Entry<String, Integer> entry : map.entrySet()) {
//                writer.println(entry.getKey()+" :    "+entry.getValue());       //to get the output file of hashmap
//                //System.out.println(entry.getKey()+" : "+entry.getValue());
//            }
//            writer.close();
//        } catch (IOException e) {
//           
//        }
            FileOutputStream fileOutputStream = new FileOutputStream("wordCount");          
         try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
             objectOutputStream.writeObject(map);
             objectOutputStream.close();
         }
            
            w.inverseTermFrequency();
            w.termweight();
            w.printMap();
            
            
            
            
            
}
}