package waterpunch.atamamozi_d.plugin.main;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import waterpunch.atamamozi_d.plugin.race.Race;

public class Main {

     public static final File file_ = new File(new File("").getAbsolutePath().toString() + "/plugins/Atamamozi_D/Race/");

     public static int fil_count = 0;

     public static void loadconfig() {
          file_.mkdirs();
          File[] targetFile_dir_list = new File(file_.toString()).listFiles();

          if (targetFile_dir_list == null) return;

          getRaces();
     }

     public static void getRaces() {
          int fil_count = 0;
          File[] files = waterpunch.atamamozi_d.plugin.tool.CreateJson.file_.listFiles();
          if (files == null) return;
          for (File tmpFile : files) {
               if (tmpFile.isDirectory()) {
                    getRaces();
               } else {
                    if (tmpFile.getName().substring(tmpFile.getName().lastIndexOf(".")).equals(".json")) {
                         fil_count++;
                         try (FileReader fileReader = new FileReader(tmpFile)) {
                              Gson gson = new Gson();
                              waterpunch.atamamozi_d.plugin.race.Race_Core.Race_list.add(gson.fromJson(fileReader, Race.class));
                              System.out.println(tmpFile.getName());
                         } catch (JsonSyntaxException | JsonIOException | IOException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                         }
                    }
               }
          }
          System.out.println(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setInfo() + fil_count + " Race Load");
     }

     public static void createfile(String string) {
          try {
               Files.createFile(Paths.get(string));
          } catch (IOException e) {}
     }

     public static void saveconfig(String string) {
          try {
               FileWriter writer = new FileWriter(file_ + "/race_list.json");

               writer.write(string);
               writer.close();
          } catch (IOException e) {
               e.printStackTrace();
          }
     }
}
