val mc_version: String by extra
val geckolib_version: String by extra
val easy_villager_version: String by extra
val jei_version: String by extra
val curios_version: String by extra

forge {
    dependOn(project(":common"))

    enableMixins()
}

// issues with mixin extras
tasks.withType<Test> { enabled = false }
tasks.compileTestJava { enabled = false }

dependencies {
    modImplementation("software.bernie.geckolib:geckolib-forge-${mc_version}:${geckolib_version}")
    modImplementation("top.theillusivec4.curios:curios-forge:${curios_version}")

    modRuntimeOnly("maven.modrinth:easy-villagers:${easy_villager_version}")
    modRuntimeOnly("mezz.jei:jei-${mc_version}-forge:${jei_version}")
}