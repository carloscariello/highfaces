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
package org.highfaces.component.chartserie;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;
import org.highfaces.component.api.ChartSeries;

/**
 *
 * @author Markus
 */
@FacesComponent(value = "org.highfaces.component.ChartSerie")
public class ChartSerie extends UIOutput implements ChartSeries {

    private static final long serialVersionUID = -5956990138432279114L;

    public String getName() {
        String result = String.class.cast(this.getStateHelper().eval("name", null));
        if (result == null) {
            result = "";
        }
        return result;
    }

    public void setName(String value) {
        this.getStateHelper().put("name", value);
    }

    public String getVar() {
        return String.class.cast(this.getStateHelper().eval("var", null));
    }

    public void setVar(String value) {
        this.getStateHelper().put("var", value);
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

    @Override
    public Object getTickLabel() {
        return Object.class.cast(this.getStateHelper().eval("tickLabel", null));
    }

    public void setTickLabel(Object value) {
        this.getStateHelper().put("tickLabel", value);
    }

    public Object getYaxis() {
        return Object.class.cast(this.getStateHelper().eval("yaxis", null));
    }

    public void setYaxis(Object value) {
        this.getStateHelper().put("yaxis", value);
    }

    public String getType() {
        return String.class.cast(this.getStateHelper().eval("type", null));
    }

    public void setType(String value) {
        this.getStateHelper().put("type", value);
    }

    public String getDataLabel() {
        return String.class.cast(this.getStateHelper().eval("dataLabel", null));
    }

    public void setDataLabel(String value) {
        this.getStateHelper().put("dataLabel", value);
    }

    public Boolean getColorByPoint() {
        Boolean result = Boolean.class.cast(this.getStateHelper().eval("colorByPoint", null));
        if (result == null) {
            result = false;
        }
        return result;
    }

    public void setColorByPoint(Boolean value) {
        this.getStateHelper().put("colorByPoint", value);
    }

    @Override
    public void addPoint(Object where, Number value) {

    }
    private static final Logger LOG = Logger.getLogger(ChartSerie.class.getName());
}
