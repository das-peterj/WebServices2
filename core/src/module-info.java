module core {
    requires com.google.gson;
    requires java.net.http;
    requires java.sql;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    opens com.iths to com.google.gson;
}