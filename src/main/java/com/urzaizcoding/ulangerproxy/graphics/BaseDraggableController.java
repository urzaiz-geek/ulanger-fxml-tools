package com.urzaizcoding.ulangerproxy.graphics;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

public class BaseDraggableController extends BaseController implements Draggable {

	@FXML
	protected Node root;

	private double xOffset, yOffset;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		root.setOnMousePressed(e -> handleMousePressed(e));
		root.setOnMouseDragged(e -> handleMousedDragged(e));
		root.setOnMouseDragEntered(e -> handleMouseDraggedStart(e));
		root.setOnMouseDragExited(e -> handleMouseDraggedEnd(e));

	}

	@Override
	public void handleMousePressed(MouseEvent event) {
		xOffset = event.getSceneX();
		yOffset = event.getSceneY();
	}

	@Override
	public void handleMousedDragged(MouseEvent event) {

		Window stage = root.getScene().getWindow();
		stage.setX(event.getScreenX() - xOffset);
		stage.setY(event.getScreenY() - yOffset);
	}

}
