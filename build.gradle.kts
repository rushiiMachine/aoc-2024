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
	implementation("me.carleslc:kotlin-extensions:0.8") // https://github.com/Carleslc/kotlin-extensions?tab=readme-ov-file
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
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
