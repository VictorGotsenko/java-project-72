.PHONY: build
.DEFAULT_GOAL := build-run

clean:
	./gradlew clean
lint:
	./gradlew checkstyleMain
build:
	./gradlew clean build
test:
	./gradlew test
report:
	./gradlew jacocoTestReport
update-deps:
	./gradlew useLatestVersions
build-run: build run
