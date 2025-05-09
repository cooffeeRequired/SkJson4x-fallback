plugins {
    id("java")
}

group = "cz.coffee.fallbackapi"
version = "4.x"

repositories {
    mavenCentral()
    maven("https://repo.skriptlang.org/releases")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven { url = uri("https://jitpack.io") }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("com.github.SkriptLang:Skript:2.11.0-pre1")
    compileOnly(files("libraries/skjson.jar"))

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    archiveBaseName.set("skjson")
    archiveExtension.set("fallback")
    doLast {
        print("! > Copy")
        copy {
            from(archiveFile)
            into("C:\\Users\\Coffee\\Desktop\\mc-developing\\plugins\\SkJson\\libraries")
        }
    }
}