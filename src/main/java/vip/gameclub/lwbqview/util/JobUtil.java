package vip.gameclub.lwbqview.util;

import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.Pointer;
import pl.betoncraft.betonquest.api.Objective;
import pl.betoncraft.betonquest.config.Config;
import pl.betoncraft.betonquest.config.ConfigPackage;
import pl.betoncraft.betonquest.utils.LogUtils;
import vip.gameclub.lwbqview.model.enumModel.LanguageEnum;
import vip.gameclub.lwbqview.model.scoreboard.JobScoreboard;
import vip.gameclub.lwlib.service.utils.BasePlayerUtil;
import vip.gameclub.lwlib.service.utils.BaseStringUtil;
import vip.gameclub.lwlib.service.utils.BaseVariableUtil;

import java.util.logging.Level;

/**
 * 任务基础方法
 *
 * @author LW-MrWU
 * @date 创建时间 2021/1/31 16:14
 */
public class JobUtil{

    /**
     * 获取任务详情
     * @param pointer 1
     * @return java.lang.String
     * @author LW-MrWU
     * @date 2021/2/1 17:59
     */
    public static String getPointerText(Pointer pointer){
        final String[] parts = pointer.getPointer().split("\\.");
        final String packName = parts[0];
        final ConfigPackage pack = Config.getPackages().get(packName);
        if (pack == null) {
            return "";
        }
        final String pointerName = parts[1];
        // resolve the text in player's language
        String text;
        if (pack.getJournal().getConfig().contains(pointerName)) {
            text = pack.getFormattedString("journal." + pointerName);
        } else {
            LogUtils.getLogger().log(Level.WARNING, "No defined journal entry " + pointerName + " in package " + pack.getName());
            text = "error";
        }
        return text;
    }

    /**
     * 获取任务名
     * @param pointer 1
     * @return java.lang.String
     * @author LW-MrWU
     * @date 2021/2/1 18:00
     */
    public static String getJobName(Pointer pointer){
        final String[] parts = pointer.getPointer().split("\\.");
        final String pointerName = parts[1];
        String jobName = pointerName.split("_")[0];
        return jobName;
    }

    /**
     * 替换job自变量
     * @param player 玩家
     * @param str 展示内容
     * @return java.lang.String
     * @author LW-MrWU
     * @date 2021/2/2 17:35
     */
    public static String replaceVariable(Player player, String str){
        if(BaseVariableUtil.isContains(str, "_c")){
            String playerId = BasePlayerUtil.getID(player);

            for (Objective objective : BetonQuest.getInstance().getPlayerObjectives(playerId)){
                String label = objective.getLabel();
                String var = BaseVariableUtil.getVariable(str, "_c");
                if(label.equalsIgnoreCase(var)){
                    int left = Integer.parseInt(objective.getData(playerId));
                    int sum = Integer.parseInt(objective.getDefaultDataInstruction());
                    str = BaseVariableUtil.replaceVariable(str, "_c", String.valueOf(sum-left));
                }
            }
            if(BaseVariableUtil.isContains(str, "_c")){
                str = BaseStringUtil.chatColorCodes(LanguageEnum.JOB_COMPLETE.getValue());
            }
        }
        if(BaseVariableUtil.isContains(str, "_s")){
            String playerId = BasePlayerUtil.getID(player);
            for (Objective objective : BetonQuest.getInstance().getPlayerObjectives(playerId)){
                if(objective.getLabel().equalsIgnoreCase(BaseVariableUtil.getVariable(str, "_s"))){
                    str = BaseVariableUtil.replaceVariable(str, "_s", objective.getDefaultDataInstruction());
                }
            }
            if(BaseVariableUtil.isContains(str, "_s")){
                str = BaseStringUtil.chatColorCodes(LanguageEnum.JOB_COMPLETE.getValue());
            }
        }
        return str;
    }

    /**
     * 刷新任务记分板
     * @param player 1
     * @return void
     * @author LW-MrWU
     * @date 2021/2/3 16:25
     */
    public static void reloadJobScoreboard(Player player){
        JobScoreboard jobScoreboard = JobScoreboard.getInstance(player, LanguageEnum.JOB_TITLE.getValue());
        jobScoreboard.reload();
    }
}
