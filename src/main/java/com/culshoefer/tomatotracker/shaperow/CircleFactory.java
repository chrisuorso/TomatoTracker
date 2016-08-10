package com.culshoefer.tomatotracker.shaperow;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 10/08/16.
 */
public class CircleFactory implements ShapeFactory {
    @Override
    public Circle getInstance() {
        Circle c = new Circle();
        c.setFill(Color.rgb(187, 187, 191));
        c.setRadius(5);
        c.setLayoutX(5);
        c.setLayoutY(5);
        c.setSmooth(true);
        return c;
    }
}
