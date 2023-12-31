import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotestVersion = "5.7.2"
val kotestSpringVersion = "1.1.3"
val ktormVersion = "3.6.0"

plugins {
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	id("com.adarshr.test-logger") version "3.2.0"
	id("org.flywaydb.flyway") version "9.22.1"
	id("com.github.ben-manes.versions") version "0.48.0"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "com.simonschoof"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jetty")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	runtimeOnly("org.postgresql:postgresql")
	implementation("org.flywaydb:flyway-core:9.22.2")
	implementation("org.ktorm:ktorm-core:$ktormVersion")
	implementation("org.ktorm:ktorm-support-postgresql:$ktormVersion")
	implementation("com.h2database:h2:2.2.224")


	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
	testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
	testImplementation("io.kotest:kotest-property:$kotestVersion")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestSpringVersion")
	// Use in tests until fixed: https://github.com/spring-projects/spring-boot/issues/33044
	testImplementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
}

configurations {
	implementation.configure {
		exclude(module = "spring-boot-starter-tomcat")
		exclude("org.apache.tomcat")
	}
}

// Downgrade until fixed: https://github.com/spring-projects/spring-boot/issues/33044
extra.apply{
	set("jakarta-servlet.version", "5.0.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

testlogger {
	setTheme("mocha")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
