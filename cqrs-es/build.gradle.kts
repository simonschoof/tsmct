import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotestVersion = "5.9.1"
val kotestSpringVersion = "1.3.0"
val ktormVersion = "4.1.1"
val embeddedDbSpringTesVersion = "2.5.1"
val embeddedPostgresVersion = "2.0.7"
val jacksonKotlinModuleVersion = "2.17.2"
val jacksonDataTypeModuleVersion = "2.17.2"
val postgresqlVersion = "42.7.4"
val kotlinLoggingVersion = "7.0.0"
val flywayVersion = "10.20.0"
val flywayTestVersion = "10.0.0"
val reactiveStreamVersion = "1.0.4"
val reactorCoreVersion = "3.6.11"
val kotlinxCoroutinesVersion = "1.9.0"
val mockkVersion = "1.13.13"
val springMockkVersion = "4.0.2"

plugins {
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("com.adarshr.test-logger") version "4.0.0"
	id("org.flywaydb.flyway") version "10.20.0"
	id("com.github.ben-manes.versions") version "0.51.0"
	kotlin("jvm") version "2.0.20"
	kotlin("plugin.spring") version "2.0.20"
}

group = "com.simonschoof"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jetty")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonKotlinModuleVersion")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonDataTypeModuleVersion")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.reactivestreams:reactive-streams:$reactiveStreamVersion")
	implementation("io.projectreactor:reactor-core:$reactorCoreVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$kotlinxCoroutinesVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinxCoroutinesVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$kotlinxCoroutinesVersion")

	runtimeOnly("org.postgresql:postgresql:$postgresqlVersion")
	runtimeOnly("org.flywaydb:flyway-database-postgresql:$flywayVersion")
	implementation("org.flywaydb:flyway-core:$flywayVersion")
	implementation("org.ktorm:ktorm-core:$ktormVersion")
	implementation("org.ktorm:ktorm-support-postgresql:$ktormVersion")
	implementation("io.zonky.test:embedded-postgres:$embeddedPostgresVersion")

	implementation("io.github.oshai:kotlin-logging:$kotlinLoggingVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
	testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
	testImplementation("io.kotest:kotest-property:$kotestVersion")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestSpringVersion")
	testImplementation("io.zonky.test:embedded-database-spring-test:$embeddedDbSpringTesVersion")
	testImplementation("org.flywaydb.flyway-test-extensions:flyway-spring-test:$flywayTestVersion")
	testImplementation("io.mockk:mockk:${mockkVersion}")
	testImplementation("com.ninja-squad:springmockk:${springMockkVersion}")
}

configurations {
	implementation.configure {
		exclude(module = "spring-boot-starter-tomcat")
		exclude("org.apache.tomcat")
	}
}

tasks.withType<KotlinCompile> {
	compilerOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = JvmTarget.JVM_21
	}
}

testlogger {
	setTheme("mocha")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
