plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.springdoc.openapi-gradle-plugin") version "1.9.0"
}

group = "lv.fx"
version = "0.2.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {

	implementation ("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	//For WebFlux non-blocking request handling
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-reactor-netty")
	//OpenApi 3.1.0 documentation (Swagger)
	implementation("org.springdoc:springdoc-openapi-starter-webflux-api:2.6.0")


	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("org.postgresql:postgresql")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")

	//Frontend
	implementation ("org.springframework.boot:spring-boot-starter-mustache")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.assertj:assertj-core:3.21.0")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
