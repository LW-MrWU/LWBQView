package vip.gameclub.lwbqview.model.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import pl.betoncraft.betonquest.Pointer;
import vip.gameclub.lwbqview.MainPlugin;
import vip.gameclub.lwbqview.util.JobUtil;
import vip.gameclub.lwlib.model.enumModel.BaseSysMsgEnum;
import vip.gameclub.lwlib.model.scoreboard.BaseScoreboard;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        MainPlugin.getInstance().registerScoreboard(this);
    }

    public static JobScoreboard getInstance(Player player, String title){
        jobScoreboard = (JobScoreboard)MainPlugin.getInstance().getBaseScoreboardService().getScoreBoard(player, JobScoreboard.class);
        if(jobScoreboard == null){
            Integer count = MainPlugin.getInstance().getConfig().getInt("jobcount");
            if(count == null){
                MainPlugin.getInstance().getBaseLogService().infoByLanguage(BaseSysMsgEnum.FILECONFIG_GET_ERROR.name(),BaseSysMsgEnum.FILECONFIG_GET_ERROR.getValue(), "config.yml", "jobcount");
            }
            jobScoreboard = new JobScoreboard(player, title, count);
        }
        return jobScoreboard;
    }

    @Override
    protected boolean showCustom() {
        int i = 0;
        List<Map<String, List<String>>> moduleList = getModuleList();
        for (Map<String, List<String>> module : moduleList){
            Iterator<Map.Entry<String, List<String>>> iterator = module.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, List<String>> entry = iterator.next();
                String key = entry.getKey();
                for (String str : entry.getValue()){
                    str = JobUtil.replaceVariable(getPlayer(), str);
                    //判断长度是否超过40,自动换行
                    int length = str.length();
                    if(length > 40){
                        for(int n=0; n<=length/40; n++){
                            if(n == length/40){
                                getObjective().getScore(str.substring(n*39)).setScore(++i);
                                break;
                            }
                            getObjective().getScore(str.substring(n*39, n*39+39)).setScore(++i);
                        }
                    }else{
                        getObjective().getScore(str).setScore(++i);
                    }
                }
                getObjective().getScore(key).setScore(++i);
            }
        }

        return true;
    }

    /**
     * 增加跟踪任务
     * @param pointer
     * @return void
     * @author LW-MrWU
     * @date 2021/2/1 17:36
     */
    public void addJob(Pointer pointer){
        String jobName = JobUtil.getJobName(pointer);
        String text = JobUtil.getPointerText(pointer);
        if(text.startsWith("[") && text.endsWith("]")){
            jobScoreboard.addModule(jobName, text.substring(1,text.length()-1).split(","));
        }else{
            jobScoreboard.addModule(jobName, text);
        }
    }

    /**
     * 删除跟踪任务
     * @param pointer 1
     * @return void
     * @author LW-MrWU
     * @date 2021/2/1 17:38
     */
    public void delJob(Pointer pointer){
        String jobName = JobUtil.getJobName(pointer);
        jobScoreboard.delModule(jobName);
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

        String jobName = JobUtil.getJobName(pointer);

        return isContains(jobName);
    }
}
