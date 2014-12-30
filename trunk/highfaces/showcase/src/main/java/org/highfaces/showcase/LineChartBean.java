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
package org.highfaces.showcase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author markus.bauer
 */
@ManagedBean
@RequestScoped
public class LineChartBean {

    protected List<Integer> simpleList;
    protected List<Births> boys;
    protected List<Births> girls;
    protected Map<String, List<Births>> mappedList;

    public List<Integer> getSimpleList() {
        return simpleList;
    }

    public List<Births> getBoys() {
        return boys;
    }

    public List<Births> getGirls() {
        return girls;
    }

    public Map<String, List<Births>> getMappedList() {
        return mappedList;
    }

    public LineChartBean() {
        reload();
    }

    public void reload() {
        simpleList = new ArrayList<>();
        boys = new ArrayList<>();
        girls = new ArrayList<>();
        mappedList = new HashMap<>();
        Random r = new Random();
        for (int i = 2000; i < 2010; i++) {
            simpleList.add(r.nextInt(500) + 800);
            boys.add(new Births(Integer.toString(i), r.nextInt(500) + 800));
            girls.add(new Births(Integer.toString(i), r.nextInt(500) + 800));

        }
        mappedList.put("boys", boys);
        mappedList.put("girls", girls);

    }

}
