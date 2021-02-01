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
            MainPlugin.getInstance().getBaseLogService().warning("event.yml LWjournal "+Utils.addPackage(instruction.getPackage(), second)+" 不支持/.符号");
            return;
        }
        if(!second.contains("_")){
            MainPlugin.getInstance().getBaseLogService().warning("event.yml LWjournal "+Utils.addPackage(instruction.getPackage(), second)+" 节点参数格式错误,规范示例: 任务名_xxx: '任务详情'");
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

            String oldPoint;
            switch (crud){
                case ADD:
                    journal.addPointer(new Pointer(name, new Date().getTime()));
                    MainPlugin.getInstance().getBaseMessageService().sendMessageByLanguagePlayerId(playerID, LanguageEnum.JOB_ADD.name(), LanguageEnum.JOB_ADD.getValue(), getJobName());
                    break;
                case UPDATE:
                    oldPoint = getJobPointer(journal);
                    if(StringUtils.isNotEmpty(oldPoint)){
                        journal.removePointer(oldPoint);
                    }
                    journal.addPointer(new Pointer(name, new Date().getTime()));
                    MainPlugin.getInstance().getBaseMessageService().sendMessageByLanguagePlayerId(playerID, LanguageEnum.JOB_UPDATE.name(), LanguageEnum.JOB_UPDATE.getValue(), getJobName());
                    break;
                case DELETE:
                    oldPoint = getJobPointer(journal);
                    journal.removePointer(oldPoint);
                    MainPlugin.getInstance().getBaseMessageService().sendMessageByLanguagePlayerId(playerID, LanguageEnum.JOB_DELETE.name(), LanguageEnum.JOB_DELETE.getValue(), getJobName());
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
        for (final Player p : MainPlugin.getInstance().getBasePlayerService().getOnlinePlayerList()) {
            final PlayerData playerData = BetonQuest.getInstance().getPlayerData(PlayerConverter.getID(p));
            final Journal journal = playerData.getJournal();
            journal.removePointer(name);
            journal.update();
        }
        BetonQuest.getInstance().getSaver().add(new Saver.Record(Connector.UpdateType.REMOVE_ALL_ENTRIES, new String[]{
                name
        }));
    }

    private String getJobPointer(Journal journal){
        String jobName = name.split("_")[0];
        //先查找玩家所拥有的同名任务节点
        for (Pointer pointer : journal.getPointers()){
            String pointerName = pointer.getPointer();
            if(pointerName.split("_")[0].equalsIgnoreCase(jobName)){
                return pointerName;
            }
        }
        return "";
    }

    private String getJobName(){
        String str = name.split("_")[0];
        return str.substring(str.indexOf(".")+1);
    }
}
