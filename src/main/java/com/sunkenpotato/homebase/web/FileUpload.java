package com.sunkenpotato.homebase.web;

public record FileUpload(String filename, boolean isProtected) implements Serializable {
}
