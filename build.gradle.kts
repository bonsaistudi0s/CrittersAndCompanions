plugins {
    id("com.possible-triangle.core")
    id("com.possible-triangle.fabric") apply false
    id("com.possible-triangle.forge") apply false
    id("com.possible-triangle.common") apply false
}

subprojects {
    repositories {
        nexus {
            content {
                includeGroup("com.possible-triangle")
                includeGroup("com.ninni.dye_depot")
            }
        }

        maven {
            url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
            content {
                includeGroup("software.bernie.geckolib")
                includeGroup("com.eliotlash.mclib")
            }
        }

        maven {
            url = uri("https://mvn.devos.one/releases/")
            content {
                includeGroup("io.github.fabricators_of_create.Porting-Lib")
                // includeGroup("io.github.tropheusj")
            }
        }

        maven {
            url = uri("https://maven.jamieswhiteshirt.com/libs-release")
            content {
                includeGroup("com.jamieswhiteshirt")
            }
        }

        maven {
            url = uri("https://maven.blamejared.com/")
            content {
                includeGroup("mezz.jei")
            }
        }

        maven {
            url = uri("https://maven.theillusivec4.top/")
            content {
                includeGroup("top.theillusivec4.curios")
            }
        }

        maven {
            url = uri("https://maven.terraformersmc.com/")
            content {
                includeGroup("dev.emi")
            }
        }

        maven {
            url = uri("https://maven.ladysnake.org/releases")
            content {
                includeGroup("dev.onyxstudios.cardinal-components-api")
            }
        }

        maven {
            url = uri("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
            content {
                includeGroup("net.minecraftforge")
                includeGroup("fuzs.forgeconfigapiport")
            }
        }
    }
}

enableSpotless()
