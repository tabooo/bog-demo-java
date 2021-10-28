package com.bog.demo.util;

import java.io.Serializable;

public class Descriptor implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean valid;

    private Object added;

    private String description;

    public Descriptor() {
    }

    public Descriptor(boolean valid) {
        this.valid = valid;
    }

    public Descriptor(boolean valid, String description) {

        this.valid = valid;
        this.description = description;
    }

    public Descriptor(boolean valid, Object added) {
        this.valid = valid;
        this.added = added;
    }

    public static Descriptor invalidDescriptor(String description) {
        return new Descriptor(Boolean.FALSE, description);
    }

    public static Descriptor invalidDescriptor() {
        return new Descriptor(Boolean.FALSE, null);
    }

    public static Descriptor invalidDescriptor(Object added, String description) {
        Descriptor descriptor = new Descriptor(Boolean.FALSE, description);
        descriptor.setAdded(added);
        return descriptor;
    }

    public static Descriptor validDescriptor(String description) {
        return new Descriptor(Boolean.TRUE, description);
    }

    public static Descriptor validDescriptor(Object added) {
        Descriptor descriptor = new Descriptor(Boolean.TRUE, null);
        descriptor.setAdded(added);
        return descriptor;
    }

    public static Descriptor validDescriptor(Object added, String description) {
        Descriptor descriptor = new Descriptor(Boolean.TRUE, description);
        descriptor.setAdded(added);
        return descriptor;
    }

    public static Descriptor validDescriptor() {
        return new Descriptor(Boolean.TRUE, null);
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Object getAdded() {
        return this.added;
    }

    public void setAdded(Object added) {
        this.added = added;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
