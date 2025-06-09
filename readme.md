# 🔋 HealthBar Plugin

A lightweight Minecraft plugin that displays a BossBar showing the health of any entity a player hits.

![Bukkit](https://img.shields.io/badge/Bukkit-1.20%2B-blue?style=flat-square)
![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

---

## ✨ Features

- Displays target entity's remaining health in a BossBar
- Health bar changes color based on percentage (green, yellow, red)
- Auto-hides after a few seconds
- Fully configurable via `config.yml`

---

## ⚙️ Configuration

Example `config.yml`:

```yaml
bar:
  title: "&c%name% &7- &a%health%❤"
  color-high: GREEN
  color-medium: YELLOW
  color-low: RED
```
## Placeholders

- `%name%`: Target entity name
- `%health%`: Remaining health(integer)