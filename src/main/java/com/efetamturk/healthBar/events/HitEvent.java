package com.efetamturk.healthBar.events;


import com.efetamturk.healthBar.HealthBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class HitEvent implements Listener {
    public HealthBar plugin;
    public HitEvent(HealthBar healthBar) {
        this.plugin = healthBar;
    }
    private BossBar bossbar;
    private void updateBossBar(Player player, LivingEntity entity, double finalHealth) {
        double maxHealth = entity.getAttribute(Attribute.MAX_HEALTH).getValue();
        double progress = Math.max(0, Math.min(1, finalHealth / maxHealth));
        bossbar.setProgress(progress);
        BarColor color;
        if (finalHealth / maxHealth > 0.66) {
            color = BarColor.valueOf(plugin.getConfig().getString("bar.color-high"));
        } else if (finalHealth / maxHealth > 0.33) {
            color = BarColor.valueOf(plugin.getConfig().getString("bar.color-medium"));
        } else {
            color = BarColor.valueOf(plugin.getConfig().getString("bar.color-low"));
        }
        bossbar.setColor(color);
    }

    @EventHandler
    public void playerHitEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity entity = event.getEntity();

            if (!(entity instanceof LivingEntity)) return;

            LivingEntity livingEntity = (LivingEntity) entity;
            double damage = event.getFinalDamage(); // Bu olayda uygulanacak hasar
            double finalHealth = Math.max(0, livingEntity.getHealth() - damage); // Hasar sonrası sağlık

            if (HealthBar.activeBars.containsKey(player)) {
                if (livingEntity.isDead() || finalHealth <= 0) {
                    HealthBar.activeBars.remove(player);
                    bossbar.removeAll();
                } else {
                    bossbar = HealthBar.activeBars.get(player);
                    String rawTitle = plugin.getConfig().getString("bar.title");
                    String formattedTitle = ChatColor.translateAlternateColorCodes('&',
                            rawTitle.replace("%name%", entity.getName())
                                    .replace("%health%", String.valueOf((int) finalHealth))
                    );
                    bossbar.setTitle(formattedTitle);

                    updateBossBar(player, livingEntity, finalHealth);
                    HealthBar.activeBars.put(player, bossbar);

                }
            } else {
                bossbar = Bukkit.createBossBar("Entity Health", BarColor.RED, BarStyle.SOLID);
                String rawTitle = plugin.getConfig().getString("bar.title");
                String formattedTitle = ChatColor.translateAlternateColorCodes('&',
                        rawTitle.replace("%name%", entity.getName())
                                .replace("%health%", String.valueOf((int) finalHealth))
                );
                bossbar.setTitle(formattedTitle);

                updateBossBar(player, livingEntity, finalHealth);
                HealthBar.activeBars.put(player, bossbar);
                bossbar.addPlayer(player);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    bossbar.removeAll();
                    HealthBar.activeBars.remove(player);
                }
            }.runTaskLater(plugin, 60L); // 3 saniye (60 tick)
        }


    }




}
