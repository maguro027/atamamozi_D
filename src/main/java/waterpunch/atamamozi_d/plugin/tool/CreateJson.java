package waterpunch.atamamozi_d.plugin.tool;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import waterpunch.atamamozi_d.plugin.main.Main;
import waterpunch.atamamozi_d.plugin.menus.Menus;
import waterpunch.atamamozi_d.plugin.race.Race;
import waterpunch.atamamozi_d.plugin.race.Race_Core;
import waterpunch.atamamozi_d.plugin.race.Race_Mode;
import waterpunch.atamamozi_d.plugin.race.Race_Runner;

public class CreateJson {

     public static final File file_Race = new File(new File("").getAbsolutePath().toString() + "/plugins/Atamamozi_D/Races/");

     public static void createfile(String string) {
          try {
               Files.createFile(Paths.get(string));
          } catch (IOException e) {
               e.printStackTrace();
          }
     }

     public static void saveRace(Player player) {
          Race_Runner run = Race_Core.getRunner(player);
          if (run == null || !(run.getMode() == Race_Mode.EDIT)) return;
          if (!(Race_Core.getRace(run.getRaceID()).getErrorCount() == 0)) {
               player.openInventory(Menus.getRaceCreate(player));
               return;
          }
          Race_Core.getRace(run.getRaceID()).setMode(Race_Mode.WAIT);
          if (!(file_Race.exists())) file_Race.mkdir();
          String URL = file_Race + "/" + Race_Core.getRace(run.getRaceID()).getRace_name() + ".json";
          Main.createfile(URL);
          Race_Core.Race_list.add(Race_Core.getRace(run.getRaceID()));
          try (Writer writer = new FileWriter(URL)) {
               Gson gson = new Gson();
               gson.toJson(Race_Core.getRace(run.getRaceID()), writer);

               Race_Core.getRace(run.getRaceID()).setMode(Race_Mode.WAIT);

               player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
               player.sendMessage(CollarMessage.setInfo() + "Race Create Complete!!");
               System.out.println(CollarMessage.setInfo() + player.getName() + "is Race Create");
               System.out.println(CollarMessage.setInfo() + "NAME :" + Race_Core.getRace(run.getRaceID()).getRace_name());
               Race_Core.removeRunner(player);
               player.closeInventory();
          } catch (IOException e) {
               e.printStackTrace();
          }
     }

     public static void save(Race race) {
          if (!(file_Race.exists())) file_Race.mkdir();
          String URL = file_Race + "/" + race.getRace_name() + ".json";
          Main.createfile(URL);
          try (Writer writer = new FileWriter(URL)) {
               Gson gson = new Gson();
               gson.toJson(race, writer);
               race.setMode(Race_Mode.WAIT);
          } catch (IOException e) {
               e.printStackTrace();
          }
     }
}
