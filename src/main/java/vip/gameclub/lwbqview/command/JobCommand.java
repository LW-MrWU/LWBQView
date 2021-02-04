package vip.gameclub.lwbqview.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vip.gameclub.lwbqview.model.inventory.JobInventory;
import vip.gameclub.lwlib.model.command.BaseCommand;
import vip.gameclub.lwlib.model.enumModel.BaseCommandSenderType;
import vip.gameclub.lwlib.service.plugin.BasePlugin;

import java.util.List;

/**
 * 任务命令
 *
 * @author LW-MrWU
 * @date 创建时间 2021/2/1 18:07
 */
public class JobCommand extends BaseCommand {
    public JobCommand() {
        super("jobs");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        Player player = (Player)commandSender;

        JobInventory.getInstance(player).load();
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
        return null;
    }
}
