# TinyClaim 领地保护插件

轻量高效、零依赖的 Minecraft 领地保护插件。四象限索引 + 方块级缓存（纳秒级查询）、Flag 权限系统、三种圈地方式、全局面板管理、子领地/模板/成员/欢迎语、经济联动。全中文界面，中英双语命令。

## 功能特性

- **三种圈地方式**：木锄头右键选区（粒子预览）/ 指令圈地（完整 3D 坐标）/ 半径圈地
- **Flag 权限系统**：12 个环境 flag（苦力怕爆炸/TNT/火焰/岩浆/水流/活塞/怪物破坏/踩踏/生成...）+ 10 个权限 flag（建造/交互/容器/PVP/攻击怪物...），config 可配默认值，领地级覆盖
- **纳秒级空间索引**：四象限分区 + 方块级 LRU 缓存，大服不卡
- **全局面板**：管理名下所有领地，全局权限成员（所有领地生效）+ 单领地成员（仅本领地）
- **领地详情页**：领地设置（flag 开关）/ 子领地 / 领地管理员设置 / 欢迎语+退出语设置 / 成员信息
- **边界粒子**：火把火光，六面全包围或 12 条边线可选（config `boundary-style`）
- **进出提示**：进入/退出领地 ActionBar 欢迎语/欢送语 + 边界粒子（2.3 秒自动消失）
- **子领地**：父领地内划子区域，子领地 flag 独立
- **模板**：预置 flag 组合（家园/战争/和平），一键应用
- **成员管理**：添加/删除成员，被添加者在线收到提示
- **经济联动**：创建/扩展领地扣费（自家 Economy → Vault → 免费，零硬依赖）
- **中英双语命令**：全部功能中文+英文别名
- **数据持久化**：claims.yml（领地/成员/flag/全局权限/欢迎语），重启不丢

## 安装

1. 下载 jar 放入 `plugins/` 目录
2. 重启服务器（或面板 reload）
3. 启动日志显示 TinyAII 横幅 + 已加载 N 个领地

> 需要 Java 17+，支持 Paper/Spigot 1.16 ~ 26.2。

## 配置

`plugins/TinyClaim/config.yml`

- `settings.boundary-style`：`faces`（六面全包围）/ `edges`（12 条边线）
- `settings.max-claims-default`：普通玩家默认领地数（默认 3）
- `settings.display-duration-seconds`：选区预览粒子时长（默认 10 秒）
- `settings.enter-leave-duration-seconds`：进出领地粒子时长（默认 2.3 秒）
- `flags.default-environment` / `flags.default-privilege`：flag 默认值（外人默认全关）
- `templates`：flag 模板（家园/战争/和平）

## 命令

### 圈地
| 命令 | 说明 |
|---|---|
| `/领地 创建 <名>` | 用木锄头选两点后创建 |
| `/领地 圈地 <名> <x1> <y1> <z1> <x2> <y2> <z2>` | 指令圈地（完整 3D 坐标） |
| `/领地 半径 <名> <半径> [y1] [y2]` | 半径圈地（以玩家为中心） |

### 管理
| 命令 | 说明 |
|---|---|
| `/领地 面板` | 打开全局面板 |
| `/领地 信息 [领地名]` | 查看领地信息 |
| `/领地 传送 <领地名>` | 传送到领地中心 |
| `/领地 删除 [确认]` | 删除当前领地 |
| `/领地 列表` | 我的领地 |
| `/领地 成员 添加\|移除 <玩家> [admin]` | 管理成员 |
| `/领地 flag 设置 <名> <开\|关>` | 开关 flag |
| `/领地 子 创建\|删除\|列表` | 子领地管理 |
| `/领地 模板 应用 <名>` | 应用 flag 模板 |
| `/领地 扩展 <方向> <格数>` | 扩展领地（上/下/北/南/东/西） |
| `/领地 欢迎语 <消息>` | 设置进入欢迎语 |
| `/领地 退出语 <消息>` | 设置退出语 |
| `/领地 管理 删除 <玩家>` | 删除他人领地（管理） |

### 权限
- `claim.use`：使用领地功能（默认 true）
- `claim.admin`：领地管理（默认 op）
- `claim.bypass`：无视所有领地保护（默认 op）
- `claim.limit.vip` / `claim.limit.svip`：领地数量上限

## 兼容性

- Paper / Spigot 1.16 ~ 26.2
- 零依赖，不装任何前置也能跑
- 经济联动：装了自家 Economy 或 Vault 自动扣费，都没装则免费

---

# TinyClaim - Territory Protection Plugin

Lightweight, zero-dependency Minecraft territory protection plugin. Quadrant-indexed spatial cache, flag permission system, three claim methods, global panel management, sub-claims/templates/members/welcome messages, economy integration. Full Chinese UI with bilingual commands.

## Features

- **3 Claim Methods**: Wooden hoe selection (particle preview) / command coords (full 3D) / radius claim
- **Flag System**: 12 env flags (creeper/TNT/fire/lava/water/piston/mob-grief/trample/spawn...) + 10 privilege flags (build/interact/container/pvp/attack...)
- **Nanosecond Spatial Index**: quadrant sectors + block-level LRU cache
- **Global Panel**: manage all your claims, global admins (all claims) + per-claim members
- **Claim Detail Page**: settings (flags) / sub-claims / admin setup / welcome+leave messages / member info
- **Boundary Particles**: torch flame, six-face or 12-edge (config `boundary-style`)
- **Enter/Leave Notifications**: ActionBar welcome/leave + particles (auto-hide in 2.3s)
- **Sub-Claims**: carve areas inside a claim, independent flags
- **Templates**: preset flag groups (home/war/peace)
- **Economy**: claim/expand cost (own Economy → Vault → free, zero hard dependency)

## Install

1. Put jar into `plugins/`
2. Restart server (or panel reload)
3. Startup log shows TinyAII banner + loaded claims count

> Java 17+, Paper/Spigot 1.16 ~ 26.2.

## License

MIT License - free, open source. TinyAII brand banner preserved.
