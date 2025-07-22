val mc_version: String by extra
val geckolib_version: String by extra
val forge_config_port_version: String by extra

plugins {
    id("dev.architectury.loom") version ("1.10-SNAPSHOT")
}

common {
    applyVanillaGradle = false
}

dependencies {
    "minecraft"("com.mojang:minecraft:${mc_version}")
    "mappings"(loom.officialMojangMappings())

    modCompileOnly("software.bernie.geckolib:geckolib-fabric-${mc_version}:${geckolib_version}")
    compileOnly("fuzs.forgeconfigapiport:forgeconfigapiport-common:${forge_config_port_version}")
}