package com.iths;

// import com.iths.spi.CurrencyConverter;
import com.iths.spi.Page;
import java.util.ServiceLoader;

public class PluginExample {
    public static void main(String[] args) {
        ServiceLoader <Page> PageLoader = ServiceLoader.load(Page.class);
        System.out.println("Testing");

        for(Page page : PageLoader){
            page.execute();
        }
    }
}
