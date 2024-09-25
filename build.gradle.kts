plugins {
    id("com.possible-triangle.gradle") version ("0.1.5")
}

subprojects {
    repositories {
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
                //includeGroup("io.github.tropheusj")
            }
        }

        maven {
            url = uri("https://maven.jamieswhiteshirt.com/libs-release")
            content {
                includeGroup("com.jamieswhiteshirt")
            }
        }
    }
}