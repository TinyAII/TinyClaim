package nl.tinyaii.tinyclaim;

import nl.tinyaii.tinyclaim.data.ClaimManager;
import nl.tinyaii.tinyclaim.events.EnvEvents;
import nl.tinyaii.tinyclaim.events.PlayerEvents;
import nl.tinyaii.tinyclaim.flags.FlagCheck;
import nl.tinyaii.tinyclaim.storage.Storage;
import nl.tinyaii.tinyclaim.util.EcoBridge;
import nl.tinyaii.tinyclaim.util.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public class TinyClaimPlugin extends JavaPlugin {

    private Storage storage;
    private ClaimManager claimManager;
    private FlagCheck flagCheck;
    private EcoBridge ecoBridge;
    private nl.tinyaii.tinyclaim.command.SelectionManager selectionManager;

    @Override
    public void onEnable() {
        // TinyAII 品牌横幅
        getLogger().info(" _____ _                _    ___ ___");
        getLogger().info("|_   _(_)_ __  _   _   / \\  |_ _|_ _|");
        getLogger().info("  | | | | '_ \\| | | | / _ \\  | | | |");
        getLogger().info("  | | | | | | | |_| |/ ___ \\ | | | |");
        getLogger().info("  |_| |_|_| |_|\\__, /_/   \\_\\___|___|");
        getLogger().info("               |___/");
        getLogger().info("TinyClaim 领地插件 v" + getDescription().getVersion() + " - TinyAII 出品");

        saveDefaultConfig();

        storage = new Storage(this);
        storage.open();

        claimManager = new ClaimManager(this);
        storage.loadAll(claimManager);

        flagCheck = new FlagCheck(this);

        getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
        getServer().getPluginManager().registerEvents(new EnvEvents(this), this);
        selectionManager = new nl.tinyaii.tinyclaim.command.SelectionManager(this);
        getServer().getPluginManager().registerEvents(new nl.tinyaii.tinyclaim.events.SelectionEvents(this), this);
        getServer().getPluginManager().registerEvents(new nl.tinyaii.tinyclaim.events.EnterListener(this), this);
        getServer().getPluginManager().registerEvents(new nl.tinyaii.tinyclaim.gui.ClaimGuiListener(this), this);
        getServer().getPluginManager().registerEvents(new nl.tinyaii.tinyclaim.gui.MemberGuiListener(this), this);
        getServer().getPluginManager().registerEvents(new nl.tinyaii.tinyclaim.gui.GlobalPanelGuiListener(this), this);
        getServer().getPluginManager().registerEvents(new nl.tinyaii.tinyclaim.gui.ClaimListGuiListener(this), this);
        getServer().getPluginManager().registerEvents(new nl.tinyaii.tinyclaim.gui.ClaimDetailGuiListener(this), this);
        getServer().getPluginManager().registerEvents(new nl.tinyaii.tinyclaim.gui.WelcomeGuiListener(this), this);
        getServer().getPluginManager().registerEvents(new nl.tinyaii.tinyclaim.gui.SubClaimGuiListener(this), this);

        getCommand("领地").setExecutor(new nl.tinyaii.tinyclaim.command.ClaimCommand(this));

        ecoBridge = new EcoBridge(this);

        getLogger().info("TinyClaim 已启用，已加载 " + claimManager.getAll().size() + " 个领地。");
    }

    @Override
    public void onDisable() {
        if (storage != null) storage.close();
    }

    public Storage getStorage() { return storage; }
    public ClaimManager getClaimManager() { return claimManager; }
    public FlagCheck getFlagCheck() { return flagCheck; }
    public EcoBridge getEcoBridge() { return ecoBridge; }
    public nl.tinyaii.tinyclaim.command.SelectionManager getSelectionManager() { return selectionManager; }
}
