val mc_version: String by extra
val geckolib_version: String by extra
val porting_lib_version: String by extra

fabric {
    dependOn(project(":common"))

    enableMixins()

    includesMod("io.github.fabricators_of_create.Porting-Lib:attributes:${porting_lib_version}+${mc_version}")
    includesMod("io.github.fabricators_of_create.Porting-Lib:entity:${porting_lib_version}+${mc_version}")
}

dependencies {
    modImplementation("software.bernie.geckolib:geckolib-fabric-${mc_version}:${geckolib_version}")
}