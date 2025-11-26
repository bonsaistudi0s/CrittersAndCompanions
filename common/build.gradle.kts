val mc_version: String by extra
val geckolib_version: String by extra
val forge_config_port_version: String by extra

plugins {
    id("com.possible-triangle.common")
}

common {
    injectInterfaces()
}

dependencies {
    compileOnly("software.bernie.geckolib:geckolib-common-${mc_version}:${geckolib_version}")
    compileOnly("fuzs.forgeconfigapiport:forgeconfigapiport-common-neoforgeapi:${forge_config_port_version}")
}