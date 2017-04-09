package retrieval;
import java.io.*;
import static java.lang.Math.ceil;
import static java.lang.Math.log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;



public class Retrieval{
    HashMap <File, Integer>  fileIndexList= new HashMap<>();                   //map for storing the index of files
    int indexOfFile=1;
    HashMap<String, HashMap<Integer, Integer>> termFrequency = new HashMap<>();
    HashMap<String,Integer> map = new HashMap<>();                              //map for count the frequency of words.
    Map<String, Set<Integer>> invertedList = new HashMap <>();              //Map for storing the word and their posting list
    HashSet<String> stopwordfile = new HashSet<>();
    public static double size =0;
    Search result = new Search();
    //termWeight tw ;
    //termFrequency tf = new termFrequency();
    
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
public void createIndexOfFile(File file) throws IOException, FileNotFoundException, ClassNotFoundException{                                     //function for creating index of all files.
    fileIndexList.put(file,indexOfFile); 
    
    readFile(file);
    if(indexOfFile%2000==0) {
        System.out.println(file+"---"+indexOfFile);
    }
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
    //System.out.println("hello");
}
    
    public double inverseTermFrequency(String word) throws IOException, FileNotFoundException, ClassNotFoundException{
        double length =0;
        //Retrieval r = new Retrieval();
        length = termFrequency.get(word).size();
        double idf = log(size/length);
        return idf;
   
}

public void printMap() throws FileNotFoundException, IOException{               //function for printing hashmap of posting list.

    
    FileOutputStream fileOutputStream = new FileOutputStream("invertedIndex");
    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
        objectOutputStream.writeObject(invertedList);
        objectOutputStream.close();
    }
    //System.out.println("Heyyy");
    
    FileOutputStream fos = new FileOutputStream("termfrequency");
    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos)) {
        objectOutputStream.writeObject(termFrequency);
        objectOutputStream.close();
    }
//        try{
//                PrintWriter writer;
//                writer = new PrintWriter("termfrequency.txt", "UTF-8");
//                writer.println("Word     count");
//                termFrequency.entrySet().stream().forEach((entry) -> {
//                    writer.println(entry.getKey()+" :    "+entry.getValue());       //to get the output file of hashmap
//                    //System.out.println(entry.getKey()+" : "+entry.getValue());
//            });
//                writer.close();
//            } catch (IOException e) {
//               
//            }
}

public void Stopwordfile() throws FileNotFoundException, IOException
{
    File f = new File ("English Stopwords.txt");         
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

public void showFiles(File[] files) throws FileNotFoundException, IOException, ClassNotFoundException {
    for (File file : files) {
        if (file.isDirectory()) {
            //System.out.println("Directory---"+file.getName());
            showFiles(file.listFiles()); // Calls same method again.
            
        } else {
            
            size+=1;
            createIndexOfFile(file);                                   //make index of all files
          
        }
    }
    
}
  
public void readFile(File f) throws FileNotFoundException, IOException, ClassNotFoundException{
    
    
    //EnglishStemmer stemWord = new EnglishStemmer();
    PorterStemmer stemWord = new PorterStemmer();
    char [] stringToCharArray = new char[10000];
    BufferedReader b1;
    FileReader fr = new FileReader(f);
    b1 =   new BufferedReader(fr);
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
                            a[i] = validWord(a[i]);                           //for removing title and content english words and all other tags
                            a[i] = removePunctuationMarks(a[i]);              //for removing special characters.
                            if(!IsWordStopword(a[i]))
                            {
                                stringToCharArray = a[i].toCharArray();
                                for(char ch =0; ch<stringToCharArray.length;ch++){
                                  //System.out.println(stringToCharArray[ch]);
                                stemWord.add(stringToCharArray[ch]);
                                }
                                stemWord.stem();
                                a[i] = stemWord.toString();
                                if(!a[i].isEmpty())                             //For removing spaces
                                {
                                  makeIndex(a[i], fileIndexList.get(f));
                                  //System.out.println("Heyyy");
                                  //termfrequency(a[i],fileIndexList.get(f));
                                  
//                                   if (map.containsKey(a[i])) 
//                                   {
//                                       map.put(a[i], map.get(a[i]) + 1);        //putting the string and it's frequency in hashmap if it already exist there.
//                                   } 
//                                   else 
//                                   {
//                                        map.put(a[i], 1);                       //if not then put that word with count 1.
//                                   }
                                }
                            }
                        }
                    }
                    line=b1.readLine();
                }
                
                    //System.out.print("Total number of words "+count);
                b1.close();
                
                
}
public static void main(String args[]) throws FileNotFoundException, IOException, ClassNotFoundException{
   
    
   // FileReader f1;
   //f1 = new FileReader("1.txt");
    Retrieval w = new Retrieval();
    //w.tw = new termWeight();
    File folder = new File("hindi/");                                           //make a folder to store all files.
    File[] listOfFiles = folder.listFiles();                                    //getting the list of all the files.
    w.showFiles(listOfFiles);
    System.out.println(size+"docments size");
    
//    for (Map.Entry<File,Integer> entry :w.fileIndexList.entrySet()) {
//        System.out.println(entry.getKey());
//        
//    }
//   
    
//    FileOutputStream fileOutputStream = new FileOutputStream("wordcount");
//    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
//        objectOutputStream.writeObject(w.map);
//        objectOutputStream.close();
//    }
//    try{
//                PrintWriter writer;
//                writer = new PrintWriter("wordcount.txt", "UTF-8");
//                writer.println("Word     count");
//                w.map.entrySet().stream().forEach((entry) -> {
//                    writer.println(entry.getKey()+" :    "+entry.getValue());       //to get the output file of hashmap
//                    //System.out.println(entry.getKey()+" : "+entry.getValue());
//            });
//                writer.close();
//            } catch (IOException e) {
//               
//            }
          
        ///System.out.println("hello");
             w.printMap();       

}
}