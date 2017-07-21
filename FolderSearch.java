import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

class FolderSearch implements Callable <Integer>{
    private File directory;
    private int count;
    protected static volatile boolean flag = true; // если переменная принимает логическое значение false, исполнение потока прекращается

    public FolderSearch(File directory) {
        this.directory = directory;
    }

    public Integer call() { //возращает кол-во найденых файлов
        count = 0;
        try {
            if (directory.exists()) {
                File[] files = directory.listFiles();
                ArrayList<Future<Integer>> results = new ArrayList<>();
                for (File file : files)
                    if (file.isDirectory()) {
                        FolderSearch counter = new FolderSearch(file);
                        FutureTask <Integer> task = new FutureTask<>(counter);
                        results.add(task);
                        Thread t = new Thread(task);
                        t.start();
                    } else {
                        if (flag) {
                            count++;
                        } else {
                            return count;
                        }
                    }
                for (Future<Integer> result :results)
                    try {
                        count += result.get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
            } else {
                System.out.println("Directory isn't found...");
            }
        }
        catch (InterruptedException e) {
        }
        return count;
    }
}