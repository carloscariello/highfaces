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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Markus
 */
@ManagedBean
@RequestScoped
public class MultAxisBean {

    protected List<Births> boys;
    protected List<Births> girls;
    protected List<AvgTemperature> avgTemp;

    public MultAxisBean() {
        reload();
    }

    public void reload() {
        boys = new ArrayList<>();
        girls = new ArrayList<>();
        avgTemp = new ArrayList<>();
        Random r = new Random();
        for (int i = 2000; i < 2010; i++) {
            boys.add(new Births(Integer.toString(i), r.nextInt(500) + 800));
            girls.add(new Births(Integer.toString(i), r.nextInt(500) + 800));
            avgTemp.add(new AvgTemperature(Integer.toString(i), new Double(r.nextInt(20) + 28 + r.nextFloat())));
        }
    }

    public List<Births> getBoys() {
        return boys;
    }

    public List<Births> getGirls() {
        return girls;
    }

    public List<AvgTemperature> getAvgTemp() {
        return avgTemp;
    }

}
