import com.google.protobuf.gradle.id

plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.google.protobuf") version "0.9.4" // Latest Protobuf plugin version
}

group = "charitan-go"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
//	maven { url "https://repo.spring.io/milestone" }
}


protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc"
	}
	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java"
		}
	}
	generateProtoTasks {
		all().forEach {
			it.plugins {
				id("grpc") {
					option("jakarta_omit")
					option("@generated=omit")
				}
			}
		}
	}
}

// Add generated sources to the main source set
sourceSets {
	main {
		java {
			srcDir("src/generated/main/java")
		}
	}
}

extra["springCloudVersion"] = "2024.0.0"
extra["springGrpcVersion"] = "0.3.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")
	implementation("org.springframework.cloud:spring-cloud-starter-gateway:4.2.0")

	// https://mvnrepository.com/artifact/org.springframework.amqp/spring-amqp
	implementation("org.springframework.boot:spring-boot-starter-amqp:3.4.2")

	// Grpc
	// implementation("io.grpc:grpc-services")
	// implementation("org.springframework.grpc:spring-grpc-spring-boot-starter")

	// https://mvnrepository.com/artifact/net.devh/grpc-client-spring-boot-starter
	// For Spring Boot 3.x
	implementation("net.devh:grpc-spring-boot-starter:3.0.0.RELEASE")
	implementation("io.grpc:grpc-netty:1.62.2")  // Explicit netty dependency
	implementation("io.grpc:grpc-protobuf:1.62.2")
	implementation("io.grpc:grpc-stub:1.62.2")

	// For Protobuf
	implementation("com.google.protobuf:protobuf-java:3.25.1")



	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
		mavenBom("org.springframework.grpc:spring-grpc-dependencies:${property("springGrpcVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	//enabled = false
}
