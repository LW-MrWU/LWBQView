package vip.gameclub.lwbqview.model.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import pl.betoncraft.betonquest.Pointer;
import vip.gameclub.lwbqview.MainPlugin;
import vip.gameclub.lwbqview.service.JobService;
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

    /**
     * 增加跟踪任务
     * @param pointer
     * @return void
     * @author LW-MrWU
     * @date 2021/2/1 17:36
     */
    public void addJob(Pointer pointer){
        String jobName = JobService.getInstance().getJobName(pointer);
        String text = JobService.getInstance().getPointerText(pointer);
        String[] strs = text.split("\n");
        jobScoreboard.addTeam(jobName, strs);
    }

    /**
     * 删除跟踪任务
     * @param pointer 1
     * @return void
     * @author LW-MrWU
     * @date 2021/2/1 17:38
     */
    public void delJob(Pointer pointer){
        String jobName = JobService.getInstance().getJobName(pointer);
        jobScoreboard.delTeam(jobName);
    }

    /**
     * 判断任务是否在记分板中展示
     * @param pointer
     * @return boolean
     * @author LW-MrWU
     * @date 2021/2/1 17:36
     */
    public boolean isJobContains(Pointer pointer){
        if(pointer == null){
            return false;
        }

        String jobName = JobService.getInstance().getJobName(pointer);

        return isContains(jobName);
    }
}
