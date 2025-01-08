package com.project_gts.project_gts.controllers.forms;

import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MenuItem {
    private ImageView imageView;
    private Hyperlink link;
    private String imagePath;
    private String color;

    public MenuItem(ImageView imageView, Hyperlink link, String imagePath, String color) {
        this.imageView = imageView;
        this.link = link;
        this.imagePath = imagePath;
        this.color = color;
    }

    public void setColor(String color) {
        this.color = color;
        link.setStyle("-fx-text-fill: " + color);
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        imageView.setImage(new Image("/com/project_gts/project_gts/images/" + imagePath));
    }

    public void updateItem() {
        imageView.setImage(new Image("/com/project_gts/project_gts/images/" + imagePath));
        link.setStyle("-fx-text-fill: " + color);
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getColor() {
        return color;
    }
}



