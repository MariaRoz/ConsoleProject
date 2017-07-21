import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ConsoleApp {
    public static void main(String[] args) throws IOException {
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(args[0])); // чтение файла,который задается первым аргументом командной строки
        ArrayList<String> list = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            list.add(line);
        }
        Killer killer = new Killer(); //создаем поток,который отменяет запущенные поиски нажатием клавиши esc
        killer.start();
        FileWriter fw = new FileWriter(args[1]);   // создаем файл
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.print("");
        for (int i = 0; i < list.size(); i++) {
            File Directory = new File(list.get(i));
            FolderSearch counter = new FolderSearch(Directory);
            FutureTask <Integer> task = new FutureTask<>(counter);
            Thread t = new Thread(task);
            t.start();
            try {
                int k = i + 1;
                System.out.println(k + "\t" + task.get() + "\t" + Directory); //выводим на экран
                out.println( list.get(i)+ ";" + task.get()); //записываем в файл
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
            }
        }
        out.close();
        if (killer.isAlive()) {
            System.out.println("Press any key to continue...");
        }
    }

    public static void interruptT() { // метод меняющий значение флаговой переменной
        FolderSearch.flag = false;
    }
}