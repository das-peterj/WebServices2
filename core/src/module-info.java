module core {
    requires com.iths.spi;
    requires com.google.gson;
    requires java.net.http;
    requires java.sql;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    //Skiter i denna så länge
    // uses com.iths.spi.CurrencyConverter;
    uses Object;

    opens com.iths.models to com.google.gson;
}