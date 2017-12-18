import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class UniqueWords {
    static AtomicBoolean goDown = new AtomicBoolean(); // глобальный флаг завершения процессов
    public static void main(String[] args) {
        AtomicBoolean global_unique_flag = new AtomicBoolean();
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
        global_unique_flag.set(true);
// - - -  создаем список потоков по списку файлов

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        goDown.set(false);
        for (String tsk : taskList) {

                executorService.submit(new Runnable() {

                    public void run() {
                        FileReader fr = new FileReader();
                        // readFile возвращает true, если в файле содержится
                        // хотя бы одно неуникальное слово
                        if (!fr.readFile(tsk,hmap)) {
                            System.out.println("В файле "+tsk+" найдено неуникальное слово! \n" +
                                                "Завершение программы.");
                            global_unique_flag.set(false);
                        }
                        executorService.shutdown();
                    }
                });
            }
// - - -
        while (!executorService.isTerminated());// ждем окончания работы потоков
        t_finish = System.currentTimeMillis();
        if (global_unique_flag.get()) {
            System.out.println("В предоставленных файлах повторяющихся слов нет");
        } 
        System.out.println("Время работы : " + (t_finish-t_start)+" мс");
    }
}
