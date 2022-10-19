import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadHandler {

    private File folder;

    public ThreadHandler(String folderPath){
        this.folder = new File(folderPath);
    }

    //"Fetching" all files in given folder into an arraylist
    private ArrayList<File> filesInFolder(){
        ArrayList<File> fileArrayList = new ArrayList<>();
        for(File fileEntry : folder.listFiles()){
            if(fileEntry.isFile()){
                fileArrayList.add(fileEntry);
            }
        }
        return fileArrayList;
    }

    //
    public void start(){
        ArrayList<File> filesInFolder = filesInFolder();
        Integer numberOfFiles = filesInFolder.size();
        ExecutorService executorService = Executors.newFixedThreadPool(filesInFolder.size());
        for(File fileEntry : filesInFolder){
            FileHandler fileHandler = new FileHandler(fileEntry.getPath());
            executorService.execute(fileHandler);
        }
        executorService.shutdown();
        while(!executorService.isTerminated()){}
        System.out.println("Finished all threads !");
    }
}
