package vip.gameclub.lwbqview.event;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.Journal;
import pl.betoncraft.betonquest.Pointer;
import pl.betoncraft.betonquest.api.QuestEvent;
import pl.betoncraft.betonquest.database.Connector;
import pl.betoncraft.betonquest.database.PlayerData;
import pl.betoncraft.betonquest.database.Saver;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.utils.PlayerConverter;
import pl.betoncraft.betonquest.utils.Utils;
import vip.gameclub.lwbqview.MainPlugin;
import vip.gameclub.lwbqview.model.enumModel.JournalCRUDEnum;
import vip.gameclub.lwbqview.model.enumModel.LanguageEnum;
import vip.gameclub.lwbqview.model.scoreboard.JobScoreboard;
import vip.gameclub.lwbqview.util.JobUtil;
import vip.gameclub.lwlib.service.utils.BasePlayerUtil;

@SuppressWarnings("PMD.CommentRequired")
public class LWJournalEvent extends QuestEvent {

    private String name;
    private JournalCRUDEnum crud;

    public LWJournalEvent(final Instruction instruction) throws InstructionParseException {
        super(instruction, false);
        staticness = true;
        final String first = instruction.next();
        //判断主参数是否合法
        JournalCRUDEnum journalCRUDEnum = JournalCRUDEnum.getEnumByName(StringUtils.upperCase(first));
        if(journalCRUDEnum == null){
            MainPlugin.getInstance().getBaseLogService().warning("event.yml LWjournal " + first + " 参数错误,请使用:add|update|delete");
            return;
        }
        this.crud = journalCRUDEnum;

        //判断第二参数
        String second = instruction.next();
        if(second.contains("/") || second.contains(".")){
            MainPlugin.getInstance().getBaseLogService().warning("journal.yml "+Utils.addPackage(instruction.getPackage(), second)+" 不支持/.符号");
            return;
        }
        if(!second.contains("_")){
            MainPlugin.getInstance().getBaseLogService().warning("journal.yml "+Utils.addPackage(instruction.getPackage(), second)+" 节点参数格式错误,规范示例: 任务名_xxx: '任务详情'");
            return;
        }
        this.name = Utils.addPackage(instruction.getPackage(), second);
    }
    
    
    @SuppressWarnings({"PMD.PreserveStackTrace", "PMD.CyclomaticComplexity"})
    @Override
    protected Void execute(final String playerID) throws QuestRuntimeException {
        if(name == null){
            MainPlugin.getInstance().getBaseLogService().warning("&c节点错误, /q reload 重载插件!");
        }
        if(!name.contains("_")){
            removePlayersPointer();
        }

        if (playerID == null) {
            if (!crud.getValue().equalsIgnoreCase("add") && name != null) {
                removePlayersPointer();
            }
        } else {
            final PlayerData playerData = PlayerConverter.getPlayer(playerID) == null ? new PlayerData(playerID) : BetonQuest.getInstance().getPlayerData(playerID);
            final Journal journal = playerData.getJournal();

            Pointer oldPoint = getPointer(journal);
            //判断记分板是否展示了该系列任务
            JobScoreboard jobScoreboard = JobScoreboard.getInstance(BasePlayerUtil.getPlayer(playerID), LanguageEnum.JOB_TITLE.getValue());
            boolean flag = false;
            if(jobScoreboard.isJobContains(oldPoint)){
                flag = true;
            }

            switch (crud){
                case ADD:
                    Pointer pointerAdd = new Pointer(name, new Date().getTime());
                    journal.addPointer(pointerAdd);
                    MainPlugin.getInstance().getBaseMessageService().sendMessageByLanguagePlayerId(playerID, LanguageEnum.JOB_ADD.name(), LanguageEnum.JOB_ADD.getValue(), JobUtil.getJobName(pointerAdd));
                    //TODO 新增任务自动覆盖展示记分板
                    break;
                case UPDATE:
                    if(oldPoint == null){
                        break;
                    }

                    if(StringUtils.isNotEmpty(oldPoint.getPointer())){
                        journal.removePointer(oldPoint.getPointer());
                        //删除记分板team
                        if(flag){
                            jobScoreboard.delJob(oldPoint);
                        }
                    }

                    Pointer pointer = new Pointer(name, new Date().getTime());
                    journal.addPointer(pointer);
                    if(flag){
                        jobScoreboard.addJob(pointer);
                    }
                    MainPlugin.getInstance().getBaseMessageService().sendMessageByLanguagePlayerId(playerID, LanguageEnum.JOB_UPDATE.name(), LanguageEnum.JOB_UPDATE.getValue(), JobUtil.getJobName(pointer));
                    break;
                case DELETE:
                    if(oldPoint == null){
                        break;
                    }
                    journal.removePointer(oldPoint.getPointer());
                    if(flag){
                        jobScoreboard.delJob(oldPoint);
                    }
                    MainPlugin.getInstance().getBaseMessageService().sendMessageByLanguagePlayerId(playerID, LanguageEnum.JOB_DELETE.name(), LanguageEnum.JOB_DELETE.getValue(), JobUtil.getJobName(oldPoint));
                    break;
            }
            journal.update();
        }
        return null;
    }

    /**
     * 删除所有玩家的指定pointer
     * @param
     * @return void
     * @author LW-MrWU
     * @date 2021/1/30 16:19
     */
    private void removePlayersPointer(){
        for (final Player p : BasePlayerUtil.getOnlinePlayerList()) {
            final PlayerData playerData = BetonQuest.getInstance().getPlayerData(PlayerConverter.getID(p));
            final Journal journal = playerData.getJournal();
            journal.removePointer(name);
            journal.update();
        }
        BetonQuest.getInstance().getSaver().add(new Saver.Record(Connector.UpdateType.REMOVE_ALL_ENTRIES, new String[]{
                name
        }));
    }

    private Pointer getPointer(Journal journal){
        String jobName = name.split("_")[0];
        //先查找玩家所拥有的同名任务节点
        for (Pointer pointer : journal.getPointers()){
            String pointerName = pointer.getPointer();
            if(pointerName.split("_")[0].equalsIgnoreCase(jobName)){
                return pointer;
            }
        }
        return null;
    }
}
