public class Main {
    public static void main(String[] args) {

        /*
        Trying threads without threadpool etc.- v1 of code

        FileHandler fileHandler1 = new FileHandler("filename.txt");
        FileHandler fileHandler2 = new FileHandler("filename2.txt");
        FileHandler fileHandler3 = new FileHandler("filename3.txt");
        fileHandler1.start();
        fileHandler2.start();
        fileHandler3.start();

         */

        //Instancing mine ThreadHandler class which is responsible for managing threads

        ThreadHandler threadHandler = new ThreadHandler("Folder");
        threadHandler.start();
    }
}