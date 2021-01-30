package vip.gameclub.lwbqview.test;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

/**
 * TODO
 *
 * @author LW-MrWU
 * @date 创建时间 2021/1/30 13:58
 */
public class BaseInventory {
    private Inventory inventory;

    public BaseInventory(){
        this.inventory = Bukkit.createInventory(null, 54, "test");
    }
}
