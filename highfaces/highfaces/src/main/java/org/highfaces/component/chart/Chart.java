/*
 * Copyright 2014 Bauer-Live Softwaredevelopment.
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
package org.highfaces.component.chart;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

/**
 *
 * @author markus.bauer
 */
@FacesComponent(value = "org.highfaces.component.Chart")
public class Chart extends UIOutput {

    @Override
    public String getFamily() {
        return "org.highfaces.component";
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {

    }

    public String getStyle() {
        return String.class.cast(this.getStateHelper().eval("style", null));
    }

    public void setStyle(String value) {
        this.getStateHelper().put("style", value);
    }

    public String getVar() {
        return String.class.cast(this.getStateHelper().eval("var", null));
    }

    public void setVar(String value) {
        this.getStateHelper().put("var", value);
    }

    public String getExtender() {
        return String.class.cast(this.getStateHelper().eval("extender", null));
    }

    public void setExtender(String value) {
        this.getStateHelper().put("extender", value);
    }

    public String getRowIndexVar() {
        return String.class.cast(this.getStateHelper().eval("rowIndexVar", null));
    }

    public void setRowIndexVar(String value) {
        this.getStateHelper().put("rowIndexVar", value);
    }

    public String getType() {
        return String.class.cast(this.getStateHelper().eval("type", null));
    }

    public void setType(String value) {
        this.getStateHelper().put("type", value);
    }

    public String getXaxisLabel() {
        return String.class.cast(this.getStateHelper().eval("xaxisLabel", null));
    }

    public void setXaxisLabel(String value) {
        this.getStateHelper().put("xaxisLabel", value);
    }

    public String getYaxisLabel() {
        Object result = this.getStateHelper().eval("yaxisLabel", null);
        if (result != null) {
            return result.toString();
        } else {
            return null;
        }
    }

    public void setYaxisLabel(String value) {
        this.getStateHelper().put("yaxisLabel", value);
    }

    public String getTitle() {
        String result = String.class.cast(this.getStateHelper().eval("title", null));
        if (result == null) {
            result = "";
        }
        return result;
    }

    public void setTitle(String value) {
        this.getStateHelper().put("title", value);
    }

    public String getSubTitle() {
        String result = String.class.cast(this.getStateHelper().eval("subTitle", null));

        return result;
    }

    public void setSubTitle(String value) {
        this.getStateHelper().put("subTitle", value);
    }

    public String getHeight() {
        String s = String.class.cast(this.getStateHelper().eval("height", null));
        if (s == null) {
            return "300px";
        }
        if (!s.endsWith("%") && !s.endsWith("px")) {
            s += "px";
        }
        return s;
    }

    public void setHeight(String value) {
        this.getStateHelper().put("height", value);
    }

    public String getWidth() {
        String s = String.class.cast(this.getStateHelper().eval("width", null));
        if (s == null) {
            return null;
        }
        if (!s.endsWith("%") && !s.endsWith("px")) {
            s += "px";
        }
        return s;
    }

    public void setWidth(String value) {
        this.getStateHelper().put("width", value);
    }

    public Object getPoint() {
        try {
            return Object.class.cast(this.getStateHelper().eval("point", null));
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        }
        return null;
    }

    public void setPoint(Object value) {
        this.getStateHelper().put("point", value);
        ValueExpression f = getValueExpression("point");

        if (f != null) {

            ELContext elContext = this.getFacesContext().getELContext();

            f.setValue(elContext, value);
        }
    }

    public Object getModel() {
        try {
            return Object.class.cast(this.getStateHelper().eval("model", null));
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        }
        return null;
    }

    public void setModel(Object value) {
        this.getStateHelper().put("model", value);
        ValueExpression f = getValueExpression("model");

        if (f != null) {

            ELContext elContext = this.getFacesContext().getELContext();

            f.setValue(elContext, value);
        }
    }

    public Object getTickLabel() {
        return Object.class.cast(this.getStateHelper().eval("tickLabel", null));
    }

    public void setTickLabel(Object value) {
        this.getStateHelper().put("tickLabel", value);
    }
    private static final Logger LOG = Logger.getLogger(Chart.class.getName());
}
