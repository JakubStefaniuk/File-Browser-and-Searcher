package filesearcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * user interface to access file browsing
 * 
 */
public class FileSearcher {
    public static void main(String[] args) {
        int size=0;
        byte choice;
        File pathFile;
        //basing on approximation of directory size, appropriate amount of 
        //file browsing threads is created, to enable faster concurrent searching
        System.out.println("Choose directory approx size: ");
        System.out.println("Small (1)");
        System.out.println("Medium (2)");
        System.out.println("Big (3)");
        System.out.print("Choice: ");
        Scanner s = new Scanner(System.in);
        String stringSize = s.nextLine();
        choice=(byte)Integer.parseInt(stringSize);
        switch(choice){
            case 1:
                size=40;
                break;
            case 2:
                size=200;
                break;
            case 3:
                size=500;
                break;
            default:
                System.out.println("Invalid choice");
        }
        //blocking queue to store files, which are put there by PathSearcher and
        //taken by FileBrowser
        BlockingQueue<File>bQueue=new ArrayBlockingQueue<File>(10);
        try{
            //artificial empty File to mark that queue is empty, as it is taken
            bQueue.put(new File("emptyPointer"));
        }
        catch(Exception a){
            System.out.println("Error occured in main thread");
            return;
        }
        System.out.println("Enter path to start with (or empty to start with current pwd)");
        Scanner reader = new Scanner(System.in);
        String fileName = reader.nextLine();
        if(fileName.equals("")){
            pathFile = new File(System.getProperty("user.dir"));
        }
        else{
            pathFile = new File(fileName);
        }
        System.out.print("Enter word to look for: ");
        String word= reader.nextLine();
        System.out.println();
        reader.close();
        //searching paths in second thread
        new Thread(new PathSearcher(bQueue,pathFile)).start();
        for(int i = 0; i < size; i++){
            //browsing files in next threads
            new Thread(new FileBrowser(bQueue,word)).start();
        }
    }
    
}
