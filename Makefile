all: \
    Doc/exigence.pdf \
    Doc/scenarios.pdf \
    Doc/Specifications/specifications.pdf

%.pdf: %.md
	pandoc -s $< -o $@
%.pdf: %.tex
	cd $(dir $<) && latexmk -pdf $(notdir $<)

Doc/Specifications/specifications.pdf: \
    Doc/Planning\ Nouvelle\ Génération.png \
    Doc/Diagramme\ de\ classes\ du\ projet\ PNG.png \
    Doc/Specifications/Diag_activites_UC03.png \
    Doc/Specifications/Diag_activites_UC11.png \
    Doc/Specifications/gui-schema.png

Doc/Planning\ Nouvelle\ Génération.png: Doc/usecases.plantuml
	plantuml $<
Doc/Diagramme\ de\ classes\ du\ projet\ PNG.png: Doc/classes.plantuml
	plantuml $<
Doc/Specifications/Diag_activites_UC03.png: Doc/Specifications/Diag_activites_UC03.plantuml
	plantuml $<
Doc/Specifications/Diag_activites_UC11.png: Doc/Specifications/Diag_activites_UC11.plantuml
	plantuml $<
Doc/Specifications/gui-schema.png: Doc/Specifications/gui-schema.svg
	magick $< $@

LATEXMK_OUT := $(filter-out Doc/Specifications/specifications.tex,$(wildcard Doc/Specifications/specifications.*))

clean:
	-rm Doc/Diagramme\ de\ classes\ du\ projet\ PNG.png
	-rm Doc/Specifications/Diag_activites_UC03.png
	-rm Doc/Specifications/Diag_activites_UC11.png
	-rm Doc/Specifications/gui-schema.png
	-rm Doc/exigence.pdf
	-rm Doc/scenarios.pdf
	-rm $(LATEXMK_OUT)

.PHONY: all clean
