package waterpunch.atamamozi_d.plugin.main;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import waterpunch.atamamozi_d.plugin.event.Event;
import waterpunch.atamamozi_d.plugin.menus.Menus;
import waterpunch.atamamozi_d.plugin.race.Race;
import waterpunch.atamamozi_d.plugin.race.Race_Core;
import waterpunch.atamamozi_d.plugin.race.Race_Mode;
import waterpunch.atamamozi_d.plugin.race.Race_Runner;
import waterpunch.atamamozi_d.plugin.race.checkpoint.CheckPointLoc;
import waterpunch.atamamozi_d.plugin.race.export.Hachitai;
import waterpunch.atamamozi_d.plugin.tool.CollarMessage;
import waterpunch.atamamozi_d.plugin.tool.Timers.Race_Timer;

public class Core extends JavaPlugin {

     static Plugin Data;
     public static int TIME;

     @Override
     public void onEnable() {
          System.out.println("ATAMAMOZI-D ENGINE START");

          saveDefaultConfig();
          getConfig();
          TIME = getConfig().getInt("Setting.CountDown.WAIT");

          Data = this;
          new Event(this);
          Main.loadconfig();
          for (Player p : this.getServer().getOnlinePlayers()) if (p.getOpenInventory().getTitle().equals("RACE_CREATE")) p.closeInventory();
     }

     @Override
     public void onDisable() {
          System.out.println("ATAMAMOZI-D ENGINE STOP");
          Race_Core.clear();
     }

     public static Plugin getthis() {
          return Data;
     }

