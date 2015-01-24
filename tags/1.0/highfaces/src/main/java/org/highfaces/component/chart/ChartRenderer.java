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
import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import org.highfaces.component.api.ChartModel;
import org.highfaces.component.api.ChartSeries;
import org.highfaces.component.api.DataPoint;
import org.highfaces.component.api.impl.DefaultChartSeries;
import org.highfaces.component.chartaxis.ChartAxis;
import org.highfaces.component.chartlegend.ChartLegend;
import org.highfaces.component.chartserie.ChartSerie;
import org.highfaces.component.charttooltip.ChartTooltip;
import org.highfaces.util.JSONFunction;
import org.highfaces.util.StreamResponseWriter;
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

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        Map<String, String> externalParameters = context.getExternalContext().getRequestParameterMap();
        Chart cv = (Chart) component;

        // Behavior
        if (externalParameters.containsKey("javax.faces.behavior.event")) {

            String eventName = externalParameters.get("javax.faces.behavior.event");
            String sourceId = externalParameters.get("javax.faces.source");

            if ((sourceId != null) && (sourceId.equals(cv.getClientId()))) {
                if ("select".equals(eventName)) {

                    cv.select(externalParameters.get(cv.getClientId() + "_selectedSeries"), externalParameters.get(cv.getClientId() + "_selectedPoint"));
                }
                List<ClientBehavior> behaviorsForEvent = cv.getClientBehaviors().get(eventName);

                if (cv.getClientBehaviors().size() > 0) {
                    String behaviorSource = externalParameters.get("javax.faces.source");
                    String clientId = cv.getClientId(context);
                    if (behaviorSource != null && behaviorSource.equals(clientId)) {
                        for (ClientBehavior behavior : behaviorsForEvent) {
                            behavior.decode(context, cv);
                        }
                    }
                }
            }
        }

        //Change selected values
        String currvis = externalParameters.get(cv.getClientId() + "_selectedSeries");
        if (currvis != null) {
            cv.setSelectedSeries(currvis);
        }
        currvis = externalParameters.get(cv.getClientId() + "_selectedPoint");
        if (currvis != null) {
            cv.setSelectedPoint(currvis);
        }
    }

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

    @Override
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
        responseWriter.startElement("input", null);
        responseWriter.writeAttribute("type", "hidden", null);
        responseWriter.writeAttribute("id", clientId + "_selectedSeries", null);
        responseWriter.writeAttribute("name", clientId + "_selectedSeries", null);
        responseWriter.writeAttribute("value", cv.getSelectedSeries(), null);
        responseWriter.startElement("input", null);
        responseWriter.writeAttribute("type", "hidden", null);
        responseWriter.writeAttribute("id", clientId + "_selectedPoint", null);
        responseWriter.writeAttribute("name", clientId + "_selectedPoint", null);
        responseWriter.writeAttribute("value", cv.getSelectedPoint(), null);
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
            JSONObject plotOptions = new JSONObject();
            chart.put("type", cv.getType());
            high.put("chart", chart);
            // colors
            JSONObject credits = new JSONObject();
            if (cv.getCredits() != null) {
                if ("".equals(cv.getCredits()) || "false".equals(cv.getCredits())) {
                    credits.put("enabled", false);
                } else {
                    credits.put("text", cv.getCredits());
                    credits.put("href", "");
                }
            } else {
                credits.put("text", "highfaces.org");
                credits.put("href", "http://www.highfaces.org");
            }

            high.put("credits", credits);
            if (cv.getColors() != null) {
                JSONArray col = new JSONArray();
                String[] arr = cv.getColors().split(",");
                for (String arr1 : arr) {
                    col.put(arr1);
                }
                high.put("colors", col);
            }
            // stacking
            if (cv.getStacking() != null) {
                JSONObject series = new JSONObject();
                series.put("stacking", cv.getStacking());
                plotOptions.put("series", series);

            }

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
            encodeAxes(context, cv, high);
            JSONArray yaxes = high.optJSONArray("yAxis");
            if (yaxes == null) {
                yaxes = new JSONArray();
            }
            // Optional Child Objects
            for (UIComponent component : cv.getChildren()) {
                if (component instanceof ChartLegend) {
                    ChartLegend legend = (ChartLegend) component;
                    JSONObject l = new JSONObject();
                    if (legend.getStyle() != null) {
                        String[] separated = legend.getStyle().toString().split(";");
                        for (String s : separated) {
                            String[] sep2 = s.split(":");
                            l.put(sep2[0], sep2[1]);
                        }
                    }
                    if (legend.getLayout() != null) {
                        l.put("layout", legend.getLayout());
                    }
                    if (legend.getBackgroundColor() != null) {
                        l.put("backgroundColor", legend.getBackgroundColor());
                    }
                    if (legend.getPosition() != null) {
                        String pos = legend.getPosition().toString();
                        if (pos.startsWith("n")) {
                            l.put("verticalAlign", "top");
                        } else if (pos.startsWith("s")) {
                            l.put("verticalAlign", "bottom");
                        } else {
                            l.put("verticalAlign", "middle");
                        }
                        if (pos.endsWith("e")) {
                            l.put("align", "right");
                        } else if (pos.endsWith("w")) {
                            l.put("align", "left");
                        } else {
                            l.put("align", "center");
                        }
                    }
                    if (legend.getTitle() != null) {
                        JSONObject tt = new JSONObject();
                        tt.put("text", legend.getTitle());
                        l.put("title", tt);
                    }
                    high.put("legend", l);
                } else if (component instanceof ChartTooltip) {
                    ChartTooltip tooltip = (ChartTooltip) component;
                    JSONObject l = new JSONObject();
                    if (tooltip.getStyle() != null) {
                        JSONObject st = new JSONObject();
                        String[] separated = tooltip.getStyle().toString().split(";");
                        for (String s : separated) {
                            String[] sep2 = s.split(":");
                            st.put(sep2[0], sep2[1]);
                        }
                        l.put("style", st);
                    }

                    if (tooltip.getBackgroundColor() != null) {
                        l.put("backgroundColor", tooltip.getBackgroundColor());
                    }
                    if (tooltip.getShared() != null) {
                        l.put("shared", tooltip.getShared());
                    }
                    if (tooltip.getFacet("header") != null) {
                        UIComponent c = tooltip.getFacet("header");
                        ResponseWriter saved = FacesContext.getCurrentInstance().getResponseWriter();
                        StringWriter sw = new StringWriter();
                        StreamResponseWriter srw = new StreamResponseWriter(sw);

                        FacesContext.getCurrentInstance().setResponseWriter(srw);
                        c.encodeAll(context);
                        l.put("headerFormat", sw.toString());
                        l.put("useHTML", true);
                        FacesContext.getCurrentInstance().setResponseWriter(saved);
                    }
                    if (tooltip.getFacet("body") != null) {
                        UIComponent c = tooltip.getFacet("body");
                        ResponseWriter saved = FacesContext.getCurrentInstance().getResponseWriter();
                        StringWriter sw = new StringWriter();
                        StreamResponseWriter srw = new StreamResponseWriter(sw);

                        FacesContext.getCurrentInstance().setResponseWriter(srw);
                        c.encodeAll(context);
                        l.put("pointFormat", sw.toString());
                        l.put("useHTML", true);
                        FacesContext.getCurrentInstance().setResponseWriter(saved);
                    }
                    if (tooltip.getFacet("footer") != null) {
                        UIComponent c = tooltip.getFacet("footer");
                        ResponseWriter saved = FacesContext.getCurrentInstance().getResponseWriter();
                        StringWriter sw = new StringWriter();
                        StreamResponseWriter srw = new StreamResponseWriter(sw);

                        FacesContext.getCurrentInstance().setResponseWriter(srw);
                        c.encodeAll(context);
                        l.put("footerFormat", sw.toString());

                        FacesContext.getCurrentInstance().setResponseWriter(saved);
                        l.put("useHTML", true);
                    }
                    high.put("tooltip", l);
                }
            }
            // Encoding Series
            JSONArray series = new JSONArray();
            if (cv.getValue() != null) {
                encodeSimpleSeries(context, cv, series);
            }
            if (cv.getModel() != null) {
                if (cv.getModel() instanceof ChartModel) {
                    ChartModel model = (ChartModel) cv.getModel();
                    for (ChartSeries serie : model.getSeries()) {
                        JSONObject jSerie = encodeSerie(context, cv, serie, (List<DataPoint>) serie.getValue());
                        if (serie.getYaxis() != null) {
                            for (int i = 0; i < yaxes.length(); i++) {
                                if (yaxes.getJSONObject(i).optString("id", "").equals(serie.getYaxis())) {
                                    jSerie.put("yAxis", i);
                                }
                            }
                        }
                        series.put(jSerie);
                        // pie charts treatment
                        if ("pie".equals(serie.getType()) || "pie".equals(cv.getType())) {
                            JSONObject pie = new JSONObject();
                            pie.put("allowPointSelect", true);
                            pie.put("cursor", "pointer");

                            plotOptions.put("pie", pie);
                        }
                    }
                }
            }
            for (UIComponent comp : cv.getChildren()) {
                if (comp instanceof ChartSerie) {
                    ChartSerie serie = (ChartSerie) comp;
                    JSONObject jSerie = encodeSerie(context, cv, serie, (Collection) serie.getValue());
                    if (serie.getYaxis() != null) {
                        for (int i = 0; i < yaxes.length(); i++) {
                            if (yaxes.getJSONObject(i).optString("id", "").equals(serie.getYaxis())) {
                                jSerie.put("yAxis", i);
                            }
                        }
                    }
                    series.put(jSerie);
                    // pie charts treatment
                    if ("pie".equals(serie.getType()) || "pie".equals(cv.getType())) {
                        JSONObject pie = new JSONObject();
                        pie.put("allowPointSelect", true);
                        pie.put("cursor", "pointer");

                        plotOptions.put("pie", pie);
                    }
                }
            }
            high.put("series", series);
            if (plotOptions.length() != 0) {
                high.put("plotOptions", plotOptions);
            }
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
        if (serie != null && serie.getName() != null) {
            result.put("name", serie.getName());
        }
        if (serie != null && serie.getType() != null) {
            result.put("type", serie.getType());

        }
        if (serie != null && serie.getColorByPoint()) {
            result.put("colorByPoint", true);

        }
        JSONArray data = new JSONArray();
        Iterator it = coll.iterator();
        Integer index = 0;
        // Do we need Pie Charts treatment ?
        // find out if this series contains datetime stuff
        boolean needNames = (serie != null && ("pie".equals(serie.getType()) || ("pie".equals(cv.getType()) && serie.getType() == null)));
        if (!needNames && "datetime".equals(getAxisType(cv, serie))) {
            needNames = true;

        }

        while (it.hasNext()) {
            Object o = it.next();
            // assigning the value to the var
            if (serie != null && serie.getVar() != null) {
                Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
                requestMap.put(serie.getVar(), o);
            } else if (cv.getVar() != null) {
                Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
                requestMap.put(cv.getVar(), o);

            }
            if (cv.getRowIndexVar() != null) {
                Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
                requestMap.put(cv.getRowIndexVar(), index);
            }

            if (needNames) {
                Object value = null;
                Object name = null;
                if (serie != null && serie.getVar() != null && serie.getPoint() != null) {
                    if (serie.getValueExpression("point") != null) {
                        value = (new JSONFunction(serie.getPoint().toString()));
                    } else {
                        value = (new JSONFunction(context.getApplication().getELResolver().getValue(context.getELContext(), serie.getVar(), serie.getPoint()).toString()));
                    }
                } else if (cv.getVar() != null && cv.getPoint() != null) {
                    if (cv.getValueExpression("point") != null) {
                        value = (new JSONFunction(cv.getPoint().toString()));
                    } else {
                        value = (new JSONFunction(context.getApplication().getELResolver().getValue(context.getELContext(), cv.getVar(), cv.getPoint()).toString()));
                    }
                } else {
                    value = (new JSONFunction(o.toString()));
                }
                if (o instanceof DataPoint) {
                    value = ((DataPoint) o).getValue();
                    name = ((DataPoint) o).getName();
                    JSONArray a = new JSONArray();
                    if (name instanceof Date) {
                        a.put(((Date) name).getTime());
                    } else {
                        a.put(name);
                    }
                    a.put(value);
                    data.put(a);
                } else if (serie != null && serie.getTickLabel() != null) {
                    name = serie.getTickLabel();
                    JSONArray a = new JSONArray();
                    if (name instanceof Date) {
                        a.put(((Date) name).getTime());
                    } else {
                        a.put(name);
                    }
                    a.put(value);
                    data.put(a);
                } else if ((serie == null || serie.getVar() == null) && cv.getTickLabel() != null) {
                    name = cv.getTickLabel();
                    JSONArray a = new JSONArray();
                    if (name instanceof Date) {
                        a.put(((Date) name).getTime());
                    } else {
                        a.put(name);
                    }
                    a.put(value);
                    data.put(a);
                } else {
                    System.out.println("Serie: " + serie);
                    if (serie != null) {
                        System.out.println("serie.getVar()=" + serie.getVar());
                    }
                    System.out.println("cv.getTickLabel()=" + cv.getTickLabel());
                    data.put(value);
                }

            } else {
                if (o instanceof DataPoint) {
                    data.put(new JSONFunction(((DataPoint) o).getValue().toString()));
                } else if (serie != null && serie.getVar() != null && serie.getPoint() != null) {
                    if (serie.getValueExpression("point") != null) {
                        data.put(new JSONFunction(serie.getPoint().toString()));
                    } else {
                        data.put(new JSONFunction(context.getApplication().getELResolver().getValue(context.getELContext(), serie.getVar(), serie.getPoint()).toString()));
                    }
                } else if (cv.getVar() != null && cv.getPoint() != null) {
                    if (cv.getValueExpression("point") != null) {
                        data.put(new JSONFunction(cv.getPoint().toString()));
                    } else {
                        data.put(new JSONFunction(context.getApplication().getELResolver().getValue(context.getELContext(), cv.getVar(), cv.getPoint()).toString()));
                    }
                } else {
                    data.put(new JSONFunction(o.toString()));
                }
            }
            index++;
        }
        if (serie != null && serie.getVar() != null) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            requestMap.remove(serie.getVar());
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
        if (serie != null && serie.getDataLabel() != null) {
            JSONObject dataLabels = new JSONObject();
            dataLabels.put("enabled", true);
            dataLabels.put("format", serie.getDataLabel());
            result.put("dataLabels", dataLabels);
        }
        if (serie != null && (cv.getSelectedPoint() != null || cv.getSelectedSeries() != null)) {
            JSONObject point = new JSONObject();
            JSONObject events = new JSONObject();
            String escaped = cv.getClientId().replaceAll(":", "\\\\\\\\:");

            ClientBehaviorContext clientBehaviorContext = ClientBehaviorContext.createClientBehaviorContext(context, cv, "select", cv.getClientId(context), null);
            StringBuilder builder = new StringBuilder();

            for (ClientBehavior behavior : cv.getClientBehaviors().get("select")) {
                builder.append(behavior.getScript(clientBehaviorContext));
                builder.append(';');
            }

            String onclick = builder.toString();
            events.put("click", new JSONFunction("function () {$('#" + escaped + "_selectedSeries').val('" + serie.getName() + "');$('#" + escaped + "_selectedPoint').val(" + (("pie".equals(serie.getType()) || ("pie".equals(cv.getType()) && serie.getType() == null)) ? "this.name" : "this.category") + ");" + onclick + "}"));
            point.put("events", events);
            result.put("point", point);
        }
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
            if (o instanceof DataPoint) {
                Object ob = ((DataPoint) o).getName();
                if (ob instanceof Date) {
                    arr.put(((Date) ob).getTime());
                } else {
                    arr.put(ob.toString());
                }
            } else {
                Object ob = cv.getTickLabel();
                if (ob instanceof Date) {
                    arr.put(((Date) ob).getTime());
                } else {
                    arr.put(ob.toString());
                }
            }
            index++;
        }
        requestMap.remove(cv.getVar());
        if (cv.getRowIndexVar() != null) {
            requestMap.remove(cv.getRowIndexVar());
        }
        return arr;
    }

    private JSONArray encodeTickLabel(FacesContext context, Chart chart, ChartSerie cv) {
        JSONArray arr = null;
        if (cv.getValue() instanceof Map) {
            Iterator it = ((Map) cv.getValue()).entrySet().iterator();
            if (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (entry.getValue() instanceof Collection) {
                    arr = encodeTickLabel(context, chart, cv, (Collection) entry.getValue());
                }
            }
        } else if (cv.getValue() instanceof Collection) {
            arr = encodeTickLabel(context, chart, cv, (Collection) cv.getValue());
        }
        return arr;
    }

    private JSONArray encodeTickLabel(FacesContext context, Chart chart, ChartSeries cv, Collection collection) {
        JSONArray arr = new JSONArray();
        Iterator it = collection.iterator();

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Integer index = 0;
        while (it.hasNext()) {
            Object o = it.next();
            if (chart.getRowIndexVar() != null) {
                requestMap.put(chart.getRowIndexVar(), index);
            }
            if (cv.getVar() != null) {
                requestMap.put(cv.getVar(), o);
            }
            if (o instanceof DataPoint) {
                Object ob = ((DataPoint) o).getName();
                if (ob instanceof Date) {
                    arr.put(((Date) ob).getTime());
                } else {
                    arr.put(ob.toString());
                }
            } else {
                Object ob = cv.getTickLabel();
                if (ob instanceof Date) {
                    arr.put(((Date) ob).getTime());
                } else {
                    arr.put(ob.toString());
                }
            }
            index++;
        }
        if (cv.getVar() != null) {
            requestMap.remove(cv.getVar());
        }
        if (chart.getRowIndexVar() != null) {
            requestMap.remove(chart.getRowIndexVar());
        }
        return arr;
    }

    private void encodeAxes(FacesContext context, Chart cv, JSONObject high) throws JSONException, IOException {
        // First of all let's see if we have some declared ChartAxis components
        JSONArray xaxes = new JSONArray();
        JSONArray yaxes = new JSONArray();
        for (UIComponent component : cv.getChildren()) {
            if (component instanceof ChartAxis) {
                ChartAxis axis = (ChartAxis) component;
                JSONObject j = new JSONObject();
                if (axis.getId() != null) {
                    j.put("id", axis.getId());
                }
                if (axis.getType() != null) {
                    j.put("type", axis.getType());
                }
                if (axis.getMin() != null) {
                    j.put("min", axis.getMin());
                }
                if (axis.getMax() != null) {
                    j.put("max", axis.getMax());
                }
                if (axis.getGridLineColor() != null) {
                    j.put("gridLineColor", axis.getGridLineColor());
                }
                if (axis.getGridLineWidth() != null) {
                    j.put("gridLineWidth", axis.getGridLineWidth());
                }
                if (axis.getStyle() != null || axis.getFormat() != null || axis.getTickAngle() != null) {
                    JSONObject labels = new JSONObject();
                    if (axis.getFormat() != null) {
                        labels.put("format", axis.getFormat());
                    }
                    if (axis.getStyle() != null) {
                        String[] separated = axis.getStyle().toString().split(";");
                        JSONObject st = new JSONObject();
                        for (String s : separated) {
                            String[] sep2 = s.split(":");
                            st.put(sep2[0], sep2[1]);
                        }
                        labels.put("style", st);
                    }
                    if (axis.getTickAngle() != null) {
                        labels.put("rotation", new JSONFunction(axis.getTickAngle().toString()));
                    }
                    j.put("labels", labels);

                }
                if (axis.getTitle() != null) {
                    JSONObject axistitle = new JSONObject();
                    axistitle.put("text", axis.getTitle());
                    j.put("title", axistitle);
                }

                if ("e".equals(axis.getPosition()) || "n".equals(axis.getPosition())) {
                    j.put("opposite", true);
                }
                if ("n".equals(axis.getPosition()) || "s".equals(axis.getPosition())) {
                    xaxes.put(j);
                } else {
                    yaxes.put(j);
                }
            }
        }
        // now let's see if we have some in the ChartModel
        if (cv.getModel() != null && cv.getModel() instanceof ChartModel) {
            ChartModel model = (ChartModel) cv.getModel();

        }
        // Then let's analyze if something is defined in the chart itself
        if (cv.getValueExpression("tickLabel") != null && cv.getValue() != null) {
            if (xaxes.length() == 0) {
                JSONObject xAxis = new JSONObject();

                xAxis.put("categories", encodeTickLabel(context, cv));
                if (cv.getXaxisLabel() != null) {
                    JSONObject j = new JSONObject();
                    j.put("text", cv.getXaxisLabel());
                    xAxis.put("title", j);
                }
                xaxes.put(xAxis);

            }
        }

        // finally check the chart series for some tickLabel attributes
        if (xaxes.length() == 0) {
            if (cv.getModel() != null && cv.getModel() instanceof ChartModel) {
                ChartModel model = (ChartModel) cv.getModel();
                for (ChartSeries serie : model.getSeries()) {
                    if (serie.getValueExpression("tickLabel") != null && serie.getValue() != null) {
                        if (xaxes.length() == 0) {
                            JSONObject xAxis = new JSONObject();

                            xAxis.put("categories", encodeTickLabel(context, cv, serie, (Collection) serie.getValue()));
                            if (cv.getXaxisLabel() != null) {
                                JSONObject j = new JSONObject();
                                j.put("text", cv.getXaxisLabel());
                                xAxis.put("title", j);
                            }
                            xaxes.put(xAxis);
                        }
                    }
                }
            }
            if (xaxes.length() == 0) {
                for (UIComponent comp : cv.getChildren()) {
                    if (comp instanceof ChartSerie) {
                        ChartSerie serie = (ChartSerie) comp;

                        if (serie.getValueExpression("tickLabel") != null && serie.getValue() != null) {
                            if (xaxes.length() == 0) {
                                JSONObject xAxis = new JSONObject();

                                xAxis.put("categories", encodeTickLabel(context, cv, serie));
                                if (cv.getXaxisLabel() != null) {
                                    JSONObject j = new JSONObject();
                                    j.put("text", cv.getXaxisLabel());
                                    xAxis.put("title", j);
                                }
                                xaxes.put(xAxis);
                            }
                        }
                    }
                }
            }
        }

        // if nothing is found. let's do some default axes
        if (xaxes.length() == 0 && cv.getXaxisLabel() != null) {
            JSONObject xAxis = new JSONObject();

            JSONObject j = new JSONObject();
            j.put("text", cv.getXaxisLabel());
            xAxis.put("title", j);

            xaxes.put(xAxis);
        }
        if (yaxes.length() == 0 && cv.getYaxisLabel() != null) {
            JSONObject yAxis = new JSONObject();

            JSONObject j = new JSONObject();
            j.put("text", cv.getYaxisLabel());
            yAxis.put("title", j);

            yaxes.put(yAxis);
        }
        if (xaxes.length() == 0 && cv.getModel() != null) {
            ChartModel model = (ChartModel) cv.getModel();
            if (model.getSeries() != null) {
                ChartSeries serie = model.getSeries().get(0);
                JSONObject xAxis = new JSONObject();

                xAxis.put("categories", encodeTickLabel(context, cv, serie, (Collection) serie.getValue()));
                if (cv.getXaxisLabel() != null) {
                    JSONObject j = new JSONObject();
                    j.put("text", cv.getXaxisLabel());
                    xAxis.put("title", j);
                }
                xaxes.put(xAxis);
            }
        }
        /* if (xaxes.length() == 1) {
         high.put("xAxis", xaxes.getJSONObject(0));
         } else */ if (xaxes.length() > 0) {
            high.put("xAxis", xaxes);
        }
        /* if (yaxes.length() == 1) {
         high.put("yAxis", yaxes.getJSONObject(0));
         } else*/ if (yaxes.length() > 0) {
            high.put("yAxis", yaxes);
        }
    }

    private String getAxisType(Chart cv, ChartSeries serie) {
        String result = "category";
        for (UIComponent c : cv.getChildren()) {
            if (c instanceof ChartAxis) {
                ChartAxis axis = (ChartAxis) c;
                if ("s".equals(axis.getPosition()) || "n".equals(axis.getPosition())) {
                    if ("datetime".equals(axis.getType())) {
                        return "datetime";
                    }
                }
            }
        }

        return result;
    }

}
