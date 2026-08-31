package nl.tinyaii.tinyclaim.gui;

import nl.tinyaii.tinyclaim.TinyClaimPlugin;
import nl.tinyaii.tinyclaim.data.Claim;
import nl.tinyaii.tinyclaim.util.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * 欢迎语设置点击：设置按钮提示用命令；返回回详情页。
 */
public class WelcomeGuiListener implements Listener {

    private final TinyClaimPlugin plugin;

    public WelcomeGuiListener(TinyClaimPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof WelcomeHolder)) return;
        e.setCancelled(true);
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        WelcomeHolder holder = (WelcomeHolder) e.getInventory().getHolder();
        Claim c = plugin.getClaimManager().getById(holder.claimId);
        if (c == null) { p.closeInventory(); return; }
        int raw = e.getRawSlot();
        if (raw == 12) {
            p.sendMessage(Messages.color("&a设置欢迎语: &e/领地 欢迎语 <消息>"));
            p.sendMessage(Messages.color("&a清除: &e/领地 欢迎语 -"));
        } else if (raw == 14) {
            p.sendMessage(Messages.color("&a设置退出语: &e/领地 退出语 <消息>"));
            p.sendMessage(Messages.color("&a清除: &e/领地 退出语 -"));
        } else if (raw == 26) {
            new ClaimDetailGui(plugin, c).open(p);
        }
    }
}
