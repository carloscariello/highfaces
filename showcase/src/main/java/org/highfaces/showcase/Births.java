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
import java.util.Date;

/**
 *
 * @author Markus
 */
public class Births implements Serializable {

    private static final long serialVersionUID = -6693851535463524178L;
    protected String year;
    protected Integer amount;
    protected Date date;

    public Date getDate() {
        return date;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Births() {
    }

    public Births(String year, Integer amount) {
        this.year = year;
        this.amount = amount;
    }

    /**
     *
     * @param date the value of date
     * @param amount the value of amount
     */
    public Births(Date date, Integer amount) {
        this.amount = amount;
        this.date = date;
    }

}
