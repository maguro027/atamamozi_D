package waterpunch.atamamozi_d.plugin.event;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.Plugin;
import waterpunch.atamamozi_d.plugin.menus.Menus;
import waterpunch.atamamozi_d.plugin.menus.Race_List;
import waterpunch.atamamozi_d.plugin.race.Race;
import waterpunch.atamamozi_d.plugin.race.Race_Core;
import waterpunch.atamamozi_d.plugin.race.Race_Mode;
import waterpunch.atamamozi_d.plugin.race.Race_Runner;
import waterpunch.atamamozi_d.plugin.race.Race_Type;
import waterpunch.atamamozi_d.plugin.race.export.Hachitai;
import waterpunch.atamamozi_d.plugin.tool.CollarMessage;
import waterpunch.atamamozi_d.plugin.tool.CreateJson;
import waterpunch.atamamozi_d.plugin.tool.Location.Loc_parts;

public class Event implements Listener {

     Plugin plugin_data = null;

     public Event(Plugin plugin) {
          plugin.getServer().getPluginManager().registerEvents(this, plugin);
          plugin_data = plugin;
     }

     @EventHandler
     public void onInventoryClickEvent(InventoryClickEvent event) {
          if (event.getInventory().toString().matches(".*" + "Custom" + ".*") && event.getInventory().getType() == InventoryType.CHEST) {
               Race_Runner run = Race_Core.getRunner((Player) event.getWhoClicked());
               if (event.getAction() != InventoryAction.PICKUP_ALL) return;
               switch (((Player) event.getWhoClicked()).getOpenInventory().getTitle().toString()) {
                    case "RACE_TOP_MENU":
                         event.setCancelled(true);
                         if (event.getRawSlot() == 1) ((Player) event.getWhoClicked()).openInventory(Race_List.getMenu((Player) event.getWhoClicked()));
                         if (event.getRawSlot() == 7) ((Player) event.getWhoClicked()).openInventory(Menus.getRaceCreate(((Player) event.getWhoClicked())));
                         break;
                    case "RACE_LIST":
                         event.setCancelled(true);
                         if (event.getRawSlot() == 45) ((Player) event.getWhoClicked()).openInventory(Menus.getTop((Player) event.getWhoClicked()));
                         if (event.getRawSlot() == 49) ((Player) event.getWhoClicked()).openInventory(Race_List.getMenu((Player) event.getWhoClicked()));

                         if (event.getRawSlot() >= 9 && event.getRawSlot() < 45) { //36
                              if (event.getCurrentItem() == null) return;
                              Race_Core.joinRace(Race_Core.getRace(event.getCurrentItem().getItemMeta().getDisplayName()), (Player) event.getWhoClicked());
                              ((Player) event.getWhoClicked()).closeInventory();
                         }
                         return;
                    case "RACE_EDIT":
                         event.setCancelled(true);
                         if (event.getRawSlot() == 45) ((Player) event.getWhoClicked()).openInventory(Menus.getTop((Player) event.getWhoClicked()));
                         break;
                    case "RACE_CREATE":
                         event.setCancelled(true);
                         if (event.getRawSlot() == 45) ((Player) event.getWhoClicked()).openInventory(Menus.getTop((Player) event.getWhoClicked()));
                         if (event.getRawSlot() == 12) ((Player) event.getWhoClicked()).openInventory(Menus.getRaceType(((Player) event.getWhoClicked())));
                         if (event.getRawSlot() == 14) ((Player) event.getWhoClicked()).openInventory(Menus.getRaceAmount(((Player) event.getWhoClicked())));
                         if (event.getRawSlot() == 16) ((Player) event.getWhoClicked()).openInventory(Menus.getRaceRap(((Player) event.getWhoClicked())));
                         if (event.getRawSlot() == 28) ((Player) event.getWhoClicked()).openInventory(Menus.getRaceIcon(((Player) event.getWhoClicked())));
                         if (event.getRawSlot() == 30) ((Player) event.getWhoClicked()).openInventory(Menus.getRaceStartPoint(((Player) event.getWhoClicked())));
                         if (event.getRawSlot() == 32) ((Player) event.getWhoClicked()).openInventory(Menus.getRaceCheckPoint(((Player) event.getWhoClicked())));
                         if (event.getRawSlot() == 49) CreateJson.saveRace((Player) event.getWhoClicked());
                         break;
                    case "RACE_CREATE_TYPE":
                         event.setCancelled(true);
                         if (event.getRawSlot() == 45) ((Player) event.getWhoClicked()).openInventory(Menus.getRaceCreate((Player) event.getWhoClicked()));
                         if (event.getRawSlot() == 20 || event.getRawSlot() == 29) {
                              Race_Core.getRace(run.getRaceID()).setRace_Type(Race_Type.WALK);
                              ((Player) event.getWhoClicked()).openInventory(Menus.getRaceType(((Player) event.getWhoClicked())));
                              Race_Core.getRunner((Player) event.getWhoClicked()).UpdateScoreboard();
                         } else if (event.getRawSlot() == 24 || event.getRawSlot() == 33) {
                              Race_Core.getRace(run.getRaceID()).setRace_Type(Race_Type.BOAT);
                              ((Player) event.getWhoClicked()).openInventory(Menus.getRaceType(((Player) event.getWhoClicked())));
                              Race_Core.getRunner((Player) event.getWhoClicked()).UpdateScoreboard();
                         }
                         break;
                    case "RACE_CREATE_RAP":
                         event.setCancelled(true);
                         if (event.getRawSlot() == 45) ((Player) event.getWhoClicked()).openInventory(Menus.getRaceCreate((Player) event.getWhoClicked()));
                         if (event.getRawSlot() == 20) {
                              Race_Core.getRace(run.getRaceID()).setRap(Race_Core.getRace(run.getRaceID()).getRap() + 1);
                              ((Player) event.getWhoClicked()).openInventory(Menus.getRaceRap(((Player) event.getWhoClicked())));
                              Race_Core.getRunner((Player) event.getWhoClicked()).UpdateScoreboard();
                         }
                         if (event.getRawSlot() == 24) {
                              if (Race_Core.getRace(run.getRaceID()).getRap() == 1) return;
                              Race_Core.getRace(run.getRaceID()).setRap(Race_Core.getRace(run.getRaceID()).getRap() + 1);
                              ((Player) event.getWhoClicked()).openInventory(Menus.getRaceRap(((Player) event.getWhoClicked())));
                              Race_Core.getRunner((Player) event.getWhoClicked()).UpdateScoreboard();
                         }
                         break;
                    case "RACE_CREATE_AMOUNT":
                         event.setCancelled(true);
                         if (event.getRawSlot() == 45) ((Player) event.getWhoClicked()).openInventory(Menus.getRaceCreate((Player) event.getWhoClicked()));
                         if (event.getRawSlot() == 20) {
                              Race_Core.getRace(run.getRaceID()).setJoin_Amount(Race_Core.getRace(run.getRaceID()).getJoin_Amount() + 1);
                              ((Player) event.getWhoClicked()).openInventory(Menus.getRaceAmount(((Player) event.getWhoClicked())));
                              Race_Core.getRunner((Player) event.getWhoClicked()).UpdateScoreboard();
                         }
                         if (event.getRawSlot() == 24) {
                              if (Race_Core.getRace(run.getRaceID()).getJoin_Amount() == 1) return;
                              Race_Core.getRace(run.getRaceID()).setJoin_Amount(Race_Core.getRace(run.getRaceID()).getJoin_Amount() - 1);
                              ((Player) event.getWhoClicked()).openInventory(Menus.getRaceAmount(((Player) event.getWhoClicked())));
                              Race_Core.getRunner((Player) event.getWhoClicked()).UpdateScoreboard();
                         }
                         break;
                    case "RACE_CREATE_ICON":
                         event.setCancelled(true);
                         if (event.getRawSlot() == 45) {
                              ((Player) event.getWhoClicked()).openInventory(Menus.getRaceCreate((Player) event.getWhoClicked()));
                         } else {
                              if (event.getCurrentItem() == null) return;
                              Race_Core.getRace(run.getRaceID()).setIcon(event.getCurrentItem().getType());
                              ((Player) event.getWhoClicked()).openInventory(Menus.getRaceIcon(((Player) event.getWhoClicked())));
                         }
                         break;
                    case "RACE_CREATE_CHECKPOINT":
                         if (event.getRawSlot() == 45) {
                              event.setCancelled(true);
                              ((Player) event.getWhoClicked()).openInventory(Menus.getRaceCreate((Player) event.getWhoClicked()));
                         } else {
                              if (event.getCurrentItem() == null || event.getCurrentItem().getType() != Material.MAP) {
                                   event.setCancelled(true);
                                   return;
                              }
                              switch (event.getAction()) {
                                   case CLONE_STACK: //remove
                                        Race_Core.getRace(run.getRaceID()).getCheckPointLoc().remove(event.getRawSlot() - 9);
                                        ((Player) event.getWhoClicked()).openInventory(Menus.getRaceCheckPoint((Player) event.getWhoClicked()));
                                        event.setCancelled(true);
                                        break;
                                   case PICKUP_HALF: //Update
                                        Race_Core.getRace(run.getRaceID()).getCheckPointLoc().set(event.getRawSlot() - 9, Race_Core.getRace(run.getRaceID()).getCheckPointLoc().get(event.getRawSlot() - 9));
                                        event.setCancelled(true);
                                        break;
                                   case PICKUP_ALL:
                                        Hachitai.setCircle(run, Race_Core.getRace(run.getRaceID()).getStartPointLoc().get(event.getRawSlot() - 9).getLocation(), 1);

                                        ((Player) event.getWhoClicked()).openInventory(Menus.getRaceCheckPoint((Player) event.getWhoClicked()));
                                        event.setCancelled(true);
                                        break;
                                   default:
                                        break;
                              }
                         }
                         break;
                    case "RACE_CREATE_STARTPOINT":
                         if (event.getRawSlot() == 45) {
                              event.setCancelled(true);
                              ((Player) event.getWhoClicked()).openInventory(Menus.getRaceCreate((Player) event.getWhoClicked()));
                         } else {
                              if (event.getCurrentItem() == null || event.getCurrentItem().getType() != Material.EMERALD_BLOCK) {
                                   event.setCancelled(true);
                                   return;
                              }
                              switch (event.getAction()) {
                                   case CLONE_STACK: //remove
                                        Race_Core.getRace(run.getRaceID()).getStartPointLoc().remove(event.getRawSlot() - 9);
                                        ((Player) event.getWhoClicked()).openInventory(Menus.getRaceStartPoint((Player) event.getWhoClicked()));
                                        event.setCancelled(true);
                                        break;
                                   case PICKUP_HALF: //Update
                                        Race_Core.getRace(run.getRaceID()).getStartPointLoc().set(event.getRawSlot() - 9, new Loc_parts(((Player) event.getWhoClicked()).getLocation()));
                                        event.setCancelled(true);
                                        break;
                                   case PICKUP_ALL:
                                        Hachitai.setCircle(run, Race_Core.getRace(run.getRaceID()).getStartPointLoc().get(event.getRawSlot() - 9).getLocation(), 1);
                                        ((Player) event.getWhoClicked()).openInventory(Menus.getRaceStartPoint((Player) event.getWhoClicked()));
                                        event.setCancelled(true);
                                        break;
                                   default:
                                        break;
                              }
                         }
               }
          }
     }

