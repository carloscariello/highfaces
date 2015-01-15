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
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Markus
 */
@ManagedBean
@RequestScoped
public class DateSeriesBean {
    
    
    protected List<Births> boys;
   
    
    public List<Births> getBoys() {
        return boys;
    }
    
    
    public DateSeriesBean() {
        reload();
    }
    
    public void reload() {
        
        boys = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -10);
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            boys.add(new Births(c.getTime(), r.nextInt(500) + 800));
            c.add(Calendar.DATE, 1);
            
        }
               
    }

}
