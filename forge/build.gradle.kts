architectury {
    forge()
}

loom {
    forge {
        mixinConfig("crittersandcompanions-common.mixins.json")
    }
    runs {
        create("data") {
            data()
            programArgs("--all", "--mod", "crittersandcompanions")
            programArgs("--output", project(":common").file("src/main/generated/resources").absolutePath)
            programArgs("--existing", project(":common").file("src/main/resources").absolutePath)
        }
    }
}

val common: Configuration by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
    configurations["developmentForge"].extendsFrom(this)
}

dependencies {
    val bytecodecsVersion: String by project

    common(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowCommon(project(path = ":common", configuration = "transformProductionForge")) {
        isTransitive = false
    }

    val minecraftVersion: String by project
    val forgeVersion: String by project

    forge(group = "net.minecraftforge", name = "forge", version = "$minecraftVersion-$forgeVersion")
    forgeRuntimeLibrary("com.teamresourceful:bytecodecs:$bytecodecsVersion")
    forgeRuntimeLibrary("com.eliotlash.mclib:mclib:20")
}
