val mc_version: String by extra
val geckolib_version: String by extra

forge {
    dependOn(project(":common"))

    enableMixins()
}

dependencies {
    modImplementation("software.bernie.geckolib:geckolib-forge-${mc_version}:${geckolib_version}")
}