GRADLEW = ./gradlew
JAR_FILE = build/libs/Idea-Platform-test-1.0-SNAPSHOT.jar
default: run

clean:
	./gradlew clean

build:
	./gradlew build

clean-build: clean build

install:
	./gradlew clean install

run:
	./gradlew run

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain

update-deps:
	./gradlew useLatestVersions


build-run: clean build run

.PHONY: build