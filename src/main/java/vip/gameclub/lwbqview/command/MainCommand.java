package vip.gameclub.lwbqview.command;

import org.bukkit.command.CommandSender;
import vip.gameclub.lwbqview.MainPlugin;
import vip.gameclub.lwlib.model.command.BaseCommand;
import vip.gameclub.lwlib.model.enumModel.BaseCommandSenderType;
import vip.gameclub.lwlib.service.plugin.BasePlugin;

import java.util.List;

/**
 * TODO
 *
 * @author LW-MrWU
 * @date 创建时间 2021/1/28 17:59
 */
public class MainCommand extends BaseCommand {
    public MainCommand() {
        super("lwbqview", "lbv");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        showHelp(commandSender, MainPlugin.getInstance());
        addSubCommands(new TestCommand());
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
        return null;
    }

    @Override
    public String getUsageHelp() {
        return null;
    }
}
