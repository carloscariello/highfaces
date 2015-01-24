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
package org.highfaces.showcase;

import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.highfaces.component.api.ChartModel;
import org.highfaces.component.api.impl.DefaultChartModel;
import org.highfaces.component.api.impl.DefaultChartSeries;

/**
 *
 * @author Markus
 */
@ManagedBean
@RequestScoped
public class ChartModelBean {

    protected DefaultChartModel model;

    public ChartModelBean() {
        reload();
    }

    public void reload() {
        model = new DefaultChartModel();
        DefaultChartSeries boysSeries = new DefaultChartSeries();
        DefaultChartSeries girlsSeries = new DefaultChartSeries();
        boysSeries.setName("Boys");
        girlsSeries.setName("Girls");
        
        Random r = new Random();
        for (int i = 2000; i < 2010; i++) {
            boysSeries.addPoint(Integer.toString(i), r.nextInt(500) + 800);
            girlsSeries.addPoint(Integer.toString(i), r.nextInt(500) + 800);
        }
        model.getSeries().add(boysSeries);
        model.getSeries().add(girlsSeries);
    }

    public ChartModel getModel() {
        return model;
    }

}
