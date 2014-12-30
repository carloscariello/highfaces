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
package org.highfaces.util;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Markus
 */
@ManagedBean(eager = true)
@ApplicationScoped
public final class HighFaces implements Serializable {

    private static final long serialVersionUID = -7529375563666411422L;
    public final static String version = "HighFaces-1.0-SNAPSHOT";
    
    public String getVersion() {
        return version;
    }

    public HighFaces() {
        LOG.log(Level.INFO, "Running on {0}", getVersion());
    }
    
    private static final Logger LOG = Logger.getLogger(HighFaces.class.getName());
}
