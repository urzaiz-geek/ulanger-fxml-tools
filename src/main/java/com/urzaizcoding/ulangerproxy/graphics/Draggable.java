package com.urzaizcoding.ulangerproxy.graphics;

import javafx.scene.input.MouseEvent;

public interface Draggable {
	void handleMousePressed(MouseEvent event);

	void handleMousedDragged(MouseEvent event);

	default void handleMouseDraggedEnd(MouseEvent event) {

	}
	
	default void handleMouseDraggedStart(MouseEvent event) {
		
	}
}
