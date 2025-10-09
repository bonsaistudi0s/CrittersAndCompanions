val mc_version: String by extra
val geckolib_version: String by extra
val forge_config_port_version: String by extra

plugins {
    id("com.possible-triangle.architectury")
}

dependencies {
    modCompileOnly("software.bernie.geckolib:geckolib-fabric-${mc_version}:${geckolib_version}")
    compileOnly("fuzs.forgeconfigapiport:forgeconfigapiport-common:${forge_config_port_version}")
}