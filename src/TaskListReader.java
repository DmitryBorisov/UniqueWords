import java.io.*;
import java.util.ArrayList;

class TaskListReader {
    static private String listFileName;

    TaskListReader(String taskFileName) {
        listFileName = taskFileName;
    }

    public  ArrayList<String> readList() {
            ArrayList<String> taskList = new ArrayList<>();
            String line;
            try (BufferedReader bfReader = new BufferedReader
                    (new InputStreamReader(new FileInputStream(listFileName), "UTF-8")))
            {
                while (bfReader.ready()) {
                    line=bfReader.readLine();
                    if (!line.isEmpty()) {
                        taskList.add(line);
                    }
                }
            } catch (NullPointerException e){
                e.printStackTrace();
            } catch (FileNotFoundException e){
                System.out.println("Файл списка задач не найден : "+listFileName);
                //e.printStackTrace();
            } catch (IOException e)  {
                e.printStackTrace();
            }
            return taskList;
        } // readList()

} // TaskListReader
