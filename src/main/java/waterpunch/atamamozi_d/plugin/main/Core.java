package waterpunch.atamamozi_d.plugin.main;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import waterpunch.atamamozi_d.plugin.event.Event;
import waterpunch.atamamozi_d.plugin.race.Race_Runner;
import waterpunch.atamamozi_d.plugin.race.checkpoint.CheckPointLoc;
import waterpunch.atamamozi_d.plugin.tool.CountDownTimer;
import waterpunch.atamamozi_d.plugin.tool.Race_Mode;

public class Core extends JavaPlugin {

     @Override
     public void onEnable() {
          System.out.println("ATAMAMOZI-D ENGINE START");
          new Event(this);
          new CountDownTimer(this);
          waterpunch.atamamozi_d.plugin.main.Main.loadconfig();

          for (Player p : this.getServer().getOnlinePlayers()) if (p.getOpenInventory().getTitle().equals("RACE_CREATE")) p.closeInventory();
     }

     @Override
     public void onDisable() {
          System.out.println("ATAMAMOZI-D ENGINE STOP");
          waterpunch.atamamozi_d.plugin.race.Race_Core.clear();
     }

     @Override
     public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
          if (!(cmd.getName().equalsIgnoreCase("atamamozi_d")) || !(sender instanceof Player) || !(sender.isOp())) return false;
          if (args.length == 0) {
               ((Player) sender).openInventory(waterpunch.atamamozi_d.plugin.menus.Menus.getTop((Player) sender));
               return false;
          }
          Race_Runner run = null;
          switch (args[0]) {
               case "help":
                    onhelp((Player) sender);
                    break;
               case "load":
                    onload((Player) sender);
                    break;
               case "stop":
                    onstop((Player) sender);
                    break;
               case "leave":
                    onleave((Player) sender);
                    break;
               case "create":
                    oncreate((Player) sender);
                    break;
               case "setName":
                    if (args.length == 1) {
                         ((Player) sender).sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setWarning() + "Need Name");
                         return false;
                    }
                    run = waterpunch.atamamozi_d.plugin.race.Race_Core.getRuner((Player) sender);
                    if (run.getMode() == Race_Mode.EDIT) {
                         if (args[1].equals("[ER]")) {
                              run.getPlayer().sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setWarning() + "NG Word");
                              return false;
                         }
                         run.getRace().setRace_name(args[1]);
                         run.getPlayer().openInventory(waterpunch.atamamozi_d.plugin.menus.Menus.getRaceCreate(run.getPlayer()));
                         run.UpdateScoreboard();
                    }
                    break;
               case "addStartPoint":
                    onaddStartpoint((Player) sender);
                    break;
               case "addCheckPoint":
                    if (args.length == 1) {
                         ((Player) sender).sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setWarning() + "Please int");
                         return false;
                    }
                    onaddCheckpoint((Player) sender, args[1]);
                    break;
               case "updateCheckPoint":
                    if (args.length == 2) {
                         ((Player) sender).sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setWarning() + "Please int");
                         return false;
                    }
                    onupdatecheckpoint((Player) sender, args[1], args[2]);
                    break;
               case "start":
                    run = waterpunch.atamamozi_d.plugin.race.Race_Core.getRuner((Player) sender);
                    if (run.getMode() == Race_Mode.GOAL) {
                         run.getPlayer().sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setInfo() + "Not join the race");
                         return false;
                    }
                    waterpunch.atamamozi_d.plugin.race.Race_Core.Race_Start(run.getRace());
                    break;
               case "re":
               case "respawn":
                    onrespawn((Player) sender);
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
               subcmd.add("load");
               subcmd.add("stop");
               subcmd.add("leave");
               subcmd.add("create");
               subcmd.add("addStartPoint");
               subcmd.add("addCheckPoint");
               subcmd.add("updateCheckPoint");
               subcmd.add("start");
               subcmd.add("respawn");

               return subcmd;
          }
          return null;
     }

     void onhelp(Player player) {
          player.sendMessage("---------------------");
          player.sendMessage("[help] this messeage");
          player.sendMessage("[load] /waterpunch.atamamozi_d load 'race name'");
          player.sendMessage("[stop] /waterpunch.atamamozi_d stop 'race name' race stop");
          player.sendMessage("[leave] leave player");
          player.sendMessage("---------------------");
     }

     void onload(Player player) {}

     void onstop(Player player) {}

     void onleave(Player player) {
          waterpunch.atamamozi_d.plugin.race.Race_Core.removeRunner(player);
     }

     void oncreate(Player player) {
          player.openInventory(waterpunch.atamamozi_d.plugin.menus.Menus.getRaceCreate(player));
     }

     void onaddStartpoint(Player player) {
          Race_Runner run = waterpunch.atamamozi_d.plugin.race.Race_Core.getRuner(player);
          if (run == null || !(run.getMode() == Race_Mode.EDIT)) return;

          run.getRace().addStartPointLoc(player.getLocation());
          player.sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setInfo() + "Set Start Point");
          waterpunch.atamamozi_d.plugin.race.export.Hachitai.setCircle(player.getLocation(), 1);
          run.UpdateScoreboard();
     }

     void onaddCheckpoint(Player player, String r) {
          Race_Runner run = waterpunch.atamamozi_d.plugin.race.Race_Core.getRuner(player);
          if (run == null || !(run.getMode() == Race_Mode.EDIT)) return;
          try {
               if (Integer.parseInt(r) <= 0) {
                    player.sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setWarning() + "Please enter Over 0");
                    return;
               }
               run.getRace().addCheckPointLoc(player.getLocation(), Integer.parseInt(r));

               player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
               player.sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setInfo() + "Set Check Point");
          } catch (NumberFormatException xr) {
               player.sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setWarning() + "<" + ChatColor.RED + r + ChatColor.GOLD + "> is Not Number");
          }
          run.UpdateScoreboard();
     }

     void onsetCheckPoint(Player player, int r, int no) {
          Race_Runner run = waterpunch.atamamozi_d.plugin.race.Race_Core.getRuner(player);
          if (run == null || !(run.getMode() == Race_Mode.EDIT)) return;
          if (run.getRace().getCheckPointLoc().size() == 0) {
               run.getRace().addCheckPointLoc(player.getLocation(), r);
          } else {
               run.getRace().getCheckPointLoc().set(no, new CheckPointLoc(player.getLocation(), r));
          }
          run.UpdateScoreboard();
     }

     void onupdatecheckpoint(Player player, String r, String No) {
          Race_Runner run = waterpunch.atamamozi_d.plugin.race.Race_Core.getRuner(player);
          if (run == null || !(run.getMode() == Race_Mode.EDIT)) return;
          try {
               if (Integer.parseInt(r) <= 0) {
                    player.sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setWarning() + "Please enter Over 0");
                    return;
               }

               if (run.getRace().getCheckPointLoc().size() > Integer.parseInt(No)) {
                    player.sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setInfo() + "Check Point<" + ChatColor.RED + r + ChatColor.GOLD + "> Up Data");
                    onsetCheckPoint(player, Integer.parseInt(r), Integer.parseInt(No));
                    if (run.getRace().getCheckPointLoc().size() == 0) {
                         run.getRace().addCheckPointLoc(player.getLocation(), Integer.parseInt(r));
                    } else {
                         run.getRace().getCheckPointLoc().set(Integer.parseInt(No), new CheckPointLoc(player.getLocation(), Integer.parseInt(r)));
                    }
               } else {
                    player.sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setWarning() + "<" + ChatColor.RED + r + ChatColor.GOLD + "> is Over Number");
               }
          } catch (NumberFormatException xr) {
               player.sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setWarning() + "<" + ChatColor.RED + r + "or" + No + ChatColor.GOLD + "> is Not Number");
          }
          run.UpdateScoreboard();
     }

     void onrespawn(Player player) {
          Race_Runner run = waterpunch.atamamozi_d.plugin.race.Race_Core.getRuner(player);
          if (run == null || !(run.getMode() == Race_Mode.EDIT)) {
               player.sendMessage(waterpunch.atamamozi_d.plugin.tool.CollarMessage.setInfo() + "You not join race");
               return;
          }
          run.ReSpawn();
          return;
     }

     void remCheckPoint(Player player, int no) {
          Race_Runner run = waterpunch.atamamozi_d.plugin.race.Race_Core.getRuner(player);
          if (run == null || !(run.getMode() == Race_Mode.EDIT)) return;
          run.getRace().getCheckPointLoc().remove(no);
          run.UpdateScoreboard();
     }
}
