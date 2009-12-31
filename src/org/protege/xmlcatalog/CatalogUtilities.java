package org.protege.xmlcatalog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;

import org.protege.xmlcatalog.entry.Entry;
import org.protege.xmlcatalog.exception.CatalogParseException;
import org.protege.xmlcatalog.parser.Handler;
import org.protege.xmlcatalog.redirect.UriRedirectVisitor;
import org.protege.xmlcatalog.write.XMLCatalogWriter;

public class CatalogUtilities {
    
    public static XMLCatalog parseDocument(URL catalog) throws IOException {
        return parseDocument(catalog, null);
    }
    
    public static XMLCatalog parseDocument(URL catalog, URI xmlbase) throws CatalogParseException {
        try {
            if (xmlbase == null) {
                xmlbase = catalog.toURI();
            }
            SAXParserFactory factory = SAXParserFactory.newInstance();
            Handler handler = new Handler(xmlbase);
            InputStream is = null;
            is = catalog.openStream();
            SAXParser parser = factory.newSAXParser();
            parser.parse(is, handler);
            return handler.getCatalog();
        }
        catch (Exception e) {
        	throw new CatalogParseException(e);
        }
    }
    
    public static void save(XMLCatalog catalog, File f) throws IOException {
        Writer writer = new FileWriter(f);
        XMLCatalogWriter xwriter = new XMLCatalogWriter(catalog, writer);
        try {
            xwriter.write();
            writer.flush();
            writer.close();
        }
        catch (ParserConfigurationException pce) {
            IOException ioe = new IOException("Error writing catalog to file " + f);
            ioe.initCause(pce);
            throw ioe;
        }
        catch (TransformerException te) {
            IOException ioe = new IOException("Error writing catalog to file " + f);
            ioe.initCause(te);
            throw ioe;  
        }
    }
    
    public static URI getRedirect(URI original, XMLCatalog catalog) {
        UriRedirectVisitor visitor = new UriRedirectVisitor(original);
        for (Entry subEntry : catalog.getEntries()) {
            subEntry.accept(visitor);
            if (visitor.getRedirect() != null) {
                break;
            }
        }
        return visitor.getRedirect() == null ? null : visitor.getRedirect();
    }
    

    public static URI resolveXmlBase(XmlBaseContext context) {
        URI xmlbase = null;
        while (xmlbase == null && context != null) {
            xmlbase = context.getXmlBase();
            context = context.getXmlBaseContext();
        }
        if (!xmlbase.isAbsolute()) {
            URI outerBase = resolveXmlBase(context);
            xmlbase = outerBase.resolve(xmlbase);
        }
        return xmlbase;
    }
}
