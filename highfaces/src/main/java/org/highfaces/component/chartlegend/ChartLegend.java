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
package org.highfaces.component.chartlegend;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

/**
 *
 * @author Markus
 */
@FacesComponent(value = "org.highfaces.component.ChartLegend")
public class ChartLegend extends UIOutput {

    private static final long serialVersionUID = -8963882331197530818L;

    public Object getPosition() {
        return Object.class.cast(this.getStateHelper().eval("position", null));
    }

    public void setPosition(Object value) {
        this.getStateHelper().put("position", value);
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

    public Object getLayout() {
        return Object.class.cast(this.getStateHelper().eval("layout", null));
    }

    public void setLayout(Object value) {
        this.getStateHelper().put("layout", value);
    }

    public Object getBackgroundColor() {
        return Object.class.cast(this.getStateHelper().eval("backgroundColor", null));
    }

    public void setBackgroundColor(Object value) {
        this.getStateHelper().put("backgroundColor", value);
    }
}
