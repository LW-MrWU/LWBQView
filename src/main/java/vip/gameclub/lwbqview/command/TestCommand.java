package vip.gameclub.lwbqview.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.Journal;
import pl.betoncraft.betonquest.Pointer;
import pl.betoncraft.betonquest.api.MobKillNotifier;
import pl.betoncraft.betonquest.api.Objective;
import pl.betoncraft.betonquest.config.ConfigPackage;
import pl.betoncraft.betonquest.database.PlayerData;
import pl.betoncraft.betonquest.id.ObjectiveID;
import pl.betoncraft.betonquest.utils.PlayerConverter;
import vip.gameclub.lwbqview.MainPlugin;
import vip.gameclub.lwlib.model.command.BaseCommand;
import vip.gameclub.lwlib.model.enumModel.BaseCommandSenderType;
import vip.gameclub.lwlib.service.plugin.BasePlugin;
import vip.gameclub.lwlib.service.utils.BasePlayerUtil;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author LW-MrWU
 * @date 创建时间 2021/1/29 14:58
 */
public class TestCommand extends BaseCommand {
    public TestCommand() {
        super("test");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        Player player = (Player)commandSender;

        final PlayerData playerData = BetonQuest.getInstance().getPlayerData(PlayerConverter.getID(player));

        List<Objective> objectiveList = BetonQuest.getInstance().getPlayerObjectives(BasePlayerUtil.getID(player));
        for (Objective objective : objectiveList){
            String data = objective.getData(PlayerConverter.getID(player));
            System.out.println("data:"+data);
            String defaultDataInstruction = objective.getDefaultDataInstruction();
            System.out.println("defaultDataInstruction:"+defaultDataInstruction);
            String label = objective.getLabel();
            System.out.println("label:"+label);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args) {
        return null;
    }

    @Override
    public BasePlugin getBasePlugin() {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public BaseCommandSenderType getCommandSenderType() {
        return BaseCommandSenderType.PLAYER;
    }

    @Override
    public String getUsageHelp() {
        return "测试命令";
    }

}
