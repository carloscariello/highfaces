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
package org.highfaces.component.api.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import org.highfaces.component.api.ChartSeries;

/**
 *
 * @author markus.bauer
 */
public class DefaultChartSeries implements Serializable, ChartSeries {

    private static final long serialVersionUID = -5508901726185744784L;
    protected String name;
    protected String point;
    protected String var;
    protected String tickLabel;
    protected String yaxis;
    protected String type;
    protected String dataLabel;
    protected Boolean colorByPoint = false;
    protected List<DefaultDataPoint> data = new LinkedList<DefaultDataPoint>();

    @Override
    public void addPoint(Object where, Number value) {
        data.add(new DefaultDataPoint(where, value));
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getPoint() {
        return point;
    }

    @Override
    public String getVar() {
        return var;
    }

    @Override
    public Object getValueExpression(String point) {
        return null;
    }

    @Override
    public Object getTickLabel() {
        return tickLabel;
    }

    @Override
    public Object getYaxis() {
        return yaxis;
    }

    public String getType() {
        return type;
    }

    public Boolean getColorByPoint() {
        return colorByPoint;
    }

    public String getDataLabel() {
        return dataLabel;
    }

    @Override
    public Object getValue() {
        return data;
    }

    public List<DefaultDataPoint> getData() {
        return data;
    }

    public void setYaxis(String yaxis) {
        this.yaxis = yaxis;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDataLabel(String dataLabel) {
        this.dataLabel = dataLabel;
    }

    public void setColorByPoint(Boolean colorByPoint) {
        this.colorByPoint = colorByPoint;
    }

}