     @EventHandler
     public void SignChangeEvent(SignChangeEvent e) {
          if (e.getLine(0).equals("[race]")) e.setLine(0, "[Race]");
          if (!(e.getLine(0).equals("[Race]"))) return;
          String name_cash = e.getLine(1);

          e.setLine(1, "Loaging...");
          Race Race = Race_Core.getRace(name_cash);
          if (Race == null) {
               e.setLine(1, ChatColor.RED + "Error");
               return;
          } else {
               e.setLine(1, Race.getRace_name());
               e.setLine(2, Race.getRap() + " : Rap");
               e.setLine(3, Race.getCreator());
          }
     }

     @Deprecated
     @EventHandler(ignoreCancelled = true)
     public void onEnSignClick(PlayerInteractEvent e) {
          if (e.getPlayer().isSneaking() || !(e.getClickedBlock().getState() instanceof Sign) || e.getAction() != Action.RIGHT_CLICK_BLOCK || !e.hasBlock()) return;
          if (!(((Sign) e.getClickedBlock().getState()).getLine(0).equals("[Race]"))) return;
          if (Race_Core.getRace(((Sign) e.getClickedBlock().getState()).getLine(1)) == null) {
               e.getPlayer().sendMessage(CollarMessage.setWarning() + "Unknown Race");
               ((Sign) e.getClickedBlock().getState()).setLine(1, ChatColor.RED + "Error");
               ((Sign) e.getClickedBlock().getState()).setLine(2, "");
               ((Sign) e.getClickedBlock().getState()).setLine(3, "");
               return;
          }
          Race_Core.joinRace(Race_Core.getRace((((Sign) e.getClickedBlock().getState()).getLine(1))), e.getPlayer());
     }

