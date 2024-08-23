package com.sunkenpotato.client2p.web;

public record FileUpload(String file_name, boolean is_protected, String owner) implements Serializable {}
