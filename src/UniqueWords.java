import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class UniqueWords {
    static AtomicBoolean goDown = new AtomicBoolean(); // глобальный флаг завершения процессов
    static AtomicInteger th_counter = new AtomicInteger(0); // счетчик запущенных процессов
    public static void main(String[] args) {
        TaskListReader taskListReader = new TaskListReader("tasks.txt");
        ArrayList<String> taskList = new ArrayList<>();
        taskList = taskListReader.readList(); // список файлов на обработку
        ConcurrentHashMap<String,String> hmap =
                new ConcurrentHashMap<>(2048);


        System.out.println("Список файлов для анализа :");
        ArrayList<String> checkedList = new ArrayList<>();
        for (String s : taskList) {
                File file = new File(s);
                if (file.exists()){
                    System.out.println(s+ " ok!");
                    checkedList.add(s);
                } else {
                    System.out.println(s + " - не найден !");
                }
        }
        taskList = checkedList;

        long t_start,t_finish;
        t_start = System.currentTimeMillis();

// - - -  создаем список потоков по списку файлов

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        goDown.set(false);
        for (String tsk : taskList) {

                executorService.submit(new Runnable() {

                    public void run() {
                        FileReader fr = new FileReader();
                        if (!fr.readFile(tsk,hmap)) {
                            System.out.println("В файле "+tsk+" найдено неуникальное слово! \n" +
                                                "Завершение программы.");
                        }

                        executorService.shutdown();
                    }
                });
            th_counter.incrementAndGet(); // уменьшаем счетчик потоков
            }
// - - -

        while (th_counter.get()>0){} // ждем окончания работы потоков
        t_finish = System.currentTimeMillis();
        System.out.println("Время работы : " + (t_finish-t_start)+" мс");
        // readFile возвращает true, если в файле содержится хотя бы одно неуникальное слово

        // для создания тестовых файлов (только уникальные слова)
        /*
        System.out.println("-----------------------------");
        for (Map.Entry<String, String> entry : hmap.entrySet()) {
            System.out.println(entry.getKey());
        }
        */

    }
}