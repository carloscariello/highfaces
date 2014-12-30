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
package org.highfaces.component.chart;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import org.highfaces.component.api.ChartSeries;
import org.highfaces.component.api.impl.DefaultChartSeries;
import org.highfaces.component.chartserie.ChartSerie;
import org.highfaces.util.JSONFunction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author markus.bauer
 */
@ResourceDependencies({
    @ResourceDependency(name = "jsf.js", target = "head", library = "javax.faces"),
    @ResourceDependency(library = "highfaces", name = "highfaces.css"),
    @ResourceDependency(library = "highfaces", name = "highfaces.js")})
@FacesRenderer(componentFamily = "org.highfaces.component", rendererType = "org.highfaces.component.ChartRenderer")
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class ChartRenderer extends Renderer implements ComponentSystemEventListener {

    /* renderes jQuery-Script inclusion conditionally. After the jQuery Script, it includes HighCharts script */
    public void renderScript(FacesContext context) {
        UIComponent headFacet = context.getViewRoot().getFacet("javax_faces_location_HEAD");
        if (headFacet == null) {
            return;
        }
        boolean needjQuery = true;
        for (UIComponent c : headFacet.getChildren()) {
            if (c.getAttributes().get("name").toString().endsWith("jquery.js")) {
                needjQuery = false;
            }
        }
        if (needjQuery) {
            UIOutput jquery = (UIOutput) context.getApplication().createComponent(context, "javax.faces.Output", "javax.faces.resource.Script");
            jquery.getAttributes().put("library", "highfaces");
            jquery.getAttributes().put("name", "jquery/jquery.js");
            jquery.getAttributes().put("target", "head");
            jquery.setId("highfaces-resource-jquery");
            jquery.setRendererType("javax.faces.resource.Script");
            context.getViewRoot().addComponentResource(context, jquery, "head");
        }
        UIOutput high = (UIOutput) context.getApplication().createComponent(context, "javax.faces.Output", "javax.faces.resource.Script");
        high.getAttributes().put("library", "highfaces");
        high.getAttributes().put("name", "highcharts/highcharts.js");
        high.getAttributes().put("target", "head");
        high.setId("highfaces-resource-highcharts");
        high.setRendererType("javax.faces.resource.Script");
        context.getViewRoot().addComponentResource(context, high, "head");
    }

    public void processEvent(final ComponentSystemEvent event) throws AbortProcessingException {
        if (event instanceof PostAddToViewEvent) {
            final FacesContext context = FacesContext.getCurrentInstance();
            renderScript(context);
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
        for (UIComponent comp : component.getChildren()) {

        }
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        Chart cv = (Chart) component;
        String clientId = cv.getClientId(context);

        String style = cv.getStyle();
        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.startElement("div", null);
        responseWriter.writeAttribute("class", "hc-chartframe", "class");
        responseWriter.writeAttribute("id", clientId, "id");
        if (style != null) {
            responseWriter.writeAttribute("style", style, "style");
        }

    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);
        Chart chart = (Chart) component;
        String clientId = chart.getClientId(context);
        encodeChart(context, chart);
        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.endElement("div");

    }

    private void encodeChart(FacesContext context, Chart cv) throws IOException {
        try {
            ResponseWriter responseWriter = context.getResponseWriter();
            responseWriter.startElement("div", null);
            String div_id = cv.getClientId(context).replace(':', '_') + "_chart";
            String style = "height:";
            style += cv.getHeight();
            if (cv.getWidth() != null) {
                style += ";width:" + cv.getWidth();
            }
            responseWriter.writeAttribute("style", style, null);
            responseWriter.writeAttribute("id", div_id, null);
            responseWriter.endElement("div");
            responseWriter.startElement("script", null);
            responseWriter.writeAttribute("type", "text/javascript", null);

            responseWriter.write("$(function () {");
            responseWriter.write("var options = ");

            JSONObject high = new JSONObject();
            JSONObject chart = new JSONObject();
            chart.put("type", cv.getType());
            high.put("chart", chart);
            // Title Object
            JSONObject title = new JSONObject();
            title.put("text", cv.getTitle());
            high.put("title", title);
            // Subtitle Object
            if (cv.getSubTitle() != null) {
                JSONObject stitle = new JSONObject();
                stitle.put("text", cv.getSubTitle());
                high.put("subtitle", stitle);
            }
            // Axis Objects
            boolean renderedXAxis = false;
            boolean renderedYAxis = false;
            if (cv.getFacet("xAxis") != null) {
                renderedXAxis = true;
            }
            if (cv.getFacet("yAxis") != null) {
                LOG.warning("found yaxis facet");
                renderedYAxis = true;
            }
            if (cv.getValueExpression("tickLabel") != null && cv.getValue() != null) {
                if (!renderedXAxis) {
                    JSONObject xAxis = new JSONObject();
                    renderedXAxis = true;
                    xAxis.put("categories", encodeTickLabel(context, cv));
                    if (cv.getXaxisLabel() != null) {
                        JSONObject j = new JSONObject();
                        j.put("text", cv.getXaxisLabel());
                        xAxis.put("title", j);
                    }
                    high.put("xAxis", xAxis);
                }
            }
            if (!renderedXAxis && cv.getXaxisLabel() != null) {
                JSONObject xAxis = new JSONObject();
                renderedXAxis = true;
                JSONObject j = new JSONObject();
                j.put("text", cv.getXaxisLabel());
                xAxis.put("title", j);

                high.put("xAxis", xAxis);
            }
            if (!renderedYAxis && cv.getYaxisLabel() != null) {
                JSONObject yAxis = new JSONObject();
                renderedYAxis = true;
                JSONObject j = new JSONObject();
                j.put("text", cv.getYaxisLabel());
                yAxis.put("title", j);

                high.put("yAxis", yAxis);
            } 
            // Encoding Series
            JSONArray series = new JSONArray();
            if (cv.getValue() != null) {
                encodeSimpleSeries(context, cv, series);
            }
            for (UIComponent comp : cv.getChildren()) {
                if (comp instanceof ChartSerie) {
                    ChartSerie serie = (ChartSerie) comp;
                    series.put(encodeSerie(context, cv, serie, (Collection) serie.getValue()));
                    if (!renderedXAxis) {
                        
                    }
                }
            }
            high.put("series", series);
            responseWriter.write(high.toString());
            responseWriter.write(";");
            if (cv.getExtender() != null) {
                responseWriter.write("options.extender = " + cv.getExtender() + ";");
                responseWriter.write("options.extender();");
            }
            responseWriter.write("$('#" + div_id + "').highcharts(options);");
            responseWriter.write("});");

            responseWriter.endElement("script");
        } catch (JSONException ex) {
            Logger.getLogger(ChartRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void encodeSimpleSeries(FacesContext context, Chart cv, JSONArray target) throws IOException, JSONException {
        if (cv.getValue() instanceof Map) {
            for (Object o : ((Map) cv.getValue()).entrySet()) {
                Map.Entry e = (Map.Entry) o;
                DefaultChartSeries serie = new DefaultChartSeries();
                serie.setName(e.getKey().toString());
                target.put(encodeSerie(context, cv, serie, (Collection) e.getValue()));
            }
        } else if (cv.getValue() instanceof Collection) {
            Collection coll = (Collection) cv.getValue();
            target.put(encodeSerie(context, cv, null, coll));
        }
    }

    private JSONObject encodeSerie(FacesContext context, Chart cv, ChartSeries serie, Collection coll) throws IOException, JSONException {
        JSONObject result = new JSONObject();
        if (serie.getName() != null) {
            result.put("name", serie.getName());
        }
        JSONArray data = new JSONArray();
        Iterator it = coll.iterator();
        Integer index = 0;
        while (it.hasNext()) {
            Object o = it.next();
            // assigning the value to the var
            if (cv.getVar() != null) {
                Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
                requestMap.put(cv.getVar(), o);

            }
            if (cv.getRowIndexVar() != null) {
                Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
                requestMap.put(cv.getRowIndexVar(), index);
            }

            if (cv.getVar() != null && cv.getPoint() != null) {
                if (cv.getValueExpression("point") != null) {
                    data.put(new JSONFunction(cv.getPoint().toString()));
                } else {
                    data.put(new JSONFunction(context.getApplication().getELResolver().getValue(context.getELContext(), cv.getVar(), cv.getPoint()).toString()));
                }
            } else {
                data.put(new JSONFunction(o.toString()));
            }
            index++;
        }
        if (cv.getVar() != null) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            requestMap.remove(cv.getVar());
        }
        if (cv.getRowIndexVar() != null) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            requestMap.remove(cv.getRowIndexVar());
        }
        result.put("data", data);
        return result;
    }
    private static final Logger LOG = Logger.getLogger(ChartRenderer.class.getName());

    private JSONArray encodeTickLabel(FacesContext context, Chart cv) throws IOException {
        JSONArray arr = null;
        if (cv.getValue() instanceof Map) {
            Iterator it = ((Map) cv.getValue()).entrySet().iterator();
            if (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (entry.getValue() instanceof Collection) {
                    arr = encodeTickLabel(context, cv, (Collection) entry.getValue());
                }
            }
        } else if (cv.getValue() instanceof Collection) {
            arr = encodeTickLabel(context, cv, (Collection) cv.getValue());
        }
        return arr;
    }

    private JSONArray encodeTickLabel(FacesContext context, Chart cv, Collection collection) throws IOException {
        JSONArray arr = new JSONArray();
        Iterator it = collection.iterator();
        boolean isFirst = true;
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Integer index = 0;
        while (it.hasNext()) {
            Object o = it.next();
            if (cv.getRowIndexVar() != null) {
                requestMap.put(cv.getRowIndexVar(), index);
            }

            isFirst = false;

            requestMap.put(cv.getVar(), o);
            arr.put(cv.getTickLabel().toString());
            index++;
        }
        requestMap.remove(cv.getVar());
        if (cv.getRowIndexVar() != null) {
            requestMap.remove(cv.getRowIndexVar());
        }
        return arr;
    }

    

   
}
