package com.sunkenpotato.client2p.web;

public record FileUpload(String name, boolean isProtected, String owner) implements Serializable {}
