all: \
    Exigences/exigence.pdf \
    Scénarios/scenarios.pdf \
    Diagrammes/Planning\ Nouvelle\ Génération.png

%.pdf: %.md
	pandoc -s $< -o $@

Diagrammes/Planning\ Nouvelle\ Génération.png: Diagrammes/usecases.plantuml
	plantuml $<

clean:
	rm Exigences/exigence.pdf
	rm Scénarios/scenarios.pdf
	rm Diagrammes/Planning\ Nouvelle\ Génération.png

.PHONY: all clean
