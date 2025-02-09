/**
 * SimpleMetroArcGaugeTrial2.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.scene.control.gauge.linear;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.test.TestUtil;
import jfxtras.scene.control.gauge.linear.AbstractLinearGauge;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.scene.control.gauge.linear.elements.Indicator;
import jfxtras.scene.control.gauge.linear.elements.PercentMarker;
import jfxtras.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.scene.control.gauge.linear.elements.Segment;

/**
 * @author Tom Eugelink
 */
public class SimpleMetroArcGaugeTrial2 extends Application {
	
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

		List<SimpleMetroArcGauge> gauges = new ArrayList<SimpleMetroArcGauge>();
		
		// the border pane makes it resizeable
		BorderPane lBorderPane = new BorderPane();
		lBorderPane.setStyle("-fx-background-color: #eeeeee;");
		
        // gauge
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.getStyleClass().add("colorscheme-green-to-red-10");
			lSimpleMetroArcGauge.setStyle("-fxx-warning-indicator-visibility: visible; -fxx-error-indicator-visibility: visible; ");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lSimpleMetroArcGauge, i * 10.0, (i+1) * 10.0);
				lSimpleMetroArcGauge.segments().add(lSegment);
			}
			for (int i = 0; i <= 20; i++) {
				lSimpleMetroArcGauge.markers().add(new PercentMarker(lSimpleMetroArcGauge, i * 5.0));
			}
			lSimpleMetroArcGauge.indicators().add(new Indicator(0, "warning"));
			lSimpleMetroArcGauge.indicators().add(new Indicator(1, "error"));
			lBorderPane.setCenter(lSimpleMetroArcGauge);
			gauges.add(lSimpleMetroArcGauge);
		}
		
        
        // create scene
        Scene scene = new Scene(lBorderPane, 300, 300);
        scene.getStylesheets().add(AbstractLinearGauge.segmentColorschemeCSSPath());
        
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
        
        // start periodically changing the value of the gauges
		Thread t = new Thread( () -> {
			Random lRandom = new Random();
			while (true) {
				TestUtil.sleep(2000);
				Platform.runLater( () -> {
					double d = lRandom.nextDouble();
					for (SimpleMetroArcGauge g : gauges) {
				 		double minValue = g.getMinValue();
				 		double maxValue = g.getMaxValue();
						g.setValue(minValue + (d * (maxValue - minValue)));
					}
				});
				
			}
		});
		t.setDaemon(true);
		t.start();
    }
}

