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
 * This interface represents a single Point inside a Chart series, especially
 * the value (y axis) and name (x-axis)
 *
 * @author Markus
 * @since 1.0
 *
 */
public interface DataPoint {

    /* This is the tick on the x axis for this point. If you have a Date-axis, this returns the Date object, else it should return a string to be displayed on the axis
    
     */
    Object getName();

    /* This is the tick on the y axis for this point. If you have a column char, remember that x and y axis are simply flipped, so here it would be the tick on the x axis.
    
     */
    Number getValue();

}
