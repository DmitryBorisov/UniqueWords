import java.io.*;
import java.util.concurrent.*;


public class FileReader {

    //  ConcurrentHashMap<String,String> map
    boolean readFile(String fileName, ConcurrentHashMap<String,String> hhmap) {
        boolean uniqueFlag = true;
        try (BufferedReader bfReader = new BufferedReader
                (new InputStreamReader(new FileInputStream(fileName), "UTF-8")))
        {
            String line;

           // while (bfReader.ready()) {
            while ((line=bfReader.readLine())!=null // null - конец файла
                    && uniqueFlag                   // не найдено повторов
                    && (!UniqueWords.goDown.get())) // не поднят глобальный флаг завершения процессов
            {
                /*
                \p -
                {Punct} -
                \s -  Любой символ-разделитель (иначе, [\n\t\f\r])
                ^\w - Любой знак, отличный от буквы или цифры
                 */
                for (String word : line.split("[\\p{Punct}\\s^\\w-«»]")) {
                    if ((!word.isEmpty()) && (word.length()>1)) {
                        word=word.toLowerCase();
                        if (!hhmap.contains(word)){  // если в HashMap нет такого слова
                            hhmap.put(word,word);    // заносим его туда
                        }
                        else {
                            System.out.println("Слово \"" + word + "\", найденное в файле "
                                            + fileName+ ",   ранее уже встречалось.");
                            uniqueFlag = false;  // ставим флаг "найдено неуникальное слово"
                            UniqueWords.goDown.set(true); // ставим флаг "все потоки завершить!"
                            break;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден : "+fileName);

        }catch (IOException e) {
            e.printStackTrace();
        }
        UniqueWords.th_counter.decrementAndGet();
        return uniqueFlag;
  } // readList()

} // FileReader
