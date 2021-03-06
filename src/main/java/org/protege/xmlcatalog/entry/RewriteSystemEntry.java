package org.protege.xmlcatalog.entry;

import java.net.URI;

import org.protege.xmlcatalog.EntryVisitor;
import org.protege.xmlcatalog.XmlBaseContext;

public class RewriteSystemEntry extends Entry {
    private String systemIdStartString;
    private URI rewritePrefix;
    
    public RewriteSystemEntry(String id, XmlBaseContext xmlBaseContext, String systemIdStartString, URI rewritePrefix) {
        super(id, xmlBaseContext);
        this.systemIdStartString = systemIdStartString;
        this.rewritePrefix = rewritePrefix;
    }

    public String getSystemIdStartString() {
        return systemIdStartString;
    }

    public void setSystemIdStartString(String systemIdStartString) {
        this.systemIdStartString = systemIdStartString;
    }

    public URI getRewritePrefix() {
        return rewritePrefix;
    }

    public void setRewritePrefix(URI rewritePrefix) {
        this.rewritePrefix = rewritePrefix;
    }

    @Override
    public void accept(EntryVisitor visitor) {
        visitor.visit(this);
    }
}
