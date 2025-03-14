package fr.univtln.yroblin156;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Grille {
    // Taille de la grille
    private int width;
    private int height;
    private Group root;
    private Canvas grille;
    private GraphicsContext gc;

    public Grille(Group root,int width, int height) {
        this.width = width;
        this.height = height;
        this.root = root;
        this.grille = new Canvas(this.width , this.height);
        this.gc = grille.getGraphicsContext2D();
        // Fond gris
        this.gc.setFill(Color.GRAY);
        this.gc.fillRect(0, 0, this.width, this.height);
        // Lignes
        this.gc.setStroke(Color.BLACK);
        for (int i = 0; i < this.width; i += this.width / 6) {
            for (int j = 0; j < this.height; j += this.height / 6) {
                this.gc.strokeRect(i, j, i+this.width / 6, j+this.height / 6);
            }
        }
        this.root.getChildren().add(this.grille);

    }

    public Group getRoot() {
        return root;
    }

    public Canvas getGrille() {
        return grille;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setRoot(Group root) {
        this.root = root;
    }

    public void setGrille(Canvas grille) {
        this.grille = grille;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }



    
}
