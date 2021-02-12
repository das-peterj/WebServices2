module core {
    requires com.iths.spi;
    //requires com.google.gson;
    //requires com.fasterxml.jackson.databind;
    //requires com.fasterxml.jackson.core;
    requires java.net.http;
    requires java.sql;


    uses com.iths.spi.Page;

    //Skiter i denna så länge
    // uses com.iths.spi.CurrencyConverter;
    uses Object;
    //opens com.iths.models to com.google.gson;
}