     @SuppressWarnings("unlikely-arg-type")
     @Override
     public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
          if (!(cmd.getName().equalsIgnoreCase("atamamozi_d")) || !(sender instanceof Player)) return false;
          if (args.length == 0) {
               ((Player) sender).openInventory(Menus.getTop((Player) sender));
               return false;
          }
          Race_Runner run = null;
          switch (args[0]) {
               case "help":
                    onhelp((Player) sender);
                    break;
               // case "load":
               //      onload((Player) sender);
               //      break;
               // case "stop":
               //      onstop((Player) sender);
               //      break;
               case "list":
                    ((Player) sender).openInventory(Menus.getRaceList(((Player) sender)));
                    break;
               case "leave":
                    onleave((Player) sender);
                    break;
               case "create":
                    ((Player) sender).openInventory(Menus.getRaceCreate(((Player) sender)));
                    break;
               case "setName":
               case "setname":
                    if (args.length == 1) {
                         ((Player) sender).sendMessage(CollarMessage.setWarning() + "Need Name");
                         return false;
                    }
                    run = Race_Core.getRunner((Player) sender);
                    if (run.getMode() == Race_Mode.EDIT) {
                         if (args[1].equals("[ER]")) {
                              run.getPlayer().sendMessage(CollarMessage.setWarning() + "NG Word");
                              return false;
                         }
                         Race_Core.getRace(run.getRaceID()).setRace_name(args[1]);
                         run.getPlayer().openInventory(Menus.getRaceCreate(run.getPlayer()));
                         run.UpdateScoreboard();
                    }
                    break;
               case "addStartPoint":
               case "addstartpoint":
                    onaddStartpoint((Player) sender);
                    break;
               case "addCheckPoint":
               case "addcheckpoint":
                    if (args.length == 1) {
                         ((Player) sender).sendMessage(CollarMessage.setWarning() + "Please int");
                         return false;
                    }
                    onaddCheckpoint((Player) sender, args[1]);
                    break;
               case "start":
                    run = Race_Core.getRunner((Player) sender);
                    if (run == null) return false;
                    switch (run.getMode()) {
                         case EDIT:
                         case RUN:
                              return false;
                         case GOAL:
                              run.getPlayer().sendMessage(CollarMessage.setInfo() + "Not join the race");
                              return false;
                         case WAIT:
                              if (Race_Core.Timers.isEmpty()) new Race_Timer(5, run.getRaceID()).runTaskTimer(Core.getthis(), 0L, 20L);
                              if (!Race_Core.Timers.contains(run.getRaceID())) new Race_Timer(5, run.getRaceID()).runTaskTimer(Core.getthis(), 0L, 20L);
                              break;
                         default:
                    }
                    break;
               case "re":
               case "respawn":
                    onrespawn((Player) sender);
                    break;
               case "join":
                    onjoin((Player) sender, args[1]);
                    break;
               default:
                    onhelp((Player) sender);
                    break;
          }
          return false;
     }

     @Override
     public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
          if (args.length == 1 && cmd.getName().equalsIgnoreCase("atamamozi_d")) {
               ArrayList<String> subcmd = new ArrayList<String>();
               subcmd.add("help");
               subcmd.add("join");
               subcmd.add("start");
               subcmd.add("leave");
               subcmd.add("list");
               subcmd.add("create");
               subcmd.add("addStartPoint");
               subcmd.add("addCheckPoint");
               subcmd.add("setName");
               subcmd.add("respawn");
               return subcmd;
          }
          return null;
     }

     void onhelp(Player player) {
          player.sendMessage("---------------------");
          player.sendMessage("[help] this messeage");
          player.sendMessage("[list] /atamamozi_d list Open Race menu");
          player.sendMessage("[respawn] /atamamozi_d respawn :)");
          player.sendMessage("[leave] leave player");
          player.sendMessage("---------------------");
     }

     void onload(Player player) {}

     void onstop(Player player) {}

     void onleave(Player player) {
          Race_Core.removeRunner(player);
     }

     void onaddStartpoint(Player player) {
          Race_Runner run = Race_Core.getRunner(player);
          if (run == null || !(run.getMode() == Race_Mode.EDIT)) return;

          Race_Core.getRace(run.getRaceID()).addStartPointLoc(player.getLocation());
          player.sendMessage(CollarMessage.setInfo() + "Set Start Point");
          Hachitai.setCircle(run, player.getLocation(), 1);
          run.UpdateScoreboard();
     }

     void onaddCheckpoint(Player player, String r) {
          Race_Runner run = Race_Core.getRunner(player);
          if (run == null || !(run.getMode() == Race_Mode.EDIT)) return;
          try {
               if (Integer.parseInt(r) <= 0) {
                    player.sendMessage(CollarMessage.setWarning() + "Please enter Over 0");
                    return;
               }
               Race_Core.getRace(run.getRaceID()).addCheckPointLoc(player.getLocation(), Integer.parseInt(r));

               player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
               player.sendMessage(CollarMessage.setInfo() + "Set Check Point");
          } catch (NumberFormatException xr) {
               player.sendMessage(CollarMessage.setWarning() + "<" + ChatColor.RED + r + ChatColor.GOLD + "> is Not Number");
          }
          run.UpdateScoreboard();
     }

     void onsetCheckPoint(Player player, int r, int no) {
          Race_Runner run = Race_Core.getRunner(player);
          if (run == null || !(run.getMode() == Race_Mode.EDIT)) return;
          if (Race_Core.getRace(run.getRaceID()).getCheckPointLoc().size() == 0) {
               Race_Core.getRace(run.getRaceID()).addCheckPointLoc(player.getLocation(), r);
          } else {
               Race_Core.getRace(run.getRaceID()).getCheckPointLoc().set(no, new CheckPointLoc(player.getLocation(), r));
          }
          run.UpdateScoreboard();
     }

     void onrespawn(Player player) {
          Race_Runner run = Race_Core.getRunner(player);
          if (run == null || run.getMode() == Race_Mode.EDIT) {
               player.sendMessage(CollarMessage.setInfo() + "You not join race");
               return;
          }
          run.ReSpawn();
          return;
     }

     private void onjoin(Player player, String args) {
          Race race = Race_Core.getRace(args);
          if (race == null) return;
          Race_Core.joinRace(race, player);
     }

     void remCheckPoint(Player player, int no) {
          Race_Runner run = Race_Core.getRunner(player);
          if (run == null || !(run.getMode() == Race_Mode.EDIT)) return;
          Race_Core.getRace(run.getRaceID()).getCheckPointLoc().remove(no);
          run.UpdateScoreboard();
     }
}
