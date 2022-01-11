package com.urzaizcoding.ulangerproxy.graphics;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GraphicFacilities {

	private static NotificationApparence notificationStyle;

	static {
		setNotificationStyle(new NotificationApparence());
	}

	public interface PrefSizes {

		Double getPrefHeight();

		Double getPrefWidth();

	}

	public static class NotificationApparence {

		public static final String DEFAULT_BOX_ID = "main";
		public static final String DEFAULT_LABEL_CLOSE_ID = "label-close";
		public static final String DEFAULT_LABEL_NOTIFICATION_ID = "label-notification";

		public static final String BOX_PROPERTY_KEY = "box";
		public static final String LABEL_CLOSE_PROPERTY_KEY = "close";
		public static final String LABEL_NOTIFICATION_PROPERTY_KEY = "message";
		public static final String DEFAULT_TOOLTIP_CLOSE_MESSAGE = "fermer";

		private String cssStyleSheet;
		private Properties cssIdProperties;
		private Double notificationDuration = 0.4;
		private Long closingDelay = 3000L;
		private String tooltipCloseMessage = DEFAULT_TOOLTIP_CLOSE_MESSAGE;
		private PrefSizes labelClosePrefSizes;
		private PrefSizes labelMessagePrefSizes;
		private PrefSizes boxPrefsizes;
		private boolean autoClosure;

		public NotificationApparence(String cssStyleSheet, Properties cssIdProperties, Double duration, Long delay,
				String tooltipMessageClose) {
			super();
			initSizes();
			this.cssStyleSheet = cssStyleSheet;
			this.cssIdProperties = cssIdProperties;
			this.notificationDuration = duration;
			this.setClosingDelay(delay);
			this.tooltipCloseMessage = tooltipMessageClose;
			
		}

		public NotificationApparence(String cssStyleSheet) {
			super();
			initSizes();
			this.cssStyleSheet = cssStyleSheet;

			propertiesNotProvided();
		}

		public NotificationApparence() {
			super();
			initSizes();
			styleSheetNotProvided();
			propertiesNotProvided();
		}

		private void styleSheetNotProvided() {
			cssStyleSheet = getClass().getResource("notification.css").toExternalForm();
		}

		private final void propertiesNotProvided() {
			cssIdProperties = new Properties();
			cssIdProperties.setProperty(BOX_PROPERTY_KEY, DEFAULT_BOX_ID);
			cssIdProperties.setProperty(LABEL_CLOSE_PROPERTY_KEY, DEFAULT_LABEL_CLOSE_ID);
			cssIdProperties.setProperty(LABEL_NOTIFICATION_PROPERTY_KEY, DEFAULT_LABEL_NOTIFICATION_ID);
		}

		private final void initSizes() {
			this.boxPrefsizes = new PrefSizes() {

				@Override
				public Double getPrefHeight() {
					return 30.0;
				}

				@Override
				public Double getPrefWidth() {
					// TODO Auto-generated method stub
					return 428.0;
				}

			};

			this.labelClosePrefSizes = new PrefSizes() {

				@Override
				public Double getPrefHeight() {
					return 16.0;
				}

				@Override
				public Double getPrefWidth() {
					// TODO Auto-generated method stub
					return 17.0;
				}

			};
		}

		public final String getCssStyleSheet() {
			return cssStyleSheet;
		}

		public final void setCssStyleSheet(String cssStyleSheet) {
			this.cssStyleSheet = cssStyleSheet;
		}

		public final Properties getCssIdProperties() {
			return cssIdProperties;
		}

		public final void setCssIdProperties(Properties cssIdProperties) {
			this.cssIdProperties = cssIdProperties;
		}

		public Double getNotificationDuration() {
			return notificationDuration;
		}

		public void setNotificationDuration(Double notificationDuration) {
			this.notificationDuration = notificationDuration;
		}

		public Long getClosingDelay() {
			return closingDelay;
		}

		public void setClosingDelay(Long closingDelay) {
			this.closingDelay = closingDelay != null? closingDelay:this.closingDelay;
			autoClosure = closingDelay != null;
		}

		public String getTooltipCloseMessage() {
			return tooltipCloseMessage;
		}

		public void setTooltipCloseMessage(String tooltipCloseMessage) {
			this.tooltipCloseMessage = tooltipCloseMessage;
		}

		public PrefSizes getLabelClosePrefSizes() {
			return labelClosePrefSizes;
		}

		public void setLabelClosePrefSizes(PrefSizes labelClosePrefSizes) {
			this.labelClosePrefSizes = labelClosePrefSizes;
		}

		public PrefSizes getLabelMessagePrefSizes() {
			return labelMessagePrefSizes;
		}

		public void setLabelMessagePrefSizes(PrefSizes labelMessagePrefSizes) {
			this.labelMessagePrefSizes = labelMessagePrefSizes;
		}

		public PrefSizes getBoxPrefsizes() {
			return boxPrefsizes;
		}

		public void setBoxPrefsizes(PrefSizes boxPrefsizes) {
			this.boxPrefsizes = boxPrefsizes;
		}

		public boolean isAutoClosure() {
			return autoClosure;
		}

		public void setAutoClosure(boolean autoClosure) {
			this.autoClosure = autoClosure;
		}

	}

	private static VBox createBox(AnchorPane root) {
		VBox boxNotification = new VBox();
		root.getChildren().add(boxNotification);
		AnchorPane.setTopAnchor(boxNotification, -30.0);
		AnchorPane.setLeftAnchor(boxNotification, 30.0);
		AnchorPane.setRightAnchor(boxNotification, 30.0);
//		boxNotification.setPrefSize(428.0, 30.0);
		boxNotification.setPrefSize(notificationStyle.getBoxPrefsizes().getPrefWidth(),
				notificationStyle.getBoxPrefsizes().getPrefHeight());
		return boxNotification;
	}

	private static void styleBox(VBox boxNotification) {
		boxNotification.setId(notificationStyle.getCssIdProperties()
				.getProperty(NotificationApparence.BOX_PROPERTY_KEY,NotificationApparence.DEFAULT_BOX_ID));
		boxNotification.getStylesheets().add(notificationStyle.getCssStyleSheet());
	}

	private static void addContents(VBox boxNotification, String message, AnchorPane root) {
		
		HBox barreClosure = new HBox();
		barreClosure.setAlignment(Pos.CENTER_RIGHT);
		Label labelNotification = new Label();
		labelNotification.setText(message);
		labelNotification.setAlignment(Pos.CENTER);

		labelNotification.setId(notificationStyle.getCssIdProperties()
				.getProperty(NotificationApparence.LABEL_NOTIFICATION_PROPERTY_KEY,NotificationApparence.DEFAULT_LABEL_NOTIFICATION_ID));

		Label labelCloseNotification = new Label("x");
		barreClosure.getChildren().add(labelCloseNotification);
		labelCloseNotification.setTooltip(new Tooltip(notificationStyle.getTooltipCloseMessage()));
		labelCloseNotification.setId(notificationStyle.getCssIdProperties()
				.getProperty(NotificationApparence.LABEL_CLOSE_PROPERTY_KEY,NotificationApparence.DEFAULT_LABEL_CLOSE_ID));

//		labelCloseNotification.setPrefSize(17.0, 16.0);
		labelCloseNotification.setPrefSize(notificationStyle.getLabelClosePrefSizes().getPrefWidth(),
				notificationStyle.getLabelClosePrefSizes().getPrefHeight());
		labelCloseNotification.setOnMouseClicked(event -> {
			closeNotification(root, boxNotification);
		});
		
		if (notificationStyle.getLabelMessagePrefSizes() != null) {
			labelNotification.setPrefSize(notificationStyle.getLabelMessagePrefSizes().getPrefWidth(),
					notificationStyle.getLabelMessagePrefSizes().getPrefHeight());
		}
		
		
		boxNotification.setAlignment(Pos.CENTER);
		boxNotification.getChildren().add(barreClosure);
		boxNotification.getChildren().add(labelNotification);
	}

	public static void showNotification(AnchorPane root, String message, NotificationApparence config) {

		notificationStyle = config == null ? notificationStyle : config;

		VBox boxNotification = createBox(root);

		addContents(boxNotification, message, root);

		styleBox(boxNotification);

		// animating the box

		animateBox(config, boxNotification);
		defineAutoClosure(root,boxNotification);

//		notifications.add(boxNotification);
	}

	private static void animateBox(NotificationApparence config, VBox boxNotification) {
		TranslateTransition animation = new TranslateTransition(Duration.seconds((config == null? notificationStyle:config).getNotificationDuration()),
				boxNotification);
		animation.setToY(30.0);
		animation.play();
	}
	
	private static void defineAutoClosure(AnchorPane root, VBox boxNotification) {
		if(notificationStyle.isAutoClosure()) {
			
			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			
			Runnable task = () -> {
				Platform.runLater(() -> closeNotification(root, boxNotification));
			};
			executor.schedule(task , notificationStyle.getClosingDelay(),TimeUnit.MILLISECONDS);
			executor.shutdown();
		}
	}
	private static void closeNotification(AnchorPane root2, VBox boxNotification) {
		TranslateTransition animation = new TranslateTransition(
				Duration.seconds(notificationStyle.getNotificationDuration()), boxNotification);
		animation.setToY(0.0);
		animation.setOnFinished(event -> {
			root2.getChildren().remove(boxNotification);
//			notifications.remove(boxNotification);			//removing it from the list of active notifications
		});
		animation.play();

	}

	public static NotificationApparence getNotificationStyle() {
		return notificationStyle;
	}

	public static void setNotificationStyle(NotificationApparence notificationStyle) {
		GraphicFacilities.notificationStyle = notificationStyle;
	}

}