     @EventHandler
     public void onPlayerMove(final PlayerMoveEvent event) {
          if (Race_Core.getRunners().isEmpty()) return;
          Race_Runner run = Race_Core.getRunner(event.getPlayer());
          if (run == null) return;
          switch (run.getMode()) {
               case EDIT:
                    if (!(Race_Core.getRace(run.getRaceID()).getCheckPointLoc().size() == 0)) for (int i = 0; i < Race_Core.getRace(run.getRaceID()).getCheckPointLoc().size(); i++) run.getLocationViewer().DrawCircle(i);
                    break;
               case GOAL:
                    break;
               case RUN:
                    Location chackpoint = Race_Core.getRace(run.getRaceID()).getCheckPointLoc().get(run.getCheckPoint()).getLocation();
                    run.setnewLoc(event.getTo());
                    run.setoldLoc(event.getFrom());
                    run.getLocationViewer().DrawCircle(run.getCheckPoint());
                    run.UpdateScoreboard();
                    if (Hachitai.CheckPlanePassed(run)) {
                         double[] rtn = Hachitai.GetIntersection(run, chackpoint, event.getTo(), event.getFrom());
                         if (((rtn[0] - chackpoint.getX()) * (rtn[0] - chackpoint.getX()) + (rtn[1] - chackpoint.getY()) * (rtn[1] - chackpoint.getY()) + (rtn[2] - chackpoint.getZ()) * (rtn[2] - chackpoint.getZ())) < Race_Core.getRace(run.getRaceID()).getCheckPointLoc().get(run.getCheckPoint()).getr() * Race_Core.getRace(run.getRaceID()).getCheckPointLoc().get(run.getCheckPoint()).getr()) run.addCheckPoint();
                    }
                    break;
               case WAIT:
                    break;
               default:
                    break;
          }
     }

