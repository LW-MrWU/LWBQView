package vip.gameclub.lwbqview.service;

import pl.betoncraft.betonquest.Pointer;
import pl.betoncraft.betonquest.config.Config;
import pl.betoncraft.betonquest.config.ConfigPackage;
import pl.betoncraft.betonquest.utils.LogUtils;

import java.util.logging.Level;

/**
 * TODO
 *
 * @author LW-MrWU
 * @date 创建时间 2021/1/31 16:14
 */
public class JobService{
    private static JobService jobService;

    private JobService(){}

    public static JobService getInstance(){
        if(jobService == null){
            jobService = new JobService();
        }
        return jobService;
    }

    public String getPointerText(Pointer pointer){
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


    public String getJobName(Pointer pointer){
        final String[] parts = pointer.getPointer().split("\\.");
        final String pointerName = parts[1];
        String jobName = pointerName.split("_")[0];
        return jobName;
    }
}
