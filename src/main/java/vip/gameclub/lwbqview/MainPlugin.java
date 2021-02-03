package vip.gameclub.lwbqview;

import pl.betoncraft.betonquest.BetonQuest;
import vip.gameclub.lwbqview.command.MainCommand;
import vip.gameclub.lwbqview.config.DefaultConfig;
import vip.gameclub.lwbqview.event.LWJournalEvent;
import vip.gameclub.lwbqview.listener.JobSelectListener;
import vip.gameclub.lwbqview.listener.bq.BQObjectiveListener;
import vip.gameclub.lwlib.model.enumModel.BaseSysMsgEnum;
import vip.gameclub.lwlib.service.plugin.BasePlugin;


/**
 * TODO
 *
 * @author LW-MrWU
 * @date 创建时间 2021/1/28 17:48
 */
public class MainPlugin extends BasePlugin {
    private static MainPlugin mainPlugin;

    public static MainPlugin getInstance(){
        return mainPlugin;
    }

    @Override
    public boolean enable() {
        mainPlugin = this;

        initConfig();

        initCommand();

        initListener();

        initBQCustomRegister();

        //成功加载提示
        getBaseLogService().infoByLanguage(BaseSysMsgEnum.SUCCESS_LOAD.name(), BaseSysMsgEnum.SUCCESS_LOAD.getValue());
        return true;
    }

    @Override
    public boolean disable() {
        return false;
    }

    /**
     * 加载config配置文件
     * @param
     * @return void
     * @author LW-MrWU
     * @date 2021/1/28 17:57
     */
    private void initConfig(){
        DefaultConfig defaultConfig = new DefaultConfig();
    }

    /**
     * 注册主命令
     * @param
     * @return void
     * @author LW-MrWU
     * @date 2021/1/28 17:59
     */
    private void initCommand(){
        MainCommand mainCommand = new MainCommand();
        registerCommand(mainCommand);
    }

    private void initListener(){
        registerListener(new JobSelectListener());
        registerListener(new BQObjectiveListener());
    }

    private void initBQCustomRegister(){
        //journal
        getBetonQuest().registerEvents("LWjournal", LWJournalEvent.class);
    }

    public BetonQuest getBetonQuest(){
        return BetonQuest.getInstance();
    }
}