     @EventHandler
     public void leave(PlayerQuitEvent event) {
          Race_Core.removeRunner(event.getPlayer());
     }

     @Deprecated
     @EventHandler
     public void AnitBoat_Damage(VehicleDestroyEvent event) {
          if (!(event.getVehicle().getPassenger() instanceof Player) || !(event.getVehicle().getType() == EntityType.BOAT)) return;
          if (Race_Core.isJoin((Player) event.getVehicle().getPassenger())) for (Race_Runner val : Race_Core.getRunners()) if (val.getPlayer().getUniqueId() == event.getVehicle().getPassenger().getUniqueId() && val.getMode() == Race_Mode.RUN) {
               event.setCancelled(true);
               return;
          }
     }

     @SuppressWarnings("unlikely-arg-type")
     @EventHandler
     public void AnitBoat_Leave(VehicleExitEvent event) {
          if (!(event.getExited() instanceof Player) || !(event.getVehicle().getType() == EntityType.BOAT)) return;
          if (Race_Core.Race_Runner_Onetime.equals((Player) event.getExited())) {
               Race_Core.Race_Runner_Onetime.remove((Player) event.getExited());
               event.setCancelled(false);
               return;
          }

          if (Race_Core.isJoin((Player) event.getExited())) for (Race_Runner val : Race_Core.getRunners()) if (val.getPlayer() == (Player) event.getExited() && val.getMode() == Race_Mode.RUN) {
               event.setCancelled(true);

               return;
          }
     }

     @EventHandler
     public void AnitEnter(VehicleEnterEvent event) {}
}
