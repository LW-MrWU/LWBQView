package vip.gameclub.lwbqview.listener.objective;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.MobKillNotifier;
import pl.betoncraft.betonquest.api.Objective;
import pl.betoncraft.betonquest.config.Config;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.objectives.MobKillObjective;
import pl.betoncraft.betonquest.utils.LogUtils;
import pl.betoncraft.betonquest.utils.PlayerConverter;
import pl.betoncraft.betonquest.utils.Utils;
import vip.gameclub.lwbqview.MainPlugin;
import vip.gameclub.lwbqview.model.enumModel.LanguageEnum;
import vip.gameclub.lwbqview.model.scoreboard.JobScoreboard;
import vip.gameclub.lwlib.service.utils.BasePlayerUtil;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

/**
 * 击杀怪物
 *
 * @author LW-MrWU
 * @date 创建时间 2021/2/2 10:55
 */
public class LWMobKillObjective extends Objective implements Listener {
    private final int notifyInterval;
    protected EntityType mobType;
    protected int amount;
    protected String name;
    protected String marked;
    protected boolean notify;

    private String jobName;

    public LWMobKillObjective(Instruction instruction) throws InstructionParseException {
        super(instruction);
        this.template = MobKillObjective.MobData.class;
        this.mobType = (EntityType)instruction.getEnum(EntityType.class);
        this.amount = instruction.getPositive();
        this.name = instruction.getOptional("name");
        if (this.name != null) {
            this.name = Utils.format(this.name, true, false).replace('_', ' ');
        }

        this.marked = instruction.getOptional("marked");
        if (this.marked != null) {
            this.marked = Utils.addPackage(instruction.getPackage(), this.marked);
        }

        this.notifyInterval = instruction.getInt(instruction.getOptional("notify"), 1);
        this.notify = instruction.hasArgument("notify") || this.notifyInterval > 0;

        String objectiveName = instruction.getID().getBaseID();
        if(objectiveName.contains("/") || objectiveName.contains(".")){
            MainPlugin.getInstance().getBaseLogService().warning("objectives.yml "+Utils.addPackage(instruction.getPackage(), objectiveName)+" 不支持/.符号");
            return;
        }
        if(!objectiveName.contains("_")){
            MainPlugin.getInstance().getBaseLogService().warning("objectives.yml "+Utils.addPackage(instruction.getPackage(), objectiveName)+" 节点参数格式错误,规范示例: 任务名_xxx: LWmobkill ZOMBIE 5");
            return;
        }
        jobName = objectiveName.split("_")[0];
    }

    @EventHandler(
            ignoreCancelled = true
    )
    @SuppressFBWarnings({"NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"})
    public void onMobKill(MobKillNotifier.MobKilledEvent event) {

        String playerID = PlayerConverter.getID(event.getPlayer());
        if (event.getEntity().getType().equals(this.mobType)) {
            if (this.name == null || event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals(this.name)) {
                if (this.marked != null) {
                    if (!event.getEntity().hasMetadata("betonquest-marked")) {
                        return;
                    }

                    List<MetadataValue> meta = event.getEntity().getMetadata("betonquest-marked");
                    Iterator var3 = meta.iterator();

                    while(var3.hasNext()) {
                        MetadataValue m = (MetadataValue)var3.next();
                        if (!m.asString().equals(this.marked)) {
                            return;
                        }
                    }
                }

                if (this.containsPlayer(playerID) && this.checkConditions(playerID)) {
                    MobKillObjective.MobData playerData = (MobKillObjective.MobData)this.dataMap.get(playerID);
                    playerData.subtract();
                    int playerAmount = playerData.getAmount();
                    if (playerData.isZero()) {
                        this.completeObjective(playerID);
                    } else if (this.notify && playerAmount % this.notifyInterval == 0) {
                        try {
                            Config.sendNotify(this.instruction.getPackage().getName(), playerID, "mobs_to_kill", new String[]{String.valueOf(playerData.getAmount())}, "mobs_to_kill,info");
                        } catch (QuestRuntimeException var7) {
                            QuestRuntimeException exception = var7;

                            try {
                                LogUtils.getLogger().log(Level.WARNING, "The notify system was unable to play a sound for the 'mobs_to_kill' category in '" + this.instruction.getObjective().getFullID() + "'. Error was: '" + exception.getMessage() + "'");
                            } catch (InstructionParseException var6) {
                                LogUtils.logThrowableReport(var6);
                            }
                        }
                    }
                }
            }
        }

        System.out.println("playerID:"+playerID);
        JobScoreboard jobScoreboard = JobScoreboard.getInstance(BasePlayerUtil.getPlayer(playerID), LanguageEnum.JOB_TITLE.getValue());
        jobScoreboard.reload();
    }

    public void start() {
        Bukkit.getPluginManager().registerEvents(this, BetonQuest.getInstance());
    }

    public void stop() {
        HandlerList.unregisterAll(this);
    }

    public String getDefaultDataInstruction() {
        return Integer.toString(this.amount);
    }

    public String getProperty(String name, String playerID) {
        if ("left".equalsIgnoreCase(name)) {
            return Integer.toString(((MobKillObjective.MobData)this.dataMap.get(playerID)).getAmount());
        } else {
            return "amount".equalsIgnoreCase(name) ? Integer.toString(this.amount - ((MobKillObjective.MobData)this.dataMap.get(playerID)).getAmount()) : "";
        }
    }

    public static class MobData extends ObjectiveData {
        private int amount;

        public MobData(String instruction, String playerID, String objID) {
            super(instruction, playerID, objID);
            this.amount = Integer.parseInt(instruction);
        }

        public int getAmount() {
            return this.amount;
        }

        public void subtract() {
            --this.amount;
            this.update();
        }

        public boolean isZero() {
            return this.amount <= 0;
        }

        public String toString() {
            return Integer.toString(this.amount);
        }
    }
}
