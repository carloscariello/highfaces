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
package org.highfaces.util;

import java.io.IOException;
import java.io.Writer;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author Markus
 */
public class StreamResponseWriter extends ResponseWriter {

    private final Writer writer;
    protected boolean isElementOpen = false;

    public StreamResponseWriter(Writer writer) {
        super();
        this.writer = writer;
    }

    @Override
    public String getContentType() {
        return "";
    }

    @Override
    public String getCharacterEncoding() {
        return "UTF-8";
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void startDocument() throws IOException {

    }

    @Override
    public void endDocument() throws IOException {

    }

    protected void closeElement() throws IOException {
        isElementOpen = false;
        writer.write(">");
    }

    @Override
    public void startElement(String string, UIComponent uic) throws IOException {
        if (isElementOpen) {
            closeElement();
        }
        isElementOpen = true;
        writer.write("<" + string);
    }

    @Override
    public void endElement(String string) throws IOException {
        if (isElementOpen) {
            closeElement();
        }
        writer.write("</" + string + ">");
    }

    @Override
    public void writeAttribute(String string, Object o, String string1) throws IOException {
        writer.write(" " + string + "=\"" + o.toString() + "\"");
    }

    @Override
    public void writeURIAttribute(String string, Object o, String string1) throws IOException {
        writeAttribute(string, o, string1);
    }

    @Override
    public void writeComment(Object o) throws IOException {
        if (isElementOpen) {
            closeElement();
        }

    }

    @Override
    public void writeText(Object o, String string) throws IOException {
        if (isElementOpen) {
            closeElement();
        }
        writer.write(o.toString());
    }

    @Override
    public void writeText(char[] chars, int i, int i1) throws IOException {
        if (isElementOpen) {
            closeElement();
        }
        writer.write(chars, i, i1);
    }

    @Override
    public ResponseWriter cloneWithWriter(Writer writer) {
        return this;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (isElementOpen) {
            closeElement();
        }
        writer.write(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

}
