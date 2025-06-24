.PHONY: build
.DEFAULT_GOAL := build-run

clean:
	make -C app clean
lint:
	make -C app lint
build:
	make -C app build
test:
	make -C app test
report:
	make -C app report
update-deps:
	make -C app update-deps
build-run: build run
