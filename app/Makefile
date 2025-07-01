.PHONY: build
.DEFAULT_GOAL := build-run


setup:
	./gradlew wrapper --gradle-version 8.7
clean:
	./gradlew clean
lint:
	./gradlew checkstyleMain
build:
	./gradlew clean build
install:
	./gradlew clean install
run-dist:
	./build/install/app/bin/app
run:
	./gradlew run	
test:
	./gradlew test
report:
	./gradlew jacocoTestReport
check-deps:
	./gradlew dependencyUpdates -Drevision=release	
update-deps:
	./gradlew useLatestVersions
	
build-run: build run
