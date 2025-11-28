val mc_version: String by extra
val geckolib_version: String by extra
val porting_lib_version: String by extra
val trinkets_version: String by extra
val forge_config_port_version: String by extra

plugins {
    id("com.possible-triangle.fabric")
}

mod {
    mods.include("io.github.fabricators_of_create.Porting-Lib:attributes:${porting_lib_version}+${mc_version}")
    mods.include("io.github.fabricators_of_create.Porting-Lib:entity:${porting_lib_version}+${mc_version}")
    mods.include("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:${forge_config_port_version}")
}

fabric {
    dependOn(project(":common"))
}

dependencies {
    modImplementation("software.bernie.geckolib:geckolib-fabric-${mc_version}:${geckolib_version}")
    modImplementation("dev.emi:trinkets:${trinkets_version}")
}