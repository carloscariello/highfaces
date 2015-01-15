/*
 * Copyright 2015 Bauer-Live Softwaredevelopment.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.highfaces.component.chartaxis;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

/**
 *
 * @author Markus
 */
@FacesComponent(value = "org.highfaces.component.ChartAxis")
public class ChartAxis extends UIOutput {

    private static final long serialVersionUID = -7804142010133827400L;

    public Object getPosition() {
        return Object.class.cast(this.getStateHelper().eval("position", null));
    }

    public void setPosition(Object value) {
        this.getStateHelper().put("position", value);
    }

    public Object getType() {
        return Object.class.cast(this.getStateHelper().eval("type", null));
    }

    public void setType(Object value) {
        this.getStateHelper().put("type", value);
    }
public Object getTickAngle() {
        return Object.class.cast(this.getStateHelper().eval("tickAngle", null));
    }

    public void setTickAngle(Object value) {
        this.getStateHelper().put("tickAngle", value);
    }
    public Object getFormat() {
        return Object.class.cast(this.getStateHelper().eval("format", null));
    }

    public void setFormat(Object value) {
        this.getStateHelper().put("format", value);
    }

    public Object getTitle() {
        return Object.class.cast(this.getStateHelper().eval("title", null));
    }

    public void setTitle(Object value) {
        this.getStateHelper().put("title", value);
    }

    public Object getStyle() {
        return Object.class.cast(this.getStateHelper().eval("style", null));
    }

    public void setStyle(Object value) {
        this.getStateHelper().put("style", value);
    }

    public Object getGridLineColor() {
        return Object.class.cast(this.getStateHelper().eval("gridLineColor", null));
    }

    public void setGridLineColor(Object value) {
        this.getStateHelper().put("gridLineColor", value);
    }

    public Object getGridLineWidth() {
        return Object.class.cast(this.getStateHelper().eval("gridLineWidth", null));
    }

    public void setGridLineWidth(Object value) {
        this.getStateHelper().put("gridLineWidth", value);
    }
    
    public Object getMin() {
        return Object.class.cast(this.getStateHelper().eval("min", null));
    }

    public void setMin(Object value) {
        this.getStateHelper().put("min", value);
    }

    public Object getMax() {
        return Object.class.cast(this.getStateHelper().eval("max", null));
    }

    public void setMax(Object value) {
        this.getStateHelper().put("max", value);
    }
}
