all: Exigences/exigence.pdf

%.pdf: %.md
	pandoc -s $< -o $@

clean:
	rm Exigences/exigence.pdf

.PHONY: all clean
