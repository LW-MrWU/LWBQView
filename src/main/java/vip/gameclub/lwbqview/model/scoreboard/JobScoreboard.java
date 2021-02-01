package vip.gameclub.lwbqview.model.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import vip.gameclub.lwbqview.MainPlugin;
import vip.gameclub.lwlib.model.enumModel.BaseSysMsgEnum;
import vip.gameclub.lwlib.model.scoreboard.BaseScoreboard;

/**
 * TODO
 *
 * @author LW-MrWU
 * @date 创建时间 2021/2/1 11:50
 */
public class JobScoreboard extends BaseScoreboard {
    private static JobScoreboard jobScoreboard;

    public JobScoreboard(Player player, String title, Integer count) {
        super(MainPlugin.getInstance(), player, title, DisplaySlot.SIDEBAR, count);
    }

    public static JobScoreboard getInstance(Player player, String title){
        if(!getScoreboardData().containsKey(player)){
            Integer count = MainPlugin.getInstance().getConfig().getInt("jobcount");
            if(count == null){
                MainPlugin.getInstance().getBaseLogService().infoByLanguage(BaseSysMsgEnum.FILECONFIG_GET_ERROR.name(),BaseSysMsgEnum.FILECONFIG_GET_ERROR.getValue(), "config.yml", "jobcount");
            }
            jobScoreboard = new JobScoreboard(player, title, count);
        }else{
            jobScoreboard = (JobScoreboard) getScoreboardData().get(player);
        }
        return jobScoreboard;
    }

    @Override
    protected boolean showCustom() {
        return false;
    }
}
