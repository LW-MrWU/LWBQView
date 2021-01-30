package vip.gameclub.lwbqview.config;

import vip.gameclub.lwbqview.MainPlugin;
import vip.gameclub.lwlib.model.config.BaseDefaultConfig;

/**
 * TODO
 *
 * @author LW-MrWU
 * @date 创建时间 2021/1/28 17:53
 */
public class DefaultConfig extends BaseDefaultConfig {
    /**
     * 构造函数
     *
     * @return
     * @author LW-MrWU
     * @date 2021/1/28 17:56
     */
    public DefaultConfig() {
        super(MainPlugin.getInstance());
    }

    @Override
    protected void loadDefaultConfig() {

    }
}
