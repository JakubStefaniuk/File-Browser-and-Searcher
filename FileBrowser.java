package filesearcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

//class used to browse files from a blocking queue for a given string

public class FileBrowser implements Runnable{
    //blocking queue storing files
    private BlockingQueue<File>files;
    //string typed by user, that thread is looking for
    private String wordName;
    public FileBrowser(BlockingQueue<File>files,String wordName){
        this.files=files;
        this.wordName=wordName;
    }
    @Override
    public void run() {
        //if next read line contains wordName, occured is incremented
        int occured = 0;
        //flag saying, that queue is empty if true
        boolean endOfTask=false;
        while(!endOfTask){
            try{
                File taken = files.take();
                //having taken emptyPointer, it is put back and flag is set to true
                if(taken.equals(new File("emptyPointer"))){
                    endOfTask=true;
                    files.put(new File("emptyPointer"));
                }
                else{
                    //searching file for wordName
                    Scanner lineReader  = new Scanner(new BufferedReader(new FileReader(taken)));
                    occured=0;
                    while(lineReader.hasNext()){
                         String phrase = lineReader.next();
                         if(phrase.contains(wordName)){
                             occured++;
                         }
                    }
                    if(occured!=0){
                        System.out.println("\"" + wordName + "\"" + " occured " + occured + " times in file: " + taken.getAbsolutePath());
                    }
                }
            }
            catch(Exception a){
                System.out.println("Error occured in file browser..");
                return;
            }
        }
    }
    
}
