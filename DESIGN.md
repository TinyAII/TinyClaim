# TinyClaim 领地插件 设计方案 v1.0

> 目标：完整实现 Dominion 的核心能力（领地保护/Flag/子领地/成员/模板/经济联动），
> 但代码全新原创，不照抄 Dominion。技术基线对齐全家桶（零硬依赖/中英双语/神权命令）。

## 一、架构分层

```
TinyClaimPlugin（主类：初始化/开关/横幅）
├── cache/      空间索引（四象限分区 + 方块LRU缓存）★性能核心
├── data/       领地数据模型（Claim/Cuboid/FlagValue...）
├── storage/    持久化（SQLite 起步，预留 MySQL）
├── flags/      Flag 定义 + 检查核心（环境/权限）
├── events/     事件拦截（玩家 + 环境 + 实体）
├── command/    命令系统（中文/英文双语）
├── gui/        领地面板 GUI（ChestUI 风格）
└── util/       工具（Messages/安全落点/选择点...）
```

## 二、核心数据结构

### Cuboid（立方体）
- world, minX, minY, minZ, maxX, maxY, maxZ
- `contain(x,y,z)` 包含判断
- `intersect(other)` 重叠判断（防重叠用）

### Claim（领地）
- id, name, owner(UUID)
- cuboid（父领地范围）
- parentId（子领地父引用，null=顶级）
- 成员表：uuid → 角色（owner/admin/member）
- flag 覆盖表：flagName → boolean（null=继承/默认）
- createdAt

### FlagValue（解析链）
- 全局默认（config）→ 世界默认 → 领地覆盖 → 子领地覆盖
- 权限检查：owner > admin > member > 非成员（按 flag 语义）

## 三、空间索引（★ Dominion 性能核心的原创实现）

### 四象限分区
- 以世界原点 (0,0) 分四个扇区 A(x>=0,z>=0) / B(x<0,z>=0) / C(x>=0,z<0) / D(x<0,z<0)
- 每扇区一个 Map<World, List<ClaimNode>>
- 查询：O(1) 定位扇区 → 遍历该区领地做 contain 判断

### 方块级 LRU 缓存
- Map<World, Map<BlockKey, Integer>>，BlockKey=(x,y,z)
- 上限 65536 条，满了整体清空重建
- 命中直接返回领地ID，未命中查完写入

### 父子递归查询
- 先找父，再在子领地中递归，子领地优先命中

## 四、Flag 系统

### 环境 Flag（EnvFlag）— 影响世界/实体，不是玩家
creeper-explosion / tnt-explosion / fire-spread / lava-flow / water-flow /
piston-push / mob-grief / mob-trample / animal-trample / item-drop / mob-spawn / animal-spawn

### 权限 Flag（PriFlag）— 决定玩家能不能做
build / interact / container / pvp / attack-monster / attack-animal /
use-elytra / use-redstone / enter / chest-access

### 检查核心（与 Dominion 思路同构但独立实现）
```java
// 环境：查坐标所在领地（无领地查世界默认），flag false → event.setCancelled(true)
checkEnvFlag(location, flag, event)
// 权限：查领地 → 查玩家角色 → 查 flag
checkPriFlag(location, flag, player, event)
```

## 五、事件拦截清单

### 玩家事件（events/player）
- 破坏方块 BlockBreak / 放置 BlockPlace / 液体取出
- 交互 PlayerInteract（箱子/门/按钮/工作台）
- 容器操作 InventoryOpenEvent → container flag
- 攻击 EntityDamageByEntity（pvp / attack-monster / attack-animal）
- 使用物品（桶/打火石/末影珍珠）

### 环境事件（events/environment）
- 爆炸（苦力怕/TNT/床）→ 方块破坏过滤
- 火焰蔓延 BlockBurn / 液体流动 BlockFromTo
- 活塞推出 BlockPistonExtend/Retract（检查目标侧）
- 怪物踩踏 EntityInteract（耕地）
- 末影人搬方块/凋灵破坏

### 通用拦截模式
```java
@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
public void onBreak(BlockBreakEvent e) {
    if (bypass(e.getPlayer())) return;
    checkPriFlag(e.getBlock().getLocation(), Flags.BUILD, e.getPlayer(), e);
}
```

## 六、命令系统（双语）

主命令 `/领地`（/claim /land）

| 功能 | 中文 | 英文 |
|---|---|---|
| 创建（选择两点后）| /领地 创建 <名> | /claim create <name> |
| 删除 | /领地 删除 [确认] | /claim delete [confirm] |
| 扩展 | /领地 扩展 <方向> <格数> | /claim expand <dir> <n> |
| 信息 | /领地 信息 | /claim info |
| 列表 | /领地 列表 | /claim list |
| 选择 | /领地 选择 开始/结束 | /claim select |
| 成员 | /领地 成员 添加/移除 <玩家> | /claim member add/remove <p> |
| Flag | /领地 flag 设置 <名> <值> | /claim flag set <name> <value> |
| 子领地 | /领地 子 创建 <名> | /claim child create |
| 模板 | /领地 模板 应用 <名> | /claim template apply |
| 传送 | /领地 传送 | /claim tp |
| 管理 | /领地 管理 删除 <玩家> | /claim admin delete <p> |
| 重载 | /领地 重载 | /claim reload |

## 七、经济联动

- softdepend Economy/Vault（自家 Economy 优先，Vault 次之，都没有则免费）
- 创建领地扣 claim-cost；扩展按方块扣费
- EcoBridge 复用全家桶模式（反射调 EconomyAPI / Vault）

## 八、存储

- SQLite（`claims.db`）：claims 表 + members 表 + flags 表 + child 关系
- 启动加载全部进内存缓存；写操作实时落库
- 预留 MySQL（数据库类型可配）

## 九、GUI（领地面板）

- 打开领地 → 菜单显示：名称/所有者/范围/成员/flag 开关
- 子菜单：成员管理（头像+角色）、flag 开关（绿/红染料）、子领地列表
- 全中文 + ASCII 安全（不用 emoji，遵守全家桶字体约定）

## 十、实现顺序

1. 骨架：主类/pom/plugin.yml/config（✅ 本次）
2. 数据模型：Cuboid/Claim/FlagValue
3. 空间索引：四象限 + LRU 缓存
4. 存储：SQLite 加载/保存
5. Flag 定义 + 检查核心
6. 选择系统 + 创建/删除/扩展
7. 事件拦截（玩家 + 环境）
8. 命令系统（全部双语）
9. GUI 面板
10. 经济联动 + 模板 + 神权管理
11. 编译部署 + 测试清单
