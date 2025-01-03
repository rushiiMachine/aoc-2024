plugins {
	kotlin("jvm") version "2.1.0"
}

repositories {
	mavenCentral()
	maven(url = "https://jitpack.io")
}

dependencies {
	implementation("com.google.guava:guava:33.3.1-jre")
	implementation("io.arrow-kt:arrow-collectors:1.2.4")
	implementation("io.arrow-kt:arrow-core:1.2.4")
	implementation("io.arrow-kt:arrow-eval:1.2.4")
	implementation("io.arrow-kt:arrow-fx-coroutines:1.2.4")
	implementation("me.carleslc:kotlin-extensions:0.8") // https://github.com/Carleslc/kotlin-extensions
	implementation("org.bouncycastle:bcprov-lts8on:2.73.7")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
	implementation("org.jetbrains.kotlinx:multik-core:0.2.3")
	implementation("org.jetbrains.kotlinx:multik-default:0.2.3")
	implementation(project(":KotlinDiscreteMathToolkit")) // https://github.com/MarcinMoskala/KotlinDiscreteMathToolkit
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll(
			"-Xsuppress-warning=NOTHING_TO_INLINE",
		)
	}
}

tasks {
	sourceSets {
		main {
			java.srcDirs("src")
		}
	}

	wrapper {
		gradleVersion = "8.11.1"
	}
}
