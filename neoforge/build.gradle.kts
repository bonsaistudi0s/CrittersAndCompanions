val mc_version: String by extra
val geckolib_version: String by extra
val easy_villager_version: String by extra
val jei_version: String by extra
val curios_version: String by extra
val pathfinding_renderer_version: String by extra
val jade_version: String by extra

plugins {
    id("com.possible-triangle.neoforge")
}

neoforge {
    dependOn(project(":common"))
    injectInterfaces()
    dataGen {
        splitSourceSet()
    }
}

dependencies {
    modImplementation("software.bernie.geckolib:geckolib-neoforge-${mc_version}:${geckolib_version}")
    modImplementation("top.theillusivec4.curios:curios-neoforge:${curios_version}")

    modRuntimeOnly("maven.modrinth:easy-villagers:${easy_villager_version}")
    modRuntimeOnly("mezz.jei:jei-${mc_version}-neoforge:${jei_version}")
    modRuntimeOnly("maven.modrinth:jade:${jade_version}")

    modRuntimeOnly("curse.maven:debug-utils-783008:6800027")
}