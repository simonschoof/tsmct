import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotestVersion = "5.9.0"
val kotestSpringVersion = "1.1.3"
val ktormVersion = "3.6.0"
val embeddedDbSpringTesVersion = "2.5.1"
val embeddedPostgresVersion = "2.0.7"
val jacksonKotlinModuleVersion = "2.17.1"
val postgresqlVersion = "42.7.3"
val kotlinLoggingVersion = "6.0.9"
val flywayVersion = "10.13.0"
val flywayTestVersion = "10.0.0"
val kediatrVersion = "3.0.0"

plugins {
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.5"
	id("com.adarshr.test-logger") version "4.0.0"
	id("org.flywaydb.flyway") version "10.13.0"
	id("com.github.ben-manes.versions") version "0.51.0"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
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
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	runtimeOnly("org.postgresql:postgresql:$postgresqlVersion")
	runtimeOnly("org.flywaydb:flyway-database-postgresql:$flywayVersion")
	implementation("org.flywaydb:flyway-core:$flywayVersion")
	implementation("org.ktorm:ktorm-core:$ktormVersion")
	implementation("org.ktorm:ktorm-support-postgresql:$ktormVersion")
	implementation("io.zonky.test:embedded-postgres:$embeddedPostgresVersion")

	implementation("io.github.oshai:kotlin-logging:$kotlinLoggingVersion")

	implementation("com.trendyol:kediatr-core:$kediatrVersion")
	implementation("com.trendyol:kediatr-spring-starter:$kediatrVersion")


	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
	testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
	testImplementation("io.kotest:kotest-property:$kotestVersion")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestSpringVersion")
	testImplementation("io.zonky.test:embedded-database-spring-test:$embeddedDbSpringTesVersion")
	testImplementation("org.flywaydb.flyway-test-extensions:flyway-spring-test:$flywayTestVersion")
}

configurations {
	implementation.configure {
		exclude(module = "spring-boot-starter-tomcat")
		exclude("org.apache.tomcat")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "21"
	}
}

testlogger {
	setTheme("mocha")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
