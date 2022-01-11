package com.urzaizcoding.ulangerproxy.graphics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.urzaizcoding.ulangerproxy.graphics.GraphicFacilities.NotificationApparence;

class GraphicFacilitiesTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void shouldCheckTheDefaultIdConfig() {
			
		//when
		NotificationApparence app = new NotificationApparence();
		
		assert(app.getCssIdProperties().getProperty(NotificationApparence.BOX_PROPERTY_KEY).equals(NotificationApparence.DEFAULT_BOX_ID));
		assert(app.getCssIdProperties().getProperty(NotificationApparence.LABEL_CLOSE_PROPERTY_KEY).equals(NotificationApparence.DEFAULT_LABEL_CLOSE_ID));
		assert(app.getCssIdProperties().getProperty(NotificationApparence.LABEL_NOTIFICATION_PROPERTY_KEY).equals(NotificationApparence.DEFAULT_LABEL_NOTIFICATION_ID));
	}

}
