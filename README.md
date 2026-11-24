# Co-Create Enchantments

Three armor enchantments designed with the community.

## Enchantments

### Gentle Descent

Boots enchantment. While worn, it keeps Slow Falling active; sneaking cancels the effect until you stop sneaking. It is mutually exclusive with Feather Falling.

### Undying Drop

Chest armor enchantment. When a fatal hit would kill you, the enchantment drops every carried and equipped item, consumes itself, and leaves you at one heart with Regeneration, Absorption, Fire Resistance, and Darkness. It is mutually exclusive with Demonic Pact and Mutual Destruction when those mods are present.

### Chinese

Chest armor enchantment. While worn, it grants creative flight and removes flight again when taken off. Taking damage in midair also consumes extra chestplate durability.

## Obtaining

These enchantments are not offered by the enchanting table. Apply them with `/enchant`, or use enchanted books from another source.

## Configuration

Server owners can tune durations, darkness, and durability costs in the generated config file. On Fabric, the same settings are available through the mod config screen.

## Building

```bash
./gradlew build
```

The loader-specific jars are written to `fabric/build/libs` and `neoforge/build/libs`.
