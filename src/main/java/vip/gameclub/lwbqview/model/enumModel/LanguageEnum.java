package vip.gameclub.lwbqview.model.enumModel;

/**
 * 语言操作
 *
 * @author LW-MrWU
 * @date 创建时间 2021/1/30 16:33
 */
public enum LanguageEnum {
    JOB_TITLE("&f任务跟踪"),
    JOB_ADD("&b新任务 &e{0} &b已添加"),
    JOB_UPDATE("&b任务 &e{0} &b已更新"),
    JOB_DELETE("&b任务 &e{0} &c已删除");

    private String value;

    LanguageEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LanguageEnum getEnumByName(String enumName) {
        LanguageEnum result = null;
        LanguageEnum[] values = LanguageEnum.values();
        for(int i=0;i<values.length;i++) {
            LanguageEnum tempEnum = values[i];
            if(tempEnum.name().equals(enumName)) {
                result = tempEnum;
                break;
            }
        }
        return result;
    }
}
