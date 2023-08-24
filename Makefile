#.DEFAULT_GOAL := build-run

clean:
	gradle clean

build:
	gradle build

install:
	./gradlew clean install

run-dist:
	./build/install/java-package/bin/java-package

run:
	gradle :run


test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain

update-deps:
	./gradlew useLatestVersions


#build-run: clean build run

.PHONY: build