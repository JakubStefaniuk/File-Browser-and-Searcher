package filesearcher;

import java.io.File;
import java.util.*;
import java.util.concurrent.BlockingQueue;

//class used to add files to blocking queue

public class PathSearcher implements Runnable{
    //blocking queue, to which new paths are added
    private BlockingQueue<File>paths;
    File startingPath;
    public PathSearcher(BlockingQueue<File>paths,File startingPath){
        this.paths=paths;
        this.startingPath=startingPath;
    }
    @Override
    public void run() {
        try {
            findPathsAddToQueue(startingPath);
            paths.put(new File("emptyPointer"));
        } catch (InterruptedException ex) {
            System.out.println("Error occured in path searcher..");
            return;
        }
                 
    }
    public void findPathsAddToQueue(File startingPath) throws InterruptedException{
        //all available files
        File [] newPaths =  startingPath.listFiles();
       //if path is directory, it is searched recursively, otherwise it is added
       //to blocking queue
        for(int i = 0; i < newPaths.length; i++){
            if(newPaths[i].isDirectory()){
                findPathsAddToQueue(newPaths[i]);
            }
            else{
                paths.put(newPaths[i]);
            }
        }
    }
    
}
