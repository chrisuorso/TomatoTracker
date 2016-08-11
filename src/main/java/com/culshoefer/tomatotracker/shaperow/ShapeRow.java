package com.culshoefer.tomatotracker.shaperow;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 08/08/16.
 */
public class ShapeRow extends Pane implements IntegerNumberDisplay {
    private int numIcons;
    private double distBetweenShapes;
    private ShapeFactory shapeF;

    public ShapeRow() {
        super();
    }

    public ShapeRow(ShapeFactory shapeF) {
        this(shapeF, 0, 0);
    }

    public ShapeRow(ShapeFactory shapeF, int numIcons, int distBetweenShapes) {
        super();
        this.shapeF = shapeF;
        this.numIcons = numIcons;
        this.distBetweenShapes = distBetweenShapes;
    }

    private void genNumIcons() {
        for (int i = 0; i < numIcons; i++) {
            Shape toAdd = shapeF.getInstance();
            toAdd.setLayoutX(toAdd.getLayoutX() + distBetweenShapes * i);
            this.getChildren().add(toAdd);
        }
    }

    private void setNumIcons(int numIcons) {
        this.numIcons = numIcons;
        this.getChildren().clear();
        this.genNumIcons();
    }

    public void setDistBetweenShapes(double distBetweenShapes) {
        this.distBetweenShapes = distBetweenShapes;
    }

    @Override
    public void displayNum(int num) {
        this.setNumIcons(num);
        this.genNumIcons();
    }
}
