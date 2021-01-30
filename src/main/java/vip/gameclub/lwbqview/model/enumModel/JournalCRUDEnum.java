package vip.gameclub.lwbqview.model.enumModel;

/**
 * 日志操作参数
 *
 * @author LW-MrWU
 * @date 创建时间 2021/1/30 16:33
 */
public enum JournalCRUDEnum {
    ADD("ADD"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private String value;

    JournalCRUDEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static JournalCRUDEnum getEnumByName(String enumName) {
        JournalCRUDEnum result = null;
        JournalCRUDEnum[] values = JournalCRUDEnum.values();
        for(int i=0;i<values.length;i++) {
            JournalCRUDEnum tempEnum = values[i];
            if(tempEnum.name().equals(enumName)) {
                result = tempEnum;
                break;
            }
        }
        return result;
    }
}
