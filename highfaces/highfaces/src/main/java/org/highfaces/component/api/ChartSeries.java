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
package org.highfaces.component.api;

/**
 * This interface represents a single Chart series, e.g. a single line inside a
 * line chart. It contains methods needed to draw the series onto a chart as
 * well as a methods for point management
 *
 * @author Markus
 * @since 1.0
 *
 */
public interface ChartSeries {

    /* adds a point to the series. If you are adding multiple points, pay attention that the points have to be sorted before adding them. 
    
     */
    public void addPoint(Object where, Number value);
    /* sets the name of this series, displayed in the legend as well as the tooltip
    
     */

    public void setName(String name);

    /* retrieves the name of this series as displayed in the legend
    
     */
    public String getName();

    /* retrieves the EL expression for the point attribute of the current running variable
     * @see var
    
     */
    public Object getPoint();

    /* retrieves the name of the current running variable. This name can be used by e.g. the point expression.
     While the drawing step, this variable will hold all objects inside the value collection of this series.
     */
    public String getVar();

    /* The type of this series. Can be line, bar, column, are and pie
    
     */
    public String getType();

    /* This method is only used by the JSF component implementation */
    public Object getValueExpression(String point);

    /* this is the EL expression of the tickLabel (x-axis). If not provided and you use DataPoints, the name attribute of the DataPoint will be used. 
    
     */
    public Object getTickLabel();

    /* The ID of the Y axis to be used for this series. If not provided, the default y axis will be used
    
     */
    public Object getYaxis();

    /* With this method, it can be defined that for each point a different color should be used. This is typically the case if you have a column chart with only
     one single Series in it.
     */
    public Boolean getColorByPoint();

    /* This method returns the format for the dataLabel used e.g. in pie charts
    
     */
    public String getDataLabel();

    /*
     This method has to return a Collection of the values of this series. The individual values typically are instances of the DataPoint object.
     */
    public Object getValue();

}